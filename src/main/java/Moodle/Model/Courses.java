package Moodle.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Courses {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String title;
    @Transient
    @OneToMany(mappedBy = "course", orphanRemoval = true)
    private Set<Tasks> tasks;
    @Transient
    @ManyToMany(mappedBy = "courses_owned")
    private Set<Users> tutors;
    @Transient
    @ManyToMany(mappedBy = "courses_joined")
    private Set<Users> students;

}
