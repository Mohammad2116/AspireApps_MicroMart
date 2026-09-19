package ir.aspireapps.identityservice.controller;

import ir.aspireapps.common.dto.identify.*;
import ir.aspireapps.identityservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/ir/aspireapps/micromart/identify/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthControllerAPI {

    private final AuthService authService;

    // ---------------------------------------------------------
    // Register
    // ---------------------------------------------------------
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody UserRegisterRequest registerRequest) {

        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---------------------------------------------------------
    // Login
    // ---------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody UserLoginRequest loginRequest) {

        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // Refresh Token
    // ---------------------------------------------------------
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody UserRefreshRequest refreshRequest) {

        AuthResponse response = authService.refresh(refreshRequest);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // Logout (single device)
    // ---------------------------------------------------------
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody UserLogoutRequest logoutRequest) {

        authService.logout(logoutRequest);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------------------------------------
    // Logout All Devices
    // ---------------------------------------------------------
    @PostMapping("/logout/all")
    public ResponseEntity<Void> logoutAll(
            @Valid @RequestBody UserLogoutRequest logoutRequest) {

        authService.logoutAll(logoutRequest);
        return ResponseEntity.noContent().build();
    }
}
