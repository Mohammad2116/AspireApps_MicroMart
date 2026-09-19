package ir.aspireapps.identifyservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ir.aspireapps.common.utility.jwt.ClaimsData;
import ir.aspireapps.common.utility.jwt.JwtClaimConstants;
import ir.aspireapps.common.utility.jwt.JwtClaimExtractor;
import ir.aspireapps.identifyservice.model.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class JwtService {
    @Getter
    private final long expirationInMS;
    private final SecretKey secretKey;
    private final JwtClaimExtractor jwtClaimExtractor;


    public JwtService(
            @Value("${security.jwt.access-token-secret-key}") String secretKey,
            @Value("${security.jwt.access-token-expiration-ms}") long expirationInMS,
            JwtClaimExtractor jwtClaimExtractor) {
        this.expirationInMS = expirationInMS;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtClaimExtractor = jwtClaimExtractor;
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

    public ClaimsData checkTokenValidity(String accessToken) {
        if(accessToken == null) return null;
        return jwtClaimExtractor.extractClaims(accessToken);
    }
}
