package com.hust.documentweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BookException extends RuntimeException {
    private final transient Object errorCode;
    private final transient Object errorList;

    public BookException(String message, Object errorCode, Object errorList) {
        super(message);
        this.errorCode = errorCode;
        this.errorList = errorList;
    }

    public BookException(Object errorCode, Object errorList) {
        this.errorCode = errorCode;
        this.errorList = errorList;
    }

    public BookException(Object errorCode) {
        this.errorCode = errorCode;
        this.errorList = null;
    }
}
