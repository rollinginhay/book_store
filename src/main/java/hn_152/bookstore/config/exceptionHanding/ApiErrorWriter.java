package hn_152.bookstore.config.exceptionHanding;

import hn_152.bookstore.model.dto.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

public class ApiErrorWriter {
    public static ResponseEntity<ApiError> write(Exception ex, WebRequest webRequest, HttpStatus httpStatus) {
        ApiError errBody = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description(webRequest.getDescription(false))
                .build();

        return new ResponseEntity<>(errBody, httpStatus);
    }
}
