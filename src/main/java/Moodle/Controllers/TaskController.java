package Moodle.Controllers;

import Moodle.Dto.TaskDto;
import Moodle.Services.ControllerService;
import Moodle.Services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
    private final ControllerService controllerService;
    private final TaskService taskService;

    public TaskController(ControllerService controllerService, TaskService taskService) {
        this.controllerService = controllerService;
        this.taskService = taskService;
    }

    @PostMapping("/add/task/to/course/{id}")
    public ResponseEntity<Object> addTask(@PathVariable int id, @RequestBody @Valid TaskDto taskDto){
        try {
            return ResponseEntity.ok(taskService.addTask(taskDto, id));
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}
