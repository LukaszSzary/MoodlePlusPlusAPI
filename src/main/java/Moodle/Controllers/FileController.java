package Moodle.Controllers;

import Moodle.Services.ControllerService;
import Moodle.Services.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {
    private final FileStorageService fileService;
    private final ControllerService controllerService;

    public FileController(FileStorageService fileService, ControllerService controllerService) {
        this.fileService = fileService;
        this.controllerService = controllerService;
    }

    @GetMapping("/files")
    public ResponseEntity files(){
        try {
            fileService.test();
        }
        catch (Exception e){
            return ResponseEntity.ok(e.toString());
        }

        return ResponseEntity.ok("sd");
    }
}
