package ir.aspireapps.common.utility.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.server.ServerWebExchange;

import java.util.UUID;

@Slf4j
public final class LoggingContextManager {
    private LoggingContextManager() {}

    public static void putRequestId(String requestId) {
        MDC.put(LoggingConstants.REQUEST_ID, requestId);
    }
    public static void putUserId(String userId) {
        MDC.put(LoggingConstants.USER_ID, userId);
    }
    public static void putUserName(String userName) {
        MDC.put(LoggingConstants.USER_NAME, userName);
    }

    public static String getRequestId() {
        return MDC.get(LoggingConstants.REQUEST_ID);
    }
    public static String getUserId() {
        return MDC.get(LoggingConstants.USER_ID);
    }
    public static String getUserName() {
        return MDC.get(LoggingConstants.USER_NAME);
    }

    public static void clear() {
        MDC.clear();
    }

    public static boolean isRequestIdSet(ServerWebExchange exchange){
        return exchange.getRequest().getHeaders().containsKey(LoggingConstants.REQUEST_ID_HEADER);
    }

    public static void setRandomRequestId(ServerWebExchange exchange){
        clear();
        String randomRequestId = UUID.randomUUID().toString();
        putRequestId(randomRequestId);
        exchange.getRequest().getHeaders().add(LoggingConstants.REQUEST_ID_HEADER, randomRequestId);
        exchange.getResponse().getHeaders().add(LoggingConstants.REQUEST_ID_HEADER, randomRequestId);
    }
    public static void setCurrentRequestId(ServerWebExchange exchange){
        String requestId = MDC.get(LoggingConstants.REQUEST_ID);
        exchange.getRequest().getHeaders().add(LoggingConstants.REQUEST_ID_HEADER, requestId);
        exchange.getResponse().getHeaders().add(LoggingConstants.REQUEST_ID_HEADER, requestId);
    }
}
