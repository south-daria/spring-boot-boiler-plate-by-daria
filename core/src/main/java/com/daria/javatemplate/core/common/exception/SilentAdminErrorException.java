package com.daria.javatemplate.core.common.exception;

import lombok.Getter;

@Getter
public class SilentAdminErrorException extends AdminErrorException {

    public SilentAdminErrorException(AdminErrorType responseStatusType) {
        super(responseStatusType);
    }

    public SilentAdminErrorException(AdminErrorType responseStatusType, Throwable t) {
        super(responseStatusType, t);
    }

    public SilentAdminErrorException(AdminErrorType responseStatusType, String customMessage) {
        super(responseStatusType, customMessage);
    }

    public SilentAdminErrorException(AdminErrorType responseStatusType, Throwable t, String customMessage) {
        super(responseStatusType, t, customMessage);
    }
}
