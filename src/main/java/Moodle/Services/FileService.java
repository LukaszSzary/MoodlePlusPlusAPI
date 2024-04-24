package Moodle.Services;

import Moodle.Model.*;
import Moodle.Repositories.CoursesRepository;
import Moodle.Repositories.FilesRepository;
import Moodle.Repositories.TasksRepository;
import Moodle.Security.StorageProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {
    private final Path rootPath;
    private final TasksRepository tasksRepository;
    private final FilesRepository filesRepository;
    private final CoursesRepository coursesRepository;
    public FileService(StorageProperties properties, TasksRepository tasksRepository, FilesRepository filesRepository, CoursesRepository coursesRepository){
        this.rootPath= Paths.get(properties.getRootLocation());
        this.tasksRepository = tasksRepository;
        this.filesRepository = filesRepository;
        this.coursesRepository = coursesRepository;
    }

    public Files saveFile(MultipartFile file, int taskId, Users authenticatedUser) throws Exception{
        Tasks task = tasksRepository.findById(taskId).orElseThrow(()->new Exception("No such task exist"));
        //check if user has authority to save file to this task
        if(!(task.getCourse().getCourse_students().contains(authenticatedUser) || task.getCourse().getCourse_owners().contains(authenticatedUser))){
            throw new Exception("You are neither tutor nor student of course that contains this task");
        }

        //checks if it's past start time for submitting
        if (LocalDate.now().isBefore(task.getDate_of_start())) {
            throw new Exception("It's to early to submit files");
        }
        //checks if the time for submitting has not ended
        if(task.getDate_of_end() != null) {
            if (LocalDate.now().isAfter(task.getDate_of_end())) {
                throw new Exception("Time for submitting files has ended");
            }
        }
        //checks if the file has proper extension
        if(!(task.getAvailable_file_extensions().isEmpty() || task.getAvailable_file_extensions() == null)){
            String[] extensions =  task.getAvailable_file_extensions().split(";");
            String fileExtension = Arrays.stream(file.getOriginalFilename().split("\\.")).toList().getLast();
            if(!Arrays.stream(extensions).toList().contains(fileExtension)) {
                throw new Exception("Invalid file extension");
            }
        }
        //check if user can add another file
        if(task.getMax_total_files_volume() == task.getFiles().stream().filter(n-> n.getUser().equals(authenticatedUser)).count()){
            throw new Exception("Max number of uploaded files reached");
        }
        //check naming if convention is preserved
        String properBeginning = task.getTitle() + authenticatedUser.getName() + authenticatedUser.getSurname();
        if(!file.getOriginalFilename().startsWith(properBeginning)){
            throw new Exception("File name defies naming convention(tasktitle+name+surname+'whateverYouWant')");
        }

        //create file entity
        Files newFile = new Files();
        newFile.setDate_of_upload(LocalDate.now());
        newFile.setTask(task);
        newFile.setName(file.getOriginalFilename());
        newFile.setVolume((int)(file.getSize())/1024);
        newFile.setExtension(Arrays.stream(file.getOriginalFilename().split("\\.")).toList().getLast());
        newFile.setUser(authenticatedUser);
        filesRepository.save(newFile);

        //save file
        Path pathToFile = Paths.get(rootPath + File.separator + task.getCourse().getTitle() + File.separator + task.getTitle() + File.separator + file.getOriginalFilename());
        InputStream inputStream = file.getInputStream();
        java.nio.file.Files.copy(inputStream, pathToFile, StandardCopyOption.REPLACE_EXISTING);

        return newFile;
    }
    public List<Files> getUserFilesSubmittedForTask (int taskId, Users authenticatedUser) throws Exception{
        Tasks task = tasksRepository.findById(taskId).orElseThrow(()->new Exception("No such task exist"));
        return task.getFiles().stream().filter(e->e.getUser().equals(authenticatedUser)).collect(Collectors.toList());
    }
    public boolean deleteFile(int fileId, Users authenticatedUser) throws Exception{
        Files file = filesRepository.findById(fileId).orElseThrow(()->new Exception("There is no such file"));
        if(!file.getUser().equals(authenticatedUser)){
            throw new Exception("This is not yours file, you can't delete it");
        }
        File fileOnDisc = new File(rootPath + File.separator + file.getTask().getCourse().getTitle() + File.separator + file.getTask().getTitle() + File.separator + file.getName());
        if(fileOnDisc.delete()){
            filesRepository.delete(file);
            return true;
        }
        return false;
    }
    public File downloadCourse(int id, Users authenticatedUser) throws Exception {
        Courses course = coursesRepository.findById(id).orElseThrow(() -> new Exception("Course not found"));
        if (!(course.getCourse_owners().contains(authenticatedUser) || authenticatedUser.getRole().equals(Role.admin))) {
            throw new Exception("You have no right to download it");
        }
        String pathToFolder = rootPath + File.separator + course.getTitle();
        String pathToZipFolder = rootPath + File.separator + course.getTitle() + ".zip";
        zipFolder(pathToFolder, pathToZipFolder);
        return new File(pathToZipFolder);
    }
    public File downloadTask(int id, Users authenticatedUser) throws Exception {
        Tasks tasks = tasksRepository.findById(id).orElseThrow(() -> new Exception("Task not found"));
        if (!(tasks.getCourse().getCourse_owners().contains(authenticatedUser) || authenticatedUser.getRole().equals(Role.admin))) {
            throw new Exception("You have no right to download it");
        }
        String pathToFolder = rootPath + File.separator + tasks.getCourse().getTitle() + File.separator + tasks.getTitle();
        String pathToZipFolder = rootPath + File.separator + tasks.getTitle() + ".zip";
        zipFolder(pathToFolder, pathToZipFolder);
        return new File(pathToZipFolder);
    }
    private static void zipFolder(String sourceFolder, String zipFilePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        ZipOutputStream zos = new ZipOutputStream(fos);
        File folder = new File(sourceFolder);
        zipFile(folder, folder.getName(), zos);
        zos.close();
        fos.close();
    }
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

}
