package hn_152.bookstore.config.exceptionHanding;

import hn_152.bookstore.model.dto.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> anyExceptionHandler(Exception e, WebRequest request) {
        return ApiErrorWriter.write(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
