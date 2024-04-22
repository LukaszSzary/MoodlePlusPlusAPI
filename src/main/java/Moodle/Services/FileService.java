package Moodle.Services;

import Moodle.Model.Tasks;
import Moodle.Repositories.FilesRepository;
import Moodle.Repositories.TasksRepository;
import Moodle.Security.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.spi.DateFormatProvider;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

@Service
public class FileService {
    private final Path rootPath;
    private final TasksRepository tasksRepository;
    private final FilesRepository filesRepository;
    @Autowired
    public FileService(StorageProperties properties, TasksRepository tasksRepository, FilesRepository filesRepository){
        this.rootPath= Paths.get(properties.getRootLocation());
        this.tasksRepository = tasksRepository;
        this.filesRepository = filesRepository;
    }

    public void saveFile(MultipartFile file,int taskId) throws Exception{
        Tasks task = tasksRepository.findById(taskId).orElseThrow(()->new Exception("No such task exist"));
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
        if(task.getMax_total_files_volume() != 0 && task.getMax_total_files_volume() == task.getFiles().size()){
            throw new Exception("Max number of uploaded files reached");
        }

       System.out.println(Arrays.stream(file.getOriginalFilename().split("\\.")).toList().getLast());
        System.out.println( file.getInputStream());

        System.out.println(rootPath);



    }
}
