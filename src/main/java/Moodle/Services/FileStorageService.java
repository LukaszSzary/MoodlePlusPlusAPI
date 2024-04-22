package Moodle.Services;

import Moodle.Security.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

@Service
public class FileStorageService {
    private final Path rootPath;
    @Autowired
    public FileStorageService(StorageProperties properties){
        this.rootPath= Paths.get(properties.getRootLocation());
    }

    public void test() throws Exception{

        System.out.println(rootPath);



    }
}
