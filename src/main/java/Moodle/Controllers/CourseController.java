package Moodle.Controllers;

import Moodle.Dto.CourseDto;
import Moodle.Model.Courses;
import Moodle.Services.ControllerService;
import Moodle.Services.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasAuthority('admin') or hasAuthority('tutor')")
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final ControllerService controllerService;

    public CourseController(ControllerService controllerService, CourseService courseService) {
        this.courseService = courseService;
        this.controllerService = controllerService;
    }

    @PostMapping("/add")
    public ResponseEntity<Courses> add(@RequestBody @Valid CourseDto courseDto, @CurrentSecurityContext(expression = "authentication")
                                           Authentication authentication){
        return ResponseEntity.ok(courseService.addCourse(courseDto,controllerService.getAuthenticatedUser(authentication)));
    }

    @PostMapping("/update/{id}")
    public Object update(@PathVariable int id,@RequestBody @Valid CourseDto courseDto, @CurrentSecurityContext(expression = "authentication")
                                            Authentication authentication){
        try {
            return ResponseEntity.ok(courseService.updateCourse(id, courseDto, controllerService.getAuthenticatedUser(authentication)));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }

    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deltete(){
        return ResponseEntity.ok("");
    }
}
