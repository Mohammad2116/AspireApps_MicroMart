package ir.aspireapps.identifyservice.config;


import ir.aspireapps.common.utility.httpheader.HttpHeaderContent;
import ir.aspireapps.common.utility.httpheader.HttpHeaderContextManager;
import ir.aspireapps.common.utility.log.LoggingContextManager;
import ir.aspireapps.common.utility.log.LoggingEvents;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GatewayHeaderAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {

        if(LoggingContextManager.isRequestIdSet(request))
            LoggingContextManager.setCurrentRequestId(response);
        else
            LoggingContextManager.setRandomRequestId(response);
        log.info(LoggingEvents.REQUEST_STARTED);

        HttpHeaderContent headerContent = HttpHeaderContextManager.LoadContent(request);
        if(headerContent.username() != null &&
            !headerContent.username().isEmpty() &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (headerContent.roles() != null &&
                    !headerContent.roles().isEmpty())
                authorities = Arrays.stream(headerContent.roles().split(","))
                        .map(String::trim)
                        .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(headerContent.username(), null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info(LoggingEvents.LOGGING_COMPLETED);
            log.info("Authentication Success with for userID: [{}] username: [{}] roles: [{}]",
                    headerContent.userId(), headerContent.username(), headerContent.roles());
        }
        else {
            log.info(LoggingEvents.LOGGING_NONAVAILABILITY);
            log.info("Request will process without authentication");
        }

        try{
            filterChain.doFilter(request, response);
        } finally {
            log.info(LoggingEvents.REQUEST_COMPLETED);
        }
    }
}
