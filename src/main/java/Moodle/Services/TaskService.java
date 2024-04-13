package Moodle.Services;

import Moodle.Dto.TaskDto;
import Moodle.Model.Courses;
import Moodle.Model.Tasks;
import Moodle.Model.Users;
import Moodle.Repositories.CoursesRepository;
import Moodle.Repositories.TasksRepository;
import Moodle.Repositories.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TasksRepository tasksRepository;
    private final UsersRepository usersRepository;
    private final CoursesRepository coursesRepository;

    public TaskService(TasksRepository tasksRepository, UsersRepository usersRepository, CoursesRepository coursesRepository) {
        this.tasksRepository = tasksRepository;
        this.usersRepository = usersRepository;
        this.coursesRepository = coursesRepository;
    }

    public Tasks addTask(TaskDto taskDto, int courseId) throws  Exception{
        Courses course = coursesRepository.findById(courseId).orElseThrow(()-> new Exception("Course with this id does not exists"));
        Tasks task = new Tasks();
        task.setTitle(taskDto.getTitle());
        task.setContents(taskDto.getContents());
        task.setNumber_of_files(taskDto.getNumber_of_files());
        task.setMin_total_files_volume(taskDto.getMin_total_files_volume());
        task.setMax_total_files_volume(taskDto.getMax_total_files_volume());
        task.setDate_of_start(taskDto.getDate_of_start());
        task.setDate_of_end(taskDto.getDate_of_end());
        task.setAvailable_file_extensions(taskDto.getAvailable_file_extensions());
        task.setCourse(course);
        tasksRepository.save(task);
        return task;
    }
}
