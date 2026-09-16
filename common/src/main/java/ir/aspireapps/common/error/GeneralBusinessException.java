package ir.aspireapps.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GeneralBusinessException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;

    public GeneralBusinessException(String message, HttpStatus httpStatus, String code) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }
}
