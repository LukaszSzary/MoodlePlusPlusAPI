package Moodle.Services;

import Moodle.Dto.TaskDto;
import Moodle.Model.Courses;
import Moodle.Model.Tasks;
import Moodle.Model.Users;
import Moodle.Repositories.CoursesRepository;
import Moodle.Repositories.TasksRepository;
import Moodle.Security.StorageProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class TaskService {
    private final TasksRepository tasksRepository;
    private final CoursesRepository coursesRepository;
    private final StorageProperties storageProperties;

    public TaskService(TasksRepository tasksRepository, CoursesRepository coursesRepository, StorageProperties storageProperties) {
        this.tasksRepository = tasksRepository;
        this.coursesRepository = coursesRepository;
        this.storageProperties = storageProperties;
    }

    public Tasks addTask(TaskDto taskDto, int courseId) throws  Exception{
        Courses course = coursesRepository.findById(courseId).orElseThrow(()-> new Exception("Course with this id does not exists"));
        Tasks task = new Tasks();
        task.setTitle(taskDto.getTitle());
        task.setContents(taskDto.getContents());
        task.setMin_total_files_volume(taskDto.getMin_total_files_volume());
        task.setMax_total_files_volume(taskDto.getMax_total_files_volume());
        task.setDate_of_start(taskDto.getDate_of_start());
        task.setDate_of_end(taskDto.getDate_of_end());
        task.setAvailable_file_extensions(taskDto.getAvailable_file_extensions().toLowerCase().trim());
        task.setCourse(course);
        Files.createDirectory(Paths.get(storageProperties.getRootLocation()+ File.separator+course.getTitle()+ File.separator+task.getTitle()));
        tasksRepository.save(task);
        return task;
    }

    public Tasks getTaskById(int id) throws Exception{
        return tasksRepository.findById(id).orElseThrow(()->new Exception("Task does not exists"));
    }

    public List<Tasks> getTasksFromCourse(int id) throws Exception{
        Courses course = coursesRepository.findById(id).orElseThrow(()->new Exception("Course does not exists"));
        return course.getTasks();
    }

    public boolean deleteTask(int id, Users authenticatedUser ) throws Exception{
        Tasks task = tasksRepository.findById(id).orElseThrow(()->new Exception("Task does not exists"));
        if(!task.getCourse().getCourse_owners().contains(authenticatedUser)){
            throw new Exception("You are not the owner of course that contains this task");
        }
        tasksRepository.delete(task);
        return true;
    }

    public Tasks updateTask(Tasks task ,Users authenticatedUser) throws Exception{
        Tasks taskToUpdate = tasksRepository.findById(task.getId()).orElseThrow(()->new Exception("Task does not exists"));
        if(!taskToUpdate.getCourse().getCourse_owners().contains(authenticatedUser)){
            throw new Exception("You are not the owner of course that contains this task");
        }
        Files.move(Paths.get(storageProperties.getRootLocation()+File.separator+taskToUpdate.getCourse().getTitle()+File.separator+taskToUpdate.getTitle()),
                Paths.get(storageProperties.getRootLocation()+File.separator+taskToUpdate.getCourse().getTitle()+File.separator+task.getTitle()));

        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setMin_total_files_volume(task.getMin_total_files_volume());
        taskToUpdate.setMax_total_files_volume(task.getMax_total_files_volume());
        taskToUpdate.setDate_of_start(task.getDate_of_start());
        taskToUpdate.setDate_of_end(task.getDate_of_end());
        taskToUpdate.setAvailable_file_extensions(task.getAvailable_file_extensions().toLowerCase().trim());
        tasksRepository.save(taskToUpdate);
        return taskToUpdate;
    }


}
