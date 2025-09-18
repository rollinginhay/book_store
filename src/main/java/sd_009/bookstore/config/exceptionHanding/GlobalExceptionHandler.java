package sd_009.bookstore.config.exceptionHanding;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.exceptionHanding.exception.UnauthorizedException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.dto.jsonApiResource.error.ErrorObject;
import sd_009.bookstore.dto.response.generic.ApiErrorResponse;
import sd_009.bookstore.util.mapper.misc.ApiErrorMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sd_009.bookstore.util.mapper.misc.ErrorMapper;

import java.util.NoSuchElementException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ErrorMapper errorMapper;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> anyExceptionHandler(Exception e, WebRequest request) {
        logger.error("Catch-all ex handler", e);

        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> unauthorizedExceptionHandler(Exception e, WebRequest request) {
        logger.error("Unauthorized ex handler", e);
        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> badRequestExceptionHandler(Exception e, WebRequest request) {
        logger.error("Bad Request ex handler", e);
        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> elementNotFoundExceptionHandler(Exception e, WebRequest request) {
        logger.error("Element not found ex handler", e);
        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidExceptionHandler(Exception e, WebRequest request) {
        logger.error("Arguments not valid ex handler", e);
        String body = errorMapper.toApiErrorDoc(e, request, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
