package hn_152.bookstore.config.exceptionHanding;

import hn_152.bookstore.config.exceptionHanding.exception.BadRequestException;
import hn_152.bookstore.config.exceptionHanding.exception.UnauthorizedException;
import hn_152.bookstore.model.dto.response.ApiErrorResponse;
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
    public ResponseEntity<ApiErrorResponse> anyExceptionHandler(Exception e, WebRequest request) {
        logger.error("Catch-all ex handler", e);
        return ApiErrorWriter.write(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> unauthorizedExceptionHandler(Exception e, WebRequest request) {
        logger.error("Unauthorized ex handler", e);
        return ApiErrorWriter.write(e, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> badRequestExceptionHandler(Exception e, WebRequest request) {
        logger.error("Bad Request ex handler", e);
        return ApiErrorWriter.write(e, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> elementNotFoundExceptionHandler(Exception e, WebRequest request) {
        logger.error("Element not found ex handler", e);
        return ApiErrorWriter.write(e, request, HttpStatus.NOT_FOUND);
    }
}
