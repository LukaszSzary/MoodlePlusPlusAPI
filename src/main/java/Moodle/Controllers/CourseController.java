package Moodle.Controllers;

import Moodle.Dto.CourseDto;
import Moodle.Dto.CourseIdTitleDto;
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

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final ControllerService controllerService;

    public CourseController(ControllerService controllerService, CourseService courseService) {
        this.courseService = courseService;
        this.controllerService = controllerService;
    }
    @PreAuthorize("hasAuthority('admin') or hasAuthority('tutor')")
    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody @Valid CourseDto courseDto, @CurrentSecurityContext(expression = "authentication")
                                           Authentication authentication){
        try {
            return ResponseEntity.ok(courseService.addCourse(courseDto, controllerService.getAuthenticatedUser(authentication)));
        }
        catch (Exception e){
            return  new ResponseEntity<>(e.toString(),HttpStatus.FAILED_DEPENDENCY);
        }
    }
    @PreAuthorize("hasAuthority('admin') or hasAuthority('tutor')")
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
    @PreAuthorize("hasAuthority('admin') or hasAuthority('tutor')")
    @DeleteMapping("/delete/{id}")
    public Object delete(@PathVariable int id,@CurrentSecurityContext(expression = "authentication")
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
    @PreAuthorize("hasAuthority('admin') or hasAuthority('tutor')")
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
    @PreAuthorize("hasAuthority('admin') or hasAuthority('tutor')")
    @DeleteMapping("/remove/tutor/{tutorId}/from/course/{courseId}")
    public ResponseEntity<Object> removeTutorFromCourse(@PathVariable int tutorId, @PathVariable int courseId ,@CurrentSecurityContext(expression = "authentication")
    Authentication authentication){
        try {
            if(courseService.removeTutorFromCourse(tutorId, courseId, controllerService.getAuthenticatedUser(authentication))){
                return ResponseEntity.ok("Tutor deleted");
            }
            return new ResponseEntity<>("Unknown error",HttpStatus.I_AM_A_TEAPOT);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('admin') or hasAuthority('tutor')")
    @DeleteMapping("/remove/student/{tutorId}/from/course/{courseId}")
    public ResponseEntity<Object> removeStudentFromCourse(@PathVariable int tutorId, @PathVariable int courseId ,@CurrentSecurityContext(expression = "authentication")
    Authentication authentication){
        try {
            if(courseService.removeStudentFromCourse(tutorId, courseId, controllerService.getAuthenticatedUser(authentication))){
                return ResponseEntity.ok("Student deleted");
            }
            return new ResponseEntity<>("Unknown error",HttpStatus.I_AM_A_TEAPOT);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/add/student")
    public ResponseEntity<Object> addStudentToCourse(@PathVariable int id, @RequestBody @Valid Users user,@CurrentSecurityContext(expression = "authentication")
    Authentication authentication){
        try {
            if(courseService.addStudentToCourse(id, user, controllerService.getAuthenticatedUser(authentication))){
                return ResponseEntity.ok("student added successfully");
            }
            return new ResponseEntity<>("Unknown error",HttpStatus.I_AM_A_TEAPOT);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get/all/courses")
    public ResponseEntity<List<Courses>> getAllCourses(){
        return ResponseEntity.ok(courseService.getAllCourses());
    }
    @GetMapping("/get/course/details/{id}")
    public ResponseEntity<Object> getCourse(@PathVariable int id){
        try {
            return ResponseEntity.ok(courseService.getCourse(id));
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.toString(),HttpStatus.NO_CONTENT);
        }

    }
    @GetMapping("/get/user/courses")
    public ResponseEntity<List<CourseIdTitleDto>> getAllUserCourses(@CurrentSecurityContext(expression = "authentication")
    Authentication authentication){
        return ResponseEntity.ok(courseService.getAllUserCourses(controllerService.getAuthenticatedUser(authentication)));
    }
}
