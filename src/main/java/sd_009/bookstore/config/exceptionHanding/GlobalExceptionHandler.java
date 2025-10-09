package sd_009.bookstore.config.exceptionHanding;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import sd_009.bookstore.config.exceptionHanding.exception.*;
import sd_009.bookstore.util.mapper.misc.ErrorMapper;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ErrorMapper errorMapper;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> anyExceptionHandler(Exception e, WebRequest request) {
        log.error("Catch-all ex handler", e);

        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> unauthorizedExceptionHandler(Exception e, WebRequest request) {
        log.error("Unauthorized ex handler", e);
        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> badRequestExceptionHandler(Exception e, WebRequest request) {
        log.error("Bad Request ex handler", e);
        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidExceptionHandler(Exception e, WebRequest request) {
        log.error("Arguments not valid ex handler", e);
        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateElementException.class)
    public ResponseEntity<String> duplicateElementExceptionHandler(Exception e, WebRequest request) {
        log.error("Duplicate element ex handler", e);
        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> elementNotFoundExceptionHandler(Exception e, WebRequest request) {
        log.error("Element not found ex handler", e);
        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> noResourceFoundExceptionHandler(Exception e, WebRequest request) {
        log.error("Arguments not valid ex handler", e);
        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IsDisabledException.class)
    public ResponseEntity<String> isDisabledExceptionHandler(Exception e, WebRequest request) {
        log.error("Element is disabled ex handler", e);
        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.CONFLICT);
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DependencyConflictException.class)
    public ResponseEntity<String> dependencyConflictExceptionHandler(Exception e, WebRequest request) {
        log.error("Dependency conflict ex handler", e);
        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.CONFLICT);
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

}
