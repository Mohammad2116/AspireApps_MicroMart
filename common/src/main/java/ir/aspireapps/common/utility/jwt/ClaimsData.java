package ir.aspireapps.common.utility.jwt;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ClaimsData(
        String subject,
        String userId,
        String roles,
        Instant issuedAt,
        Instant expiresAt
) {
}
