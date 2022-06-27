package com.daria.javatemplate.core.common.exception;

import lombok.Getter;

@Getter
public class AdminErrorException extends RuntimeException {
    private AdminErrorType responseStatusType;
    private String customMessage;

    public AdminErrorException(AdminErrorType responseStatusType) {
        super(responseStatusType.getMessage());
        this.responseStatusType = responseStatusType;
    }

    public AdminErrorException(AdminErrorType responseStatusType, Throwable t) {
        super(t);
        this.responseStatusType = responseStatusType;
    }

    public AdminErrorException(AdminErrorType responseStatusType, String customMessage) {
        super(responseStatusType.getMessage());
        this.responseStatusType = responseStatusType;
        this.customMessage = customMessage;
    }

    public AdminErrorException(AdminErrorType responseStatusType, Throwable t, String customMessage) {
        super(t);
        this.responseStatusType = responseStatusType;
        this.customMessage = customMessage;
    }

    public AdminErrorException(AdminErrorType responseStatusType, Throwable t, String customMessageFormat, String customMessageArgs) {
        super(t);
        this.responseStatusType = responseStatusType;
        this.customMessage = String.format(customMessageFormat, customMessageArgs);
    }
}
