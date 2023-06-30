package ru.clevertec.nms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.clevertec.nms.model.exception.ExceptionInfo;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler {


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {

        return buildResponseEntity(buildExceptionInfo(HttpStatus.BAD_REQUEST, ex, request));
    }


    private ExceptionInfo buildExceptionInfo(HttpStatus httpStatus, Exception ex, WebRequest request) {
        return ExceptionInfo.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage() + "(" + ex.getLocalizedMessage() + ")")
                .exception(ex.getClass())
                .path(request.getDescription(false))
                .build();
    }

    private ResponseEntity<Object> buildResponseEntity(ExceptionInfo exceptionInfo) {
        return new ResponseEntity<>(exceptionInfo, exceptionInfo.getStatus());
    }
}
