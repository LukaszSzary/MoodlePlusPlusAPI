package Moodle.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class sda {
    @GetMapping("/test")
    public String cos(){
        return "dasdadada";
    }
}
