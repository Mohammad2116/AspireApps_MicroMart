package ir.aspireapps.common.error;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends GeneralBusinessException{
    public EntityNotFoundException(String message, HttpStatus httpStatus, String code) {
        super(message, HttpStatus.NOT_FOUND, BusinessExceptionCodes.ENTITY_NOT_FOUND);
    }
}
