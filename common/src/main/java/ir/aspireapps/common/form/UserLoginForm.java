package ir.aspireapps.common.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginForm {
    @NotEmpty(message = "Username can't be empty")
    @Size(min = 6, max = 255, message = "Username must be 6-255 characters")
    @Pattern(
            regexp = "^[a-zA-Z_]+$",
            message = "Username can only contain English letters and underscores"
    )
    private String username;

    @NotEmpty(message = "Password can't be empty")
    @Size(min = 8, max = 150, message = "Password must be 8-255 characters")
    private String password;

    @NotBlank(message = "Device name is required")
    @Size(max = 512, message = "Device name must not exceed 255 characters")
    private String deviceName;

    @NotNull(message = "Device Id is required as a valued UUID number")
    private UUID deviceId;
}
