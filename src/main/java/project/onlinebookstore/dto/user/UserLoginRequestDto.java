package project.onlinebookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import project.onlinebookstore.validation.Password;

@Data
public class UserLoginRequestDto {
    @Email
    @NotBlank(message = "Field email can't be null")
    private String email;
    @Length(min = 8, max = 25)
    @Password
    private String password;
}
