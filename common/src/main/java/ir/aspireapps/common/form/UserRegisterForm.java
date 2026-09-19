package ir.aspireapps.common.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterForm {
    @NotEmpty(message = "Username can't be empty")
    @Size(min = 6, max = 255, message = "Username must be 6-255 characters")
    @Pattern(
            regexp = "^[a-zA-Z_]+$",
            message = "Username can only contain English letters and underscores"
    )
    private String username;

    @NotEmpty(message = "Password can't be empty")
    @Size(min = 8, max = 150, message = "Password must be 8-255 characters")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,150}$",
            message = "Password must be 8-150 characters and contain at least one letter, one number, and one special character"
    )
    private String password;

    @NotEmpty(message = "Confirm password can't be empty")
    private String confirmPassword;

    @NotBlank(message = "Email is required")
    @Size(max = 254, message = "Email must not exceed 254 characters")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Device name is required")
    @Size(max = 255, message = "Device name must not exceed 255 characters")
    private String deviceName;

    @NotNull(message = "Device Id is required as a valued UUID number")
    private UUID deviceId;
}
