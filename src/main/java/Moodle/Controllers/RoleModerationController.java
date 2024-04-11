package Moodle.Controllers;

import Moodle.Services.JwtService;
import Moodle.Services.RoleModerationService;
import Moodle.Services.UserDetailsServiceImp;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasAuthority('admin')")
public class RoleModerationController {
    private final RoleModerationService service;
    RoleModerationController(RoleModerationService service){
        this.service=service;
    }

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
}
