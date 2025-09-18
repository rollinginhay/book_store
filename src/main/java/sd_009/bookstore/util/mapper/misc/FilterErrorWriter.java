package sd_009.bookstore.util.mapper.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import sd_009.bookstore.dto.response.generic.ApiErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FilterErrorWriter {
    private final ObjectMapper objectMapper;

    public void writeToResp(HttpServletResponse resp, int status, String message, String description) throws IOException {
        ApiErrorResponse errBody = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .description(description)
                .build();

        resp.setStatus(status);
        resp.setContentType("application/vnd.api+json");

        objectMapper.writeValue(resp.getWriter(), errBody);
    }
}
