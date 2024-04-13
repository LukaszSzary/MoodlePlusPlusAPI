package Moodle.Services;

import Moodle.Repositories.TasksRepository;
import Moodle.Repositories.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TasksRepository tasksRepository;
    private final UsersRepository usersRepository;

    public TaskService(TasksRepository tasksRepository, UsersRepository usersRepository) {
        this.tasksRepository = tasksRepository;
        this.usersRepository = usersRepository;
    }
}
