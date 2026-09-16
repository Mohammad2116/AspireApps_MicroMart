package ir.aspireapps.common.error;

import org.springframework.http.HttpStatus;

public class DuplicatedEntityException extends GeneralBusinessException{
    public DuplicatedEntityException(String message, HttpStatus httpStatus, String code) {
        super(message, HttpStatus.CONFLICT, BusinessExceptionCodes.DUPLICATED_ENTITY);
    }
}
