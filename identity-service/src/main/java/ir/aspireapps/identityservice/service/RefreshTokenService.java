package ir.aspireapps.identityservice.service;

import ir.aspireapps.common.error.AuthenticationFailedException;
import ir.aspireapps.common.utility.refreshtoken.ActiveRefreshTokenData;
import ir.aspireapps.identityservice.model.RefreshToken;
import ir.aspireapps.identityservice.model.User;
import ir.aspireapps.identityservice.repo.RefreshTokenRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
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

    @Transactional
    public String generateRefreshToken(User user, String deviceName, UUID deviceId) {
        String rawToken = refreshTokenGenerator.generateRefreshToken();
        String hashToken = refreshTokenGenerator.hashRefreshToken(rawToken);
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .hashedToken(hashToken)
                .expirationAt(Instant.now().plusMillis(expirationMS))
                .deviceName(deviceName)
                .deviceId(deviceId)
                .build();
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);
        user.addRefreshTokens(savedRefreshToken);

        return rawToken;
    }

    public long getExpirationInMS() {
        return expirationMS;
    }

    @Transactional
    public boolean verifyAndRevoke(@NotNull String refreshToken, @NotBlank String deviceName, @NotBlank UUID deviceId) {
        String hashedRefreshToken = refreshTokenGenerator.hashRefreshToken(refreshToken);
        RefreshToken foundRefreshToken = refreshTokenRepository.findByHashTokenAndRevokedFalse(hashedRefreshToken)
                .orElseThrow(() -> new AuthenticationFailedException("Refresh token not found or revoked"));
        if(foundRefreshToken.getExpirationAt().isBefore(Instant.now())) {
            throw new AuthenticationFailedException("Refresh token expired");
        }
        if(foundRefreshToken.getDeviceName().equals(deviceName) &&
                foundRefreshToken.getDeviceId().equals(deviceId)) {
            foundRefreshToken.revoke();
            return true;
        } else {
            foundRefreshToken.revoke();
            throw new AuthenticationFailedException("Illegal or stolen refresh token");
        }
    }

    @Transactional
    public User getTokenUser(@NotNull String refreshToken) {
        String hashedRefreshToken = refreshTokenGenerator.hashRefreshToken(refreshToken);
        RefreshToken foundRefreshToken = refreshTokenRepository.findByHashTokenAndRevokedFalse(hashedRefreshToken)
                .orElseThrow(() -> new AuthenticationFailedException("Refresh token not found or revoked"));
        return foundRefreshToken.getUser();
    }

    public boolean verifyAndRevokeAll(
            @NotEmpty(message = "Password can't be empty")
            @Size(min = 86, max = 86, message = "Refresh token must be exact 86 characters lenght")
            String refreshToken,
            @NotBlank(message = "Device name is required")
            @Size(max = 255, message = "Device name must not exceed 255 characters")
            String deviceName,
            @NotNull(message = "Device Id is required as a valued UUID number")
            UUID deviceId) {
        String hashedRefreshToken = refreshTokenGenerator.hashRefreshToken(refreshToken);
        RefreshToken foundRefreshToken = refreshTokenRepository.findByHashTokenAndRevokedFalse(hashedRefreshToken)
                .orElseThrow(() -> new AuthenticationFailedException("Refresh token not found or revoked"));

        if(foundRefreshToken.getExpirationAt().isBefore(Instant.now())) {
            throw new AuthenticationFailedException("Refresh token expired");
        }
        if(foundRefreshToken.getDeviceName().equals(deviceName) &&
                foundRefreshToken.getDeviceId().equals(deviceId)) {
            List<RefreshToken> tokens = refreshTokenRepository.findByUserAndRevokedFalse(foundRefreshToken.getUser());
            tokens.forEach(RefreshToken::revoke);
            return true;
        } else {
            foundRefreshToken.revoke();
            throw new AuthenticationFailedException("Illegal or stolen refresh token");
        }
    }

    public Optional<RefreshToken> getUserTokenForDeviceId(
            User user,
            @NotNull(message = "Device Id is required as a valued UUID number")
            UUID deviceId) {
        return refreshTokenRepository.findByUserAndDeviceIdAndRevokedFalse(user, deviceId);
    }

    public boolean checkTokenValidity(String refreshToken) {
        String hashedRefreshToken = refreshTokenGenerator.hashRefreshToken(refreshToken);
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByHashTokenAndRevokedFalse(hashedRefreshToken)
                .orElse(null);
        if(refreshTokenEntity == null) return false;
        return !refreshTokenEntity.getExpirationAt().isBefore(Instant.now());
    }

    public ActiveRefreshTokenData getActiveRefreshTokenDataIfValid(String refreshToken) {
        String hashedRefreshToken = refreshTokenGenerator.hashRefreshToken(refreshToken);
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByHashTokenAndRevokedFalse(hashedRefreshToken)
                .orElse(null);
        if(refreshTokenEntity == null) return null;
        if(refreshTokenEntity.getExpirationAt().isBefore(Instant.now())) {return null;}
        return ActiveRefreshTokenData.builder()
                .deviceName(refreshTokenEntity.getDeviceName())
                .deviceId(refreshTokenEntity.getDeviceId())
                .expirationAt(refreshTokenEntity.getExpirationAt())
                .createdAt(refreshTokenEntity.getCreatedAt())
                .build();
    }
}
