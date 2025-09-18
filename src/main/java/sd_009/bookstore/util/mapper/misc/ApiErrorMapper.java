package sd_009.bookstore.util.mapper.misc;

import jsonapi.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.dto.response.generic.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@Component
public class ApiErrorMapper {

    public static ResponseEntity<ApiErrorResponse> mapToRespEntity(Exception ex, WebRequest webRequest, HttpStatus httpStatus) {
        ApiErrorResponse errBody = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .description(webRequest.getDescription(false))
                .build();

        return new ResponseEntity<>(errBody, httpStatus);
    }


}
