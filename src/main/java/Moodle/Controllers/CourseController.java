package Moodle.Controllers;

import Moodle.Dto.CourseDto;
import Moodle.Model.Courses;
import Moodle.Model.Users;
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

    @PostMapping("/update")
    public Object update(@RequestBody @Valid Courses course, @CurrentSecurityContext(expression = "authentication")
                                            Authentication authentication){
        try {
            return ResponseEntity.ok(courseService.updateCourse(course, controllerService.getAuthenticatedUser(authentication)));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }

    }
    @DeleteMapping("/delete/{id}")
    public Object deltete(@PathVariable int id,@CurrentSecurityContext(expression = "authentication")
                                              Authentication authentication){
        try {
            if(courseService.deleteCourse(id, controllerService.getAuthenticatedUser(authentication))){
                return ResponseEntity.ok("");
            }
            else {
                return new ResponseEntity<>("You dont have authority to do it",HttpStatus.CONFLICT);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.I_AM_A_TEAPOT);
        }
    }
    @PostMapping("/{id}/add/tutor")
    public ResponseEntity<Object> addTutorToCourse(@PathVariable int id, @RequestBody @Valid Users user,@CurrentSecurityContext(expression = "authentication")
    Authentication authentication){
        try {
            if(courseService.addTutorToCourse(id, user, controllerService.getAuthenticatedUser(authentication))){
                return ResponseEntity.ok("user added as owner");
            }
            return new ResponseEntity<>("Unknown error",HttpStatus.I_AM_A_TEAPOT);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }
}
