package Moodle.Controllers;

import Moodle.Dto.LoginDto;
import Moodle.Dto.UserDto;
import Moodle.Services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationService service;
    public AuthenticationController(AuthenticationService service){
        this.service=service;
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@Valid @RequestBody  UserDto user){
        try {
            service.registerUser(user);
            return new ResponseEntity<>("Created", HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody  LoginDto loginDto){
        return ResponseEntity.ok(service.loginUser(loginDto));
    }
}
