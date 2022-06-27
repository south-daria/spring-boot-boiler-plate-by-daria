package com.daria.javatemplate.core.common.exception;

import org.springframework.http.HttpStatus;

public enum AdminErrorType {

    DUPLICATE_DATA(HttpStatus.BAD_REQUEST, -10000, "중복되는 데이터가 있습니다."),
    ;


    private HttpStatus httpStatus;
    private int code;
    private String message;

    AdminErrorType(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }

    public int getStatusCode() {
        return httpStatus.value();
    }

}
