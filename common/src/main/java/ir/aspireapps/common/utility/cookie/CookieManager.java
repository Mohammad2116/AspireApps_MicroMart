package ir.aspireapps.common.utility.cookie;

import ir.aspireapps.common.dto.identify.AuthResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;

@Component
public class CookieManager {

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";

    private final boolean cookieSecure;
    private final boolean httpOnly;

    public CookieManager(@Value("${cookie.cookie_secure}") boolean cookieSecure,
                         @Value("${cookie.http_only}") boolean httpOnly) {
        this.cookieSecure = cookieSecure;
        this.httpOnly = httpOnly;
    }

    // ---------------------------------------------------------
    // Set both access + refresh cookies
    // ---------------------------------------------------------
    public void setCookies(HttpServletResponse response, AuthResponse authResponse) {
        addTokenCookie(
                response,
                ACCESS_TOKEN,
                authResponse.accessToken(),
                Duration.ofMillis(authResponse.accessExpiresIn()) // convert MS → Duration
        );

        addTokenCookie(
                response,
                REFRESH_TOKEN,
                authResponse.refreshToken(),
                Duration.ofMillis(authResponse.refreshExpiresIn())
        );
    }

    // ---------------------------------------------------------
    // Clear both cookies
    // ---------------------------------------------------------
    public void clearCookies(HttpServletResponse response) {
        clearCookie(response, ACCESS_TOKEN);
        clearCookie(response, REFRESH_TOKEN);
    }

    private void clearCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(httpOnly)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    // ---------------------------------------------------------
    // Add cookie with correct expiration + flags
    // ---------------------------------------------------------
    private void addTokenCookie(HttpServletResponse response,
                                String tokenName,
                                String tokenValue,
                                Duration duration) {

        ResponseCookie cookie = ResponseCookie.from(tokenName, tokenValue)
                .httpOnly(httpOnly)
                .secure(cookieSecure)
                .sameSite("Lax") // safe for same-site web apps
                .path("/")
                .maxAge(duration.getSeconds()) // FIXED: seconds, not milliseconds
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    // ---------------------------------------------------------
    // Extract cookie value
    // ---------------------------------------------------------
    public String extractToken(HttpServletRequest request, String tokenName) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(tokenName))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
