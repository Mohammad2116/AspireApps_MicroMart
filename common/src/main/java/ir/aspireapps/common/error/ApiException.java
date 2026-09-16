package ir.aspireapps.common.error;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

@Builder
public record ApiException(
        Instant timestamp,
        HttpStatus httpStatus,
        String code,
        String reason,
        String message,
        String path,
        Map<String,String> errors
) {
}
