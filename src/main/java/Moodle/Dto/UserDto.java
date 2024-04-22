package Moodle.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {
    @NotEmpty(message = "The name is required.")
    private String name;
    @NotEmpty(message = "The surname is required.")
    private String surname;
    @NotEmpty(message = "The mail is required.")
    private String mail;
    @NotEmpty(message = "The password is required.")
    private String password;

}
