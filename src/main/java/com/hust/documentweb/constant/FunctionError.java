package com.hust.documentweb.constant;

import lombok.Getter;

@Getter
public enum FunctionError {
    NOT_FOUND,
    UPDATE_FAILED,
    DELETE_FAILED,
    UPLOAD_FAILED,
    CAN_NOT_READ_FILE,
    CREATE_FAILED
}
