package ir.aspireapps.common.dto.identify;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record UserRegisterRequest(
    @NotEmpty(message = "Username can't be empty")
    @Size(min = 6, max = 255, message = "Username must be 6-255 characters")
    @Pattern(
            regexp = "^[a-zA-Z_]+$",
            message = "Username can only contain English letters and underscores"
    )
    String username,

    @NotEmpty
    @Size(min = 8, max = 150, message = "Password must be 6-255 characters")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,150}$",
            message = "Password must be 8-150 characters and contain at least one letter, one number, and one special character"
    )
    String password,

    @NotBlank(message = "Email is required")
    @Size(max = 254, message = "Email must not exceed 254 characters")
    @Email(message = "Please provide a valid email address")
    String email
) {
}
