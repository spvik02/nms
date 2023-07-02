package ru.clevertec.nms.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.clevertec.nms.model.exception.ExceptionInfo;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {

        ExceptionInfo exceptionInfo = buildExceptionInfo(HttpStatus.BAD_REQUEST, ex, request);
        log.error(exceptionInfo.toString());
        return buildResponseEntity(exceptionInfo);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.error(buildExceptionInfo(HttpStatus.valueOf(statusCode.value()), ex, request).toString());
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
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
