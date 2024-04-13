package Moodle.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
@Data
public class TaskDto {
    @NotEmpty
    private String title;
    private String contents;
    private Integer number_of_files;
    private Integer min_total_files_volume;
    private Integer max_total_files_volume;
    @NotNull
    private Date date_of_start;
    private Date date_of_end;
    private String available_file_extensions;
}
