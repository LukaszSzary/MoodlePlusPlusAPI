package Moodle.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Courses {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @NotNull
    private String title;

    @OneToMany(mappedBy = "course", orphanRemoval = true)
    private List<Tasks> tasks;




    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name="course_owners",
            joinColumns = @JoinColumn(name ="courses_id"),
            inverseJoinColumns = @JoinColumn(name="users_id")
    )

    private List<Users> course_owners;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name="course_students",
            joinColumns = @JoinColumn(name ="courses_id"),
            inverseJoinColumns = @JoinColumn(name="users_id")
    )
    private List<Users> course_students;
}
