package Moodle.Dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseDetailsUserDto {
    private Integer id;
    private String name;
    private String surname;
    private String mail;
    private List<CourseDetailsTaskDto> tasks = new ArrayList<>();
}
