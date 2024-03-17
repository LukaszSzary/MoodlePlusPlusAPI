package Moodle.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Entity
@Data
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String title;
    private String contents;
    private Integer number_of_files;
    private Integer min_total_files_volume;
    private Integer max_total_files_volume;
    @NotNull
    private Date date_of_start;
    private Date date_of_end;
    private String available_file_extensions;
    @Transient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id",nullable = false)
    private Courses course;
    @Transient
    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private Set<Files> files;
}
