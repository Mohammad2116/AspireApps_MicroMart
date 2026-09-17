package ir.aspireapps.identityservice.service;

import ir.aspireapps.identityservice.model.RefreshToken;
import ir.aspireapps.identityservice.model.User;
import ir.aspireapps.identityservice.repo.RefreshTokenRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class RefreshTokenService {
    private final long expirationMS;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(
            @Value("${security.jwt.refresh-token-expiration-ms}")
            long expirationMS,
            RefreshTokenGenerator refreshTokenGenerator,
            RefreshTokenRepository refreshTokenRepository){
        this.expirationMS = expirationMS;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateRefreshToken(
            @NotNull(message = "Can't generate refresh token without User information")
            User user,
            @NotBlank(message = "Device name is required")
            @Size(max = 512, message = "Email must not exceed 512 characters")
            String deviceName,
            @NotNull(message = "Device Id is required as a valued UUID number")
            UUID deviceId) {

        String token = refreshTokenGenerator.generateRefreshToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .hashedToken(refreshTokenGenerator.hashRefreshToken(token))
                .expirationAt(Instant.now().plusMillis(expirationMS))
                .deviceName(deviceName)
                .deviceId(deviceId)
                .build();
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);
        user.addRefreshTokens(savedRefreshToken);

        return token;
    }

    public long getExpirationInMS() {
        return expirationMS;
    }
}
