package Moodle.Controllers;

import Moodle.Services.ControllerService;
import Moodle.Services.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {
    private final FileService fileService;
    private final ControllerService controllerService;

    public FileController(FileService fileService, ControllerService controllerService) {
        this.fileService = fileService;
        this.controllerService = controllerService;
    }

    @PostMapping("/save/file/to/task/{id} ")
    public ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile file, @PathVariable int id,@CurrentSecurityContext(expression = "authentication")
                                    Authentication authentication) {
        try {
            return ResponseEntity.ok(fileService.saveFile(file,id, controllerService.getAuthenticatedUser(authentication)));
       }
       catch (Exception e){
           return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
       }
    }
}
