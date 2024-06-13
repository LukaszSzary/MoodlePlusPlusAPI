package Moodle.Dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseDetailsDto{
    private Integer id;

    private String title;
    private List<CourseDetailsUserDto> users = new ArrayList<>();
}
