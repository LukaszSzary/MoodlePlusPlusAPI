package Moodle.Controllers;

import Moodle.Services.ControllerService;
import Moodle.Services.FileService;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/save/file")
    public ResponseEntity saveFile(@RequestParam("file") MultipartFile file){
        try {
            fileService.saveFile(file,202);
        }
        catch (Exception e){
            return ResponseEntity.ok(e.toString());
        }

        return ResponseEntity.ok("sd");
    }
}
