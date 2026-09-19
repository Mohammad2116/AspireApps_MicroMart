package ir.aspireapps.gatewayservice.config;


import ir.aspireapps.common.utility.log.LoggingContextManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private final List<String> PUBLIC_PATHS = List.of(
            "/ir/aspireapps/micromart/home",
            "/ir/aspireapps/micromart/identify/api/v1/auth/register",
            "/ir/aspireapps/micromart/identify/api/v1/auth/login",
            "/ir/aspireapps/micromart/identify/api/v1/auth/refresh",
            "/ir/aspireapps/micromart/identify/web/v1/auth/register",
            "/ir/aspireapps/micromart/identify/web/v1/auth/login",
            "/ir/aspireapps/micromart/identify/web/v1/auth/refresh"
            );
    private final List<String> WEB_PATHS = List.of(
            "/ir/aspireapps/micromart/home",
            "/ir/aspireapps/micromart/identify/web/v1/auth/register",
            "/ir/aspireapps/micromart/identify/web/v1/auth/login",
            "/ir/aspireapps/micromart/identify/web/v1/auth/refresh",

            "/ir/aspireapps/micromart/identify/web/v1/auth/profile"
            );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = String.valueOf(exchange.getRequest().getURI());
        log.info("==================================================");
        log.info("==================================================");
        log.info("Jwt Authentication Filter called for: {}", path);
        if(LoggingContextManager.isRequestIdSet(exchange))
            LoggingContextManager.setCurrentRequestId(exchange);
        else
            LoggingContextManager.setRandomRequestId(exchange);
        log.info("RequestID set to {}", LoggingContextManager.getRequestId());

        if(isWebPath(path)){
            return processWebRequests(exchange, chain, path);
        }
        else {
            return processApiRequests(exchange, chain, path);
        }
    }

    private Mono<Void> processApiRequests(ServerWebExchange exchange, GatewayFilterChain chain, String path) {
        log.info("Request for Api detected");
        if(isPublicPath(path)){
            return chain.filter(exchange);
        }
        return null;
    }

    private Mono<Void> processWebRequests(ServerWebExchange exchange, GatewayFilterChain chain, String path) {
        log.info("Request for Web detected");
        if(isPublicPath(path)){
            return chain.filter(exchange);
        }
        return null;
    }


    private  boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private boolean isWebPath(String path) {
        return WEB_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
