package Moodle.Controllers;

import Moodle.Services.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAuthority('admin') or hasAuthority('tutor')")
@RequestMapping("/course")
public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(){
        return ResponseEntity.ok("");
    }
    @PostMapping("/update")
    public ResponseEntity<String> update(){
        return ResponseEntity.ok("");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deltete(){
        return ResponseEntity.ok("");
    }
}
