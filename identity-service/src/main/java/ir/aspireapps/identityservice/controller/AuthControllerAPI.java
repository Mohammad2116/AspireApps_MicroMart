package ir.aspireapps.identityservice.controller;

import ir.aspireapps.common.dto.identify.AuthResponse;
import ir.aspireapps.common.dto.identify.UserLoginRequest;
import ir.aspireapps.common.dto.identify.UserRegisterRequest;
import ir.aspireapps.common.dto.identify.UserResponse;
import ir.aspireapps.identityservice.service.AuthService;
import jakarta.servlet.ServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/ir/aspireapps/micromart/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerAPI {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody UserRegisterRequest registerRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    authService.register(registerRequest)
                    );
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody UserLoginRequest loginRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        authService.login(loginRequest)
                );
    }

    @GetMapping("/refresh")
    public void refresh(){

    }

    @PostMapping("/logout")
    public void logout(){

    }

    @PostMapping("/logout/all")
    public void logoutAll(){

    }
}
