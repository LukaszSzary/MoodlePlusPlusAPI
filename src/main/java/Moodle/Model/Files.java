package Moodle.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Files {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private String extension;
    @NotNull
    private Integer volume;
    @NotNull
    private Date date_of_upload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id",nullable = false)
    private Tasks task;
}
