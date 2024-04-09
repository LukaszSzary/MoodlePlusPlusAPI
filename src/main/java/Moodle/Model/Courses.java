package Moodle.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
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
    private Set<Tasks> tasks = new HashSet<>();


    @ManyToMany(mappedBy = "courses_owned")
    private Set<Users> tutors = new HashSet<Users>();

    @ManyToMany(mappedBy = "courses_joined")
    private Set<Users> students = new HashSet<>();
    public void addTutor(Users user){
        tutors.add(user);
    }
}
