package Moodle.Controllers;

import Moodle.Dto.UserDto;
import Moodle.Services.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/register")
public class RegisterController {
    private RegisterService service;
    @PostMapping("/newAccount")
    public ResponseEntity<String> registerNewUser(UserDto user){
        try {
            service.addNewUser(user);
            return new ResponseEntity<>("Created", HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }
}
