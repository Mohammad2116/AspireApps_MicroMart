package ir.aspireapps.common.error;

public final class BusinessExceptionCodes {
    private BusinessExceptionCodes() {}

    public static final String DUPLICATED_ENTITY = "DUPLICATED_ENTITY_VALUE_AT_UNIQUE_FIELD";
    public static final String ENTITY_NOT_FOUND = "ENTITY_NOT_FOUND";

    public static final String METHOD_ARGUMENT_NOT_VALID = "VALIDATION_ERROR_AT_METHOD_ARGUMENTS";
    public static final String INVALID_REQUEST_BODY = "REQUEST_BODY_IS_INVALIED";

    public static final String UNAUTHORIZED_ERROR = "AUTHORIZATION_FAILED";

    public static final String INTERNAL_SERVER_ERROR = "SOMETHING_WENT_WRONG";
}
