package ir.aspireapps.common.dto.identify;

import ir.aspireapps.common.enums.Role;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String username,
        String email,
        Role role,
        Instant createdAt,
        Instant updatedAt
) {
}
