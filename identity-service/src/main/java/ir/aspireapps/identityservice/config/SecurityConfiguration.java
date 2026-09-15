package ir.aspireapps.identityservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            GatewayHeaderAuthFilter gatewayHeaderAuthFilter) throws Exception {
        log.info("Security Filter Chain at identity-service received a request");
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> {
                            session.sessionCreationPolicy(
                                    SessionCreationPolicy.STATELESS
                            );
                        }
                )
                .authorizeHttpRequests(
                        authorize ->
                                authorize.requestMatchers(
                                        "/ir/aspireapps/micromart/auth/api/v1/register",
                                        "/ir/aspireapps/micromart/auth/api/v1/login",
                                        "/ir/aspireapps/micromart/auth/api/v1/refresh",
                                        "/ir/aspireapps/micromart/auth/web/v1/register",
                                        "/ir/aspireapps/micromart/auth/web/v1/login",
                                        "/ir/aspireapps/micromart/auth/web/v1/refresh"
                                )
                                .permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterAfter(gatewayHeaderAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }
}
