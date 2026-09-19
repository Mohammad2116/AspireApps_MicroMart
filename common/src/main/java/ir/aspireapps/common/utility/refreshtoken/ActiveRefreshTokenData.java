package ir.aspireapps.common.utility.refreshtoken;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ActiveRefreshTokenData(
        String deviceName,
        UUID deviceId,
        Instant expirationAt,
        Instant createdAt
) {
}
