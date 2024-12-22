package com.hust.documentweb.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponsePageDTO<T> extends ResponseDTO<T> {
    private long totalElements;

    public ResponsePageDTO(int status, Object message, T value, long totalElements) {
        super(status, message, value);
        this.setTotalElements(totalElements);
    }

    public static <T> ResponsePageDTO<T> success(Object message, T value, long totalElements) {
        return new ResponsePageDTO<>(200, message, value, totalElements);
    }

    public static <T> ResponsePageDTO<T> success(T value, long totalElements) {
        return new ResponsePageDTO<>(200, null, value, totalElements);
    }
}
