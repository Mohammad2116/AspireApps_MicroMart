package ir.aspireapps.gatewayservice.config;

import ir.aspireapps.common.utility.log.LoggingContextManager;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class WebAuthenticationFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = String.valueOf(exchange.getRequest().getURI());
        log.info("==================================================");
        log.info("==================================================");
        log.info("Web Authentication Filter called for: {}", path);
        if(LoggingContextManager.isRequestIdSet(exchange))
            LoggingContextManager.setCurrentRequestId(exchange);
        else
            LoggingContextManager.setRandomRequestId(exchange);
        log.info("RequestID set to {}", LoggingContextManager.getRequestId());

        return chain.filter(exchange);

    }
}
