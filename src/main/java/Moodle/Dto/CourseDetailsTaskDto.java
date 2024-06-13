package Moodle.Dto;

import Moodle.Model.Files;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseDetailsTaskDto {
    private Integer id;

    private String title;

    private List<Files> userFiles = new ArrayList<>();

}
