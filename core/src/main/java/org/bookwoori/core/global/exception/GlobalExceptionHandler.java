package org.bookwoori.core.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<ErrorDto> handleCustomException(CustomException e,
        HttpServletRequest request) {
        log.error(e);
        ErrorDto errorDto = ErrorDto.builder()
            .timestamp(LocalDateTime.now().toString())
            .status(e.getErrorCode().getStatus())
            .code(e.getErrorCode().getCode())
            .message(e.getErrorCode().getMessage())
            .path(request.getRequestURI())
            .build();
        return new ResponseEntity<>(errorDto, HttpStatusCode.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorDto> handleValidationException(Exception e,
        HttpServletRequest request) {
        BindingResult bindingResult = null;

        if (e instanceof BindException) {
            bindingResult = ((BindException) e).getBindingResult();
        } else if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        }
        FieldError fieldError = bindingResult != null ? bindingResult.getFieldError() : null;

        String errorMessage = fieldError != null
            ? fieldError.getField() + ": " + fieldError.getDefaultMessage()
            : "Validation error";

        ErrorDto errorDto = ErrorDto.builder()
            .timestamp(LocalDateTime.now().toString())
            .status(ErrorCode.BAD_REQUEST.getStatus())
            .code(ErrorCode.BAD_REQUEST.getCode())
            .message(errorMessage)
            .path(request.getRequestURI())
            .build();

        return new ResponseEntity<>(errorDto,
            HttpStatusCode.valueOf(ErrorCode.BAD_REQUEST.getStatus()));
    }
}
