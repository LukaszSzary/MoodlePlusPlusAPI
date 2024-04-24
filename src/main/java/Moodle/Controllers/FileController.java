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

    @PostMapping("/save/file/to/task/{id}")
    public ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile file, @PathVariable int id,@CurrentSecurityContext(expression = "authentication")
                                    Authentication authentication) {
        try {
            return ResponseEntity.ok(fileService.saveFile(file,id, controllerService.getAuthenticatedUser(authentication)));
       }
       catch (Exception e){
           return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
       }
    }
    @GetMapping("/get/users/files/from/task/{id}")
    public ResponseEntity<?> getFiles(@PathVariable int id, @CurrentSecurityContext(expression = "authentication")
                                        Authentication authentication){
        try{
            return ResponseEntity.ok(fileService.getUserFilesSubmittedForTask(id,controllerService.getAuthenticatedUser(authentication)));
        }
        catch (Exception e ){
            return new ResponseEntity<String>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/file/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable int id, @CurrentSecurityContext(expression = "authentication")
    Authentication authentication){
        try{
            if(fileService.deleteFile(id,controllerService.getAuthenticatedUser(authentication))){
                return ResponseEntity.ok("Deleted");
            }
            return new ResponseEntity<>("Unknown error",HttpStatus.I_AM_A_TEAPOT);
        }
        catch (Exception e ){
            return new ResponseEntity<String>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }
}
