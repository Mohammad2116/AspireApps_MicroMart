package ir.aspireapps.common.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRefreshForm {
    @NotBlank(message = "Device name is required")
    @Size(max = 255, message = "Device name must not exceed 255 characters")
    private String deviceName;

    @NotNull(message = "Device Id is required as a valued UUID number")
    private UUID deviceId;
}
