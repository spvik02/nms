package ru.clevertec.nms.model.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class ExceptionInfo {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private HttpStatus status;
    private String message;
    private Class<?> exception;
    private String path;
}
