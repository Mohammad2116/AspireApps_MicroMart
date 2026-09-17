package ir.aspireapps.common.dto.identify;

import ir.aspireapps.common.enums.Role;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record AuthResponse(
        String accessToken,
        String refreshToken,

        String tokenType,
        long accessExpiresIn,
        long refreshExpiresIn,
        Instant issuedAt,

        UUID id,
        String username,
        String email,
        Role role
) {
}
