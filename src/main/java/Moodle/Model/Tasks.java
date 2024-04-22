package Moodle.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Entity
@Data
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotEmpty
    private String title;
    private String contents;
    private Integer min_total_files_volume;
    @NotNull
    private Integer max_total_files_volume;
    @NotNull
    private LocalDate date_of_start;
    private LocalDate date_of_end;
    private String available_file_extensions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "course_id",nullable = false)
    private Courses course;

    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private List<Files> files = new ArrayList<>();
}
