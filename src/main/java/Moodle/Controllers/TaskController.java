package Moodle.Controllers;

import Moodle.Dto.TaskDto;
import Moodle.Model.Tasks;
import Moodle.Services.ControllerService;
import Moodle.Services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {
    private final ControllerService controllerService;
    private final TaskService taskService;

    public TaskController(ControllerService controllerService, TaskService taskService) {
        this.controllerService = controllerService;
        this.taskService = taskService;
    }
    @PreAuthorize("hasAuthority('admin') or hasAuthority('tutor')")
    @PostMapping("/add/task/to/course/{id}")
    public ResponseEntity<Object> addTask(@PathVariable int id, @RequestBody @Valid TaskDto taskDto){
        try {
            return ResponseEntity.ok(taskService.addTask(taskDto, id));
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get/task/{id}")
    public ResponseEntity<Object> getTaskById(@PathVariable int id){
        try {
            return ResponseEntity.ok(taskService.getTaskById(id));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/tasks/from/course/{id}")
    public ResponseEntity<Object> getTasksFromCourse(@PathVariable int id){
        try {
            return ResponseEntity.ok(taskService.getTasksFromCourse(id));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('admin') or hasAuthority('tutor')")
    @PostMapping("/update/task")
    public ResponseEntity<Object> updateTask(@RequestBody @Valid Tasks task,@CurrentSecurityContext(expression = "authentication")
                                            Authentication authentication){
        try{
            return ResponseEntity.ok(taskService.updateTask(task,controllerService.getAuthenticatedUser(authentication)));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('admin') or hasAuthority('tutor')")
    @DeleteMapping("/delete/task/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id, @CurrentSecurityContext(expression = "authentication")
                                                Authentication authentication){
        try{
            if(taskService.deleteTask(id,controllerService.getAuthenticatedUser(authentication))) {
             return ResponseEntity.ok("Deleted");
            }
            else{
                return new ResponseEntity<>("Unknown error",HttpStatus.FAILED_DEPENDENCY);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }
}
