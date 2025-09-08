package hn_152.bookstore.config.exceptionHanding;

import hn_152.bookstore.model.dto.response.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> anyExceptionHandler(Exception e, WebRequest request) {
        logger.error("Catch-all ex handler", e);
        return ApiErrorWriter.write(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> unauthorizedExceptionHandler(Exception e, WebRequest request) {
        logger.error("Unauthorized ex handler", e);
        return ApiErrorWriter.write(e, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> elementNotFoundExceptionHandler(Exception e, WebRequest request) {
        logger.error("Element not found ex handler", e);
        return ApiErrorWriter.write(e, request, HttpStatus.NOT_FOUND);
    }
}
