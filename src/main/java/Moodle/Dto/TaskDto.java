package Moodle.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
@Data
public class TaskDto {
    @NotEmpty(message = "The title is required.")
    private String title;
    private String contents;
    private Integer min_total_files_volume;
    @NotNull(message = "Max number of files can't be null")
    private Integer max_total_files_volume;
    @NotNull(message = "The date_of_start is required.")
    private LocalDate date_of_start;
    private LocalDate date_of_end;
    private String available_file_extensions;
}
