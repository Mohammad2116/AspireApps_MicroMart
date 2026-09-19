package ir.aspireapps.identifyservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Component
public class RefreshTokenGenerator {
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private static final int TOKEN_BYTE_LENGTH = 64;

    private final SecureRandom random = new SecureRandom();
    private final SecretKeySpec secretKeySpec;

    public RefreshTokenGenerator(
            @Value("${security.jwt.refresh-token-secret-key}")
            String secretKey){
        this.secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256_ALGORITHM);
    }

    public String generateRefreshToken() {
        byte[] bytes = new byte[TOKEN_BYTE_LENGTH];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public String hashRefreshToken(String rawRefreshToken) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(secretKeySpec);
            byte[] bytes = mac.doFinal(rawRefreshToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyToken(String rawToken, String hashedToken){
        String computedHashToken =  hashRefreshToken(rawToken);
        return MessageDigest.isEqual(computedHashToken.getBytes(StandardCharsets.UTF_8),
                hashedToken.getBytes(StandardCharsets.UTF_8));
    }
}
