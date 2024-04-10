package Moodle.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id",nullable = false)
    private Courses course;

    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private List<Files> files;
}
