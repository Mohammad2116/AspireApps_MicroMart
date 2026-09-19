package ir.aspireapps.identityservice.controller;

import ir.aspireapps.common.dto.identify.*;
import ir.aspireapps.common.error.DuplicatedEntityException;
import ir.aspireapps.common.error.EntityNotFoundException;
import ir.aspireapps.common.form.UserLoginForm;
import ir.aspireapps.common.form.UserRegisterForm;
import ir.aspireapps.common.utility.cookie.CookieManager;
import ir.aspireapps.common.utility.refreshtoken.ActiveRefreshTokenData;
import ir.aspireapps.identityservice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/ir/aspireapps/micromart/identify/web/v1/auth")
@RequiredArgsConstructor
public class AuthControllerWeb {

    private final AuthService authService;
    private final CookieManager cookieManager;

    // ---------------------------------------------------------
    // Helper method to handle redirect logic for GET pages
    // ---------------------------------------------------------
    private String handleAuthRedirect(HttpServletRequest request) {
        String access = cookieManager.extractToken(request, "ACCESS_TOKEN");
        String refresh = cookieManager.extractToken(request, "REFRESH_TOKEN");

        if (authService.isValidAccessToken(access))
            return "redirect:/ir/aspireapps/micromart/identify/web/v1/user/profile";

        if (authService.isValidRefreshToken(refresh))
            return "redirect:/ir/aspireapps/micromart/identify/web/v1/auth/refresh";

        return null; // show page
    }

    // ---------------------------------------------------------
    // GET Register Page
    // ---------------------------------------------------------
    @GetMapping("/register")
    public String register(Model model, HttpServletRequest request) {
        String redirect = handleAuthRedirect(request);
        if (redirect != null) return redirect;

        model.addAttribute("userRegisterForm", new UserRegisterForm());
        return "register";
    }

    // ---------------------------------------------------------
    // POST Register
    // ---------------------------------------------------------
    @PostMapping("/register")
    public String processRegister(
            @ModelAttribute("userRegisterForm") UserRegisterForm userRegisterForm,
            BindingResult bindingResult,
            Model model,
            HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingError", true);
            return "register";
        }

        if (!userRegisterForm.getPassword().equals(userRegisterForm.getConfirmPassword())) {
            model.addAttribute("passwordMatchError", true);
            return "register";
        }

        AuthResponse authResponse;
        try {
            authResponse = authService.register(
                    UserRegisterRequest.builder()
                            .username(userRegisterForm.getUsername())
                            .email(userRegisterForm.getEmail())
                            .password(userRegisterForm.getPassword())
                            .deviceName(userRegisterForm.getDeviceName())
                            .deviceId(userRegisterForm.getDeviceId())
                            .build()
            );
        } catch (DuplicatedEntityException e) {
            model.addAttribute("userAlreadyExists", true);
            return "register";
        } catch (Exception e) {
            log.error("Unexpected error during registration", e);
            model.addAttribute("internalServerError", true);
            return "register";
        }

        cookieManager.setCookies(response, authResponse);
        return "redirect:/ir/aspireapps/micromart/identify/web/v1/user/profile";
    }

    // ---------------------------------------------------------
    // GET Login Page
    // ---------------------------------------------------------
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        String redirect = handleAuthRedirect(request);
        if (redirect != null) return redirect;

        model.addAttribute("userLoginForm", new UserLoginForm());
        return "login";
    }

    // ---------------------------------------------------------
    // POST Login
    // ---------------------------------------------------------
    @PostMapping("/login")
    public String processLogin(
            @ModelAttribute("userLoginForm") UserLoginForm userLoginForm,
            BindingResult bindingResult,
            Model model,
            HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingError", true);
            return "login";
        }

        AuthResponse authResponse;
        try {
            authResponse = authService.login(
                    UserLoginRequest.builder()
                            .username(userLoginForm.getUsername())
                            .password(userLoginForm.getPassword())
                            .deviceName(userLoginForm.getDeviceName())
                            .deviceId(userLoginForm.getDeviceId())
                            .build()
            );
        } catch (EntityNotFoundException e) {
            model.addAttribute("userNotFound", true);
            return "login";
        } catch (AuthenticationException e) {
            model.addAttribute("authenticationError", true);
            return "login";
        } catch (Exception e) {
            log.error("Unexpected error during login", e);
            model.addAttribute("internalServerError", true);
            return "login";
        }

        cookieManager.setCookies(response, authResponse);
        return "redirect:/ir/aspireapps/micromart/identify/web/v1/user/profile";
    }

    // ---------------------------------------------------------
    // POST Refresh Token (should NOT be GET)
    // ---------------------------------------------------------
    @PostMapping("/refresh")
    public String refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieManager.extractToken(request, "REFRESH_TOKEN");
        if (refreshToken == null)
            return "redirect:/ir/aspireapps/micromart/identify/web/v1/auth/login";

        ActiveRefreshTokenData data = authService.getRefreshTokenDataIfValid(refreshToken);
        if (data == null)
            return "redirect:/ir/aspireapps/micromart/identify/web/v1/auth/login";

        AuthResponse authResponse = authService.refresh(
                UserRefreshRequest.builder()
                        .refreshToken(refreshToken)
                        .deviceName(data.deviceName())
                        .deviceId(data.deviceId())
                        .build()
        );

        cookieManager.setCookies(response, authResponse);
        return "redirect:/ir/aspireapps/micromart/identify/web/v1/user/profile";
    }

    // ---------------------------------------------------------
    // POST Logout
    // ---------------------------------------------------------
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieManager.extractToken(request, "REFRESH_TOKEN");
        if (refreshToken == null)
            return "redirect:/ir/aspireapps/micromart/identify/web/v1/user/profile";

        ActiveRefreshTokenData data = authService.getRefreshTokenDataIfValid(refreshToken);
        if (data == null)
            return "redirect:/ir/aspireapps/micromart/identify/web/v1/user/profile";

        try {
            authService.logout(
                    UserLogoutRequest.builder()
                            .refreshToken(refreshToken)
                            .deviceId(data.deviceId())
                            .deviceName(data.deviceName())
                            .build()
            );
        } catch (RuntimeException e) {
            log.error("Logout failed", e);
        }

        cookieManager.clearCookies(response);
        return "redirect:/ir/aspireapps/micromart/home";
    }

    // ---------------------------------------------------------
    // POST Logout All Devices
    // ---------------------------------------------------------
    @PostMapping("/logout/all")
    public String logoutAll(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieManager.extractToken(request, "REFRESH_TOKEN");
        if (refreshToken == null)
            return "redirect:/ir/aspireapps/micromart/identify/web/v1/user/profile";

        ActiveRefreshTokenData data = authService.getRefreshTokenDataIfValid(refreshToken);
        if (data == null)
            return "redirect:/ir/aspireapps/micromart/identify/web/v1/user/profile";

        try {
            authService.logoutAll(
                    UserLogoutRequest.builder()
                            .refreshToken(refreshToken)
                            .deviceId(data.deviceId())
                            .deviceName(data.deviceName())
                            .build()
            );
        } catch (RuntimeException e) {
            log.error("Logout all failed", e);
        }

        cookieManager.clearCookies(response);
        return "redirect:/ir/aspireapps/micromart/home";
    }
}
