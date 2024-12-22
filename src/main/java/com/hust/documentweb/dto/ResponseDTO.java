package com.hust.documentweb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ResponseDTO<T> {
    private int status;
    private Object message;
    private T value;

    public static <T> ResponseDTO<T> success(Object message, T value) {
        return new ResponseDTO<>(200, message, value);
    }

    public static <T> ResponseDTO<T> success(T value) {
        return new ResponseDTO<>(200, null, value);
    }

    public static <T> ResponseDTO<T> success() {
        return new ResponseDTO<>(200, null, null);
    }

    public static <T> ResponseDTO<T> fail(Object message, T value) {
        return new ResponseDTO<>(400, message, value);
    }

    public static <T> ResponseDTO<T> fail(T value) {
        return new ResponseDTO<>(400, null, value);
    }

    public static <T> ResponseDTO<T> unauthorized(Object message) {
        return new ResponseDTO<>(401, message, null);
    }

    public static <T> ResponseDTO<T> forbidden(Object message) {
        return new ResponseDTO<>(403, message, null);
    }
}
