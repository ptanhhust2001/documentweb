package com.hust.documentweb.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.hust.documentweb.dto.ResponseDTO;
import com.hust.documentweb.dto.request.ApiResponse;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(ResponseDTO.fail("CONSTRAIN_VIOLATED", validationErrors), HttpStatus.BAD_REQUEST);
    }

    //    @ExceptionHandler(AuthorizationDeniedException.class)
    //    ResponseEntity<ApiResponse> handlingAccessDeniedException(AuthorizationDeniedException exception) {
    //        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
    //
    //
    //        return ResponseEntity
    //                .status(errorCode.getStatusCode())
    //                .body(ApiResponse
    //                        .builder()
    //                        .code(errorCode.getCode())
    //                        .message(errorCode.getMessage())
    //                        .build());
    //    }
    @ExceptionHandler(BookException.class)
    public ResponseEntity<ResponseDTO<Object>> handleMccException(BookException exception, WebRequest webRequest) {
        return new ResponseEntity<>(
                ResponseDTO.fail(exception.getErrorCode(), exception.getErrorList()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(exception.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("File size exceeds limit!");
    }
}
