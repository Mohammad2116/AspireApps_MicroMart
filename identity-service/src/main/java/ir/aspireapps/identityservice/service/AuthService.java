package ir.aspireapps.identityservice.service;

import ir.aspireapps.common.dto.identify.*;
import ir.aspireapps.common.error.AuthenticationFailedException;
import ir.aspireapps.common.error.DuplicatedEntityException;
import ir.aspireapps.common.error.EntityNotFoundException;
import ir.aspireapps.identityservice.mapper.UserMapper;
import ir.aspireapps.identityservice.model.User;
import ir.aspireapps.identityservice.repo.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse register(@Valid UserRegisterRequest registerRequest) {
        if(userRepository.existsByUsernameOrEmail(registerRequest.username(), registerRequest.email()))
            throw new DuplicatedEntityException("Username:[" + registerRequest.username() +
                    "] or email[" + registerRequest.email() + "] already exists.");
        User user = userMapper.toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        return AuthResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .issuedAt(Instant.now())
                .accessToken(jwtService.generateAccessToken(user))
                .accessExpiresIn(jwtService.getExpirationInMS())
                .refreshToken(refreshTokenService.generateRefreshToken(user, registerRequest.deviceName(), registerRequest.deviceId()))
                .refreshExpiresIn(refreshTokenService.getExpirationInMS())
                .build();
    }

    public AuthResponse login(@Valid UserLoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new EntityNotFoundException("User [" + loginRequest.username() + "] not exists"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new AuthenticationFailedException("Wrong password");
        }
        return AuthResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .issuedAt(Instant.now())
                .accessToken(jwtService.generateAccessToken(user))
                .accessExpiresIn(jwtService.getExpirationInMS())
                .refreshToken(refreshTokenService.generateRefreshToken(user, loginRequest.deviceName(), loginRequest.deviceId()))
                .refreshExpiresIn(refreshTokenService.getExpirationInMS())
                .build();
    }

    public AuthResponse refresh(@Valid UserRefreshRequest userRefreshRequest) {
        User user = refreshTokenService.getTokenUser(userRefreshRequest.refreshToken());

        if(refreshTokenService.verifyAndRevoke(userRefreshRequest.refreshToken(),
                                                userRefreshRequest.deviceName(),
                                                userRefreshRequest.deviceId())) {
            return AuthResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .issuedAt(Instant.now())
                    .accessToken(jwtService.generateAccessToken(user))
                    .accessExpiresIn(jwtService.getExpirationInMS())
                    .refreshToken(refreshTokenService.generateRefreshToken(user, userRefreshRequest.deviceName(), userRefreshRequest.deviceId()))
                    .refreshExpiresIn(refreshTokenService.getExpirationInMS())
                    .build();
        }
        throw new AuthenticationFailedException("Something went wrong while refreshing");
    }

    public void logout(@Valid UserLogoutRequest userLogoutRequest) {
        User user = refreshTokenService.getTokenUser(userLogoutRequest.refreshToken());

        if(refreshTokenService.verifyAndRevoke(userLogoutRequest.refreshToken(), userLogoutRequest.deviceName(), userLogoutRequest.deviceId()))
            return;
        throw new AuthenticationFailedException("Something went wrong while refreshing");
    }

    public void logoutAll(@Valid UserLogoutRequest userLogoutRequest) {
        User user = refreshTokenService.getTokenUser(userLogoutRequest.refreshToken());

        if(refreshTokenService.verifyAndRevokeAll(userLogoutRequest.refreshToken(), userLogoutRequest.deviceName(), userLogoutRequest.deviceId()))
            return;
        throw new AuthenticationFailedException("Something went wrong while refreshing");
    }
}
