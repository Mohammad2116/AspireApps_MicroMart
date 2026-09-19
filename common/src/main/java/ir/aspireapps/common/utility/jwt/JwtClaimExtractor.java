package ir.aspireapps.common.utility.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtClaimExtractor {
    private final SecretKey secretKey;
    public JwtClaimExtractor(
            @Value("${security.jwt.access-token-secret-key}") String secretKey
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public ClaimsData extractClaims(String token) {
        Claims claims = null;
        claims = Jwts
                .parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        if(claims != null) {
            return ClaimsData.builder()
                    .subject(claims.getSubject())
                    .userId(claims.get(JwtClaimConstants.USER_ID, String.class))
                    .roles(claims.get(JwtClaimConstants.USER_ROLES, String.class))
                    .expiresAt(claims.getExpiration().toInstant())
                    .issuedAt(claims.getIssuedAt().toInstant())
                    .build();
        }
        return null;
    }
}
