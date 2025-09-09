package hn_152.bookstore.config.exceptionHanding;

import com.fasterxml.jackson.databind.ObjectMapper;
import hn_152.bookstore.model.dto.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FilterErrorWriter {
    private final ObjectMapper objectMapper;

    public void write(HttpServletResponse resp, int status, String message, String description) throws IOException {
        ApiErrorResponse errBody = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .description(description)
                .build();

        resp.setStatus(status);
        resp.setContentType("application/json");

        objectMapper.writeValue(resp.getWriter(), errBody);
    }
}
