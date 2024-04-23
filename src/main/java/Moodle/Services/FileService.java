package Moodle.Services;

import Moodle.Model.Files;
import Moodle.Model.Tasks;
import Moodle.Model.Users;
import Moodle.Repositories.FilesRepository;
import Moodle.Repositories.TasksRepository;
import Moodle.Security.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.spi.DateFormatProvider;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

@Service
public class FileService {
    private final Path rootPath;
    private final TasksRepository tasksRepository;
    private final FilesRepository filesRepository;
    public FileService(StorageProperties properties, TasksRepository tasksRepository, FilesRepository filesRepository){
        this.rootPath= Paths.get(properties.getRootLocation());
        this.tasksRepository = tasksRepository;
        this.filesRepository = filesRepository;
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
}
