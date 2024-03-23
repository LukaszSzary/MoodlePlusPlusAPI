package Moodle.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    private String mail;
    @NotNull
    private String password;

}
