package Moodle.Controllers;

import Moodle.Services.UserService;
import Moodle.Model.Users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class UserController {
    private final UserService service;
    UserController(UserService service){
        this.service=service;
    }
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/grant/student/role")
    public ResponseEntity<String> grantStudentRole(@RequestBody String mail ){
        try {
            service.giveStudentRole(mail);
            return ResponseEntity.ok("Role granted.");
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/grant/tutor/role")
    public ResponseEntity<String> grantTutorRole(@RequestBody String mail ){
        try {
            service.giveTutorRole(mail);
            return ResponseEntity.ok("Role granted.");
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/grant/admin/role")
    public ResponseEntity<String> grantAdminRole(@RequestBody String mail){
        try {
            service.giveAdminRole(mail);
            return ResponseEntity.ok("Role granted.");
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/block/user")
    public ResponseEntity<String> blockUser(@RequestBody String mail){
        try {
            service.blockUser(mail);
            return ResponseEntity.ok("User blocked");
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/unblock/user")
    public ResponseEntity<String> unBlockUser(@RequestBody String mail){
        try {
            service.unBlockUser(mail);
            return ResponseEntity.ok("User unblocked");
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/all/users")
    public ResponseEntity<List<Users>> getUsers(){
        return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/get/user/{id}")
    public ResponseEntity<Object> getUser(@PathVariable int id){
        try {
            return ResponseEntity.ok( service.getUser(id));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/delete/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        try {
            service.deleteUser(id);
            return ResponseEntity.ok("User deleted");
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }
}
