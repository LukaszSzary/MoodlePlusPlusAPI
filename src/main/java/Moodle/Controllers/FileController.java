package Moodle.Controllers;

import Moodle.Services.ControllerService;
import Moodle.Services.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

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
    @PreAuthorize("hasAuthority('admin') or hasAuthority('tutor')")
    @GetMapping("/download/course/{id}/files")
    public ResponseEntity<?> downloadCourseFiles(@PathVariable int id, @CurrentSecurityContext(expression = "authentication")
    Authentication authentication) {
        try {
            File zipFile = fileService.downloadCourse(id, controllerService.getAuthenticatedUser(authentication));
            InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + zipFile.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(zipFile.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        }
        catch (Exception e){
            return new ResponseEntity(e.toString(),HttpStatus.NO_CONTENT);
        }
    }
    @PreAuthorize("hasAuthority('admin') or hasAuthority('tutor')")
    @GetMapping("/download/task/{id}/files")
    public ResponseEntity<?> downloadTaskFiles(@PathVariable int id, @CurrentSecurityContext(expression = "authentication")
    Authentication authentication) {
        try {
            File zipFile = fileService.downloadTask(id, controllerService.getAuthenticatedUser(authentication));
            InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + zipFile.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(zipFile.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        }
        catch (Exception e){
            return new ResponseEntity(e.toString(),HttpStatus.NO_CONTENT);
        }
    }
}
