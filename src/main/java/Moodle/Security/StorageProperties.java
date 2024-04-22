package Moodle.Security;

import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class StorageProperties {
    private final String rootLocation ;
    public StorageProperties(){
        rootLocation = System.getProperty("user.dir") + File.separator+ "FileStorage";
    }
    public String getRootLocation(){
        return this.rootLocation;
    }
}
