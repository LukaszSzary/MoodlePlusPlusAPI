package Moodle.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {
    @NotNull
    private String mail;
    @NotNull
    private String password;
}
