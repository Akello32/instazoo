package by.matmux.instazoo.payload.request;

import by.matmux.instazoo.annotations.PasswordMatches;
import by.matmux.instazoo.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {
    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;
    @NotEmpty(message = "Firstname cannot be empty")
    private String firstname;
    @NotEmpty(message = "Lastname cannot be empty")
    private String lastname;
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @NotEmpty(message = "Password is required")
    @Size(min = 6)
    private String password;
    private String confirmPassword;

}
