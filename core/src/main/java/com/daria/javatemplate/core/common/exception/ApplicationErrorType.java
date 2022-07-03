package com.daria.javatemplate.core.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 주의! error code 값을 변경할 때는 클라이언트에서 혹시 사용하고 있지 않은지 꼭 확인해야 합니다!
 */
@AllArgsConstructor
public enum ApplicationErrorType {
    /**
     * common (-10000 ~ -19999)
     */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, -10000, "try.again"),
    INVALID_DATA_ARGUMENT(HttpStatus.BAD_REQUEST, -10001, "try.again"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -10002, "try.again"),
    INTERNAL_CONFIGURATION_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -10003, "try.again"),
    INVALID_VALIDATION_CODE(HttpStatus.BAD_REQUEST, -15000, "try.again"),
    INVALID_REQUEST_TO_ROOT_VIEW(HttpStatus.BAD_REQUEST, -10004, "try.again"/*클라이언트에서 Root View 로 이동*/),
    CLIENT_ABORT(HttpStatus.BAD_REQUEST, -10005, "try.again"),
    ALREADY_ACCOUNT(HttpStatus.BAD_REQUEST, -10006, "이미 가입된 이메일입니다. 일반 로그인해주세요"),

    JSON_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -10007 , "json 파싱에 실패했습니다."),

    /**
     * Maintenance Mode (-99999)
     */
    MAINTENANCE_MODE_IS_ON(HttpStatus.SERVICE_UNAVAILABLE, -99999, "maintenance.mode");


    @Getter
    private HttpStatus httpStatus;

    @Getter
    private Integer code;

    @Getter
    private String message;

    public int getStatusCode() {
        return httpStatus.value();
    }
}
