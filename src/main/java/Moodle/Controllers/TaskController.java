package Moodle.Controllers;

import Moodle.Services.ControllerService;
import Moodle.Services.TaskService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
    private final ControllerService controllerService;
    private final TaskService taskService;

    public TaskController(ControllerService controllerService, TaskService taskService) {
        this.controllerService = controllerService;
        this.taskService = taskService;
    }
}
