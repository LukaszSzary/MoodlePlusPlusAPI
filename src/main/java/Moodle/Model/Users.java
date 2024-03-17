package Moodle.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;


@Entity
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    private String mail;
    @NotNull
    private String password;
    @NotNull
    private Role role;
    @Transient
    @ManyToMany
    @JoinTable(
            name="courses_owned",
            joinColumns = @JoinColumn(name ="owned_courses_id"),
            inverseJoinColumns = @JoinColumn(name="tutor_id")
    )
    private Set<Courses> courses_owned;
    @Transient
    @ManyToMany
    @JoinTable(
            name="courses_joined",
            joinColumns = @JoinColumn(name ="joined_courses_id"),
            inverseJoinColumns = @JoinColumn(name="student_id")
    )
    private Set<Courses> courses_joined;
}
