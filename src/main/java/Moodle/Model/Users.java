package Moodle.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
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

    @ManyToMany()
    @JoinTable(
            name="courses_owned",
            joinColumns = @JoinColumn(name ="users_id"),
            inverseJoinColumns = @JoinColumn(name="courses_id")
    )
    private Set<Courses> courses_owned = new HashSet<>();
    @ManyToMany

    @JoinTable(
            name="courses_joined",
            joinColumns = @JoinColumn(name ="users_id"),
            inverseJoinColumns = @JoinColumn(name="courses_id")
    )
    private Set<Courses> courses_joined = new HashSet<>();

    public void addCourseToOwned(Courses courses){
        this.courses_owned.add(courses);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.mail;
    }//Yes, it's dumb, but it is what it is
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
