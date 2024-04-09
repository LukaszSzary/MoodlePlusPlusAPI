package Moodle.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseDto {
    @NotEmpty(message = "title can't be empty")
    private String title;
}
