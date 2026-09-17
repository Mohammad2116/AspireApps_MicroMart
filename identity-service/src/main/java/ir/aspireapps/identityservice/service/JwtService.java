package ir.aspireapps.identityservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ir.aspireapps.common.utility.jwt.JwtClaimConstants;
import ir.aspireapps.identityservice.model.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

public class JwtService {
    @Getter
    private final long expirationInMS;
    private final SecretKey secretKey;

    public JwtService(
            @Value("${security.jwt.access-token-secret-key}")
            String secretKey,
            @Value("$security.jwt.access-token-expiration-ms")
            long expirationInMS) {
        this.expirationInMS = expirationInMS;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.getUsername())
                .claim(JwtClaimConstants.USER_ID, user.getId())
                .claim(JwtClaimConstants.USER_ROLES, user.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationInMS)))
                .signWith(secretKey)
                .compact();
    }
}
