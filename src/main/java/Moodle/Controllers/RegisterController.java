package Moodle.Controllers;

import Moodle.Dto.UserDto;
import Moodle.Services.RegisterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/register")
public class RegisterController {
    private final RegisterService service;
    public RegisterController(RegisterService service){
        this.service=service;
    }
    @PostMapping("/newAccount")
    public ResponseEntity<String> registerNewUser(@RequestBody @Valid UserDto user){
        try {
            service.addNewUser(user);
            return new ResponseEntity<>("Created", HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }
}
