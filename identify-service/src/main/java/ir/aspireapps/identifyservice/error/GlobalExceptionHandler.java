package ir.aspireapps.identifyservice.error;

import ir.aspireapps.common.error.*;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GeneralBusinessException.class)
    public ResponseEntity<ApiException> handleGeneralBusinessException(
            DuplicatedEntityException exception,
            HttpServletRequest request) {
        return generateApiException(exception, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiException> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        return generateApiException(
                HttpStatus.BAD_REQUEST,
                BusinessExceptionCodes.METHOD_ARGUMENT_NOT_VALID,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation method arguments failed",
                request.getRequestURI(),
                exception.getBindingResult()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiException> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception,
            HttpServletRequest request){
        return generateApiException(
                HttpStatus.BAD_REQUEST,
                BusinessExceptionCodes.INVALID_REQUEST_BODY,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Request body is invalid or corrupted",
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiException> handleAuthenticationException(
            AuthenticationException exception,
            HttpServletRequest request) {
        return generateApiException(
                HttpStatus.UNAUTHORIZED,
                BusinessExceptionCodes.UNAUTHORIZED_ERROR,
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Invalid username or password",
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiException> handleException(
            Exception exception,
            HttpServletRequest request) {
        return generateApiException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                BusinessExceptionCodes.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Something went wrong on server side",
                request.getRequestURI(),
                null
        );
    }

    @Nonnull
    private ResponseEntity<ApiException> generateApiException(HttpStatus httpStatus,
                                                              String code,
                                                              String reason,
                                                              String message,
                                                              String path,
                                                              BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        if(bindingResult != null && bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
        }
        return ResponseEntity.status(httpStatus).body(
                ApiException.builder()
                        .timestamp(Instant.now())
                        .httpStatus(httpStatus)
                        .code(code)
                        .reason(reason)
                        .message(message)
                        .path(path)
                        .errors(errors)
                        .build()
        );
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
