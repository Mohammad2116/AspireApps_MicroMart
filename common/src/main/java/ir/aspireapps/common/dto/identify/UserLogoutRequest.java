package ir.aspireapps.common.dto.identify;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserLogoutRequest(
        @NotBlank(message = "Refresh token is required")
        @Size(min = 86, max = 86, message = "Refresh token must be exactly 86 characters")
        String refreshToken,

        @NotBlank(message = "Device name is required")
        @Size(max = 255, message = "Device name must not exceed 255 characters")
        String deviceName,

        @NotNull(message = "Device Id is required as a valued UUID number")
        UUID deviceId
) {
}
