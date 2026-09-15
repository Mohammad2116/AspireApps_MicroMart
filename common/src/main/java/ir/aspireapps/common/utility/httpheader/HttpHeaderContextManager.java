package ir.aspireapps.common.utility.httpheader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.http.HttpRequest;

public final class HttpHeaderContextManager {
    private HttpHeaderContextManager(){}

    public static HttpHeaderContent LoadContent(HttpServletRequest httpRequest){
        return HttpHeaderContent.builder()
                .userId(httpRequest.getHeader(HttpHeaderConstants.X_USERID))
                .username(httpRequest.getHeader(HttpHeaderConstants.X_USERNAME))
                .roles(httpRequest.getHeader(HttpHeaderConstants.X_ROLES))
                .build();
    }

    public static HttpHeaderContent LoadContent(HttpServletResponse httpResponse){
        return HttpHeaderContent.builder()
                .userId(httpResponse.getHeader(HttpHeaderConstants.X_USERID))
                .username(httpResponse.getHeader(HttpHeaderConstants.X_USERNAME))
                .roles(httpResponse.getHeader(HttpHeaderConstants.X_ROLES))
                .build();
    }

    public static void setContent(HttpServletResponse httpResponse, HttpHeaderContent content){
        httpResponse.setHeader(HttpHeaderConstants.X_USERID, content.username());
        httpResponse.setHeader(HttpHeaderConstants.X_USERNAME, content.username());
        httpResponse.setHeader(HttpHeaderConstants.X_ROLES, content.roles());
    }
}
