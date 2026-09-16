package ir.aspireapps.identityservice.error;

import ir.aspireapps.common.error.*;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GeneralBusinessException.class)
    public ResponseEntity<ApiException> handleGeneralBusinessException(
            DuplicatedEntityException exception,
            HttpServletRequest request) {
        return generateApiException(exception, request);
    }

    @Nonnull
    private ResponseEntity<ApiException> generateApiException(
            GeneralBusinessException exception,
            HttpServletRequest request) {
        return ResponseEntity.status(exception.getHttpStatus()).body(
                ApiException.builder()
                        .timestamp(Instant.now())
                        .httpStatus(exception.getHttpStatus())
                        .code(exception.getCode())
                        .reason(exception.getHttpStatus().getReasonPhrase())
                        .message(exception.getMessage())
                        .path(request.getRequestURI())
                        .errors(null)
                        .build()
        );
    }
}
