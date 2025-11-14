package com.gdg.jwtexample.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        // 시스템 에러만 ERROR 레벨로 로깅
        if (errorCode.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
            log.error("CustomException [{}]: {}", errorCode, e.getMessage(), e);
        } else {
            // 비즈니스 예외, 클라이언트 오류는 WARN 레벨
            log.warn("CustomException [{}]: {}", errorCode, e.getMessage());
        }

        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        // 검증 실패는 클라이언트 입력 오류이므로, WARN 레벨
        log.warn("Validation failed: {}", errorMessage);

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, errorMessage);
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        // 예상치 못한 시스템 에러는 ERROR 레벨로 로깅
        log.error("Unexpected exception occurred: ", e);

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(errorResponse);
    }
}

