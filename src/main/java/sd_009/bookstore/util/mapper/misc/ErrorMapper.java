package sd_009.bookstore.util.mapper.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Types;
import jakarta.servlet.http.HttpServletResponse;
import jsonapi.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.dto.jsonApiResource.error.ErrorObject;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ErrorMapper {
    private final JsonApiAdapterProvider adapterProvider;

    public String toApiErrorDoc(Exception e, WebRequest req, HttpStatus status) {


        if (e instanceof MethodArgumentNotValidException) {
            JsonAdapter<Document<List<ErrorObject>>> adapter = adapterProvider.listResourceAdapter(ErrorObject.class);
            return adapter
                    .toJson(Document
                            .from(((MethodArgumentNotValidException) e).getFieldErrors().stream()
                                    .map(err -> ErrorObject.builder()
                                            .title(err.getDefaultMessage())
                                            .detail(req.getDescription(false))
                                            .build()).toList()));
        }

        JsonAdapter<Document<ErrorObject>> adapter = adapterProvider.singleResourceAdapter(ErrorObject.class);
        return adapter.toJson(Document.from(ErrorObject.builder()
                .title(e.getMessage())
                .detail(req.getDescription(false))
                .status(String.valueOf(status.value()))
                .build()));
    }

    public void writeFilterErrorDoc(HttpServletResponse resp, int status, String message, String detail) throws IOException {
        ErrorObject errObj = ErrorObject.builder()
                .status(String.valueOf(status))
                .title(message)
                .detail(detail)
                .build();

        JsonAdapter<Document<ErrorObject>> adapter = adapterProvider.singleResourceAdapter(ErrorObject.class);

        resp.setStatus(status);
        resp.setContentType("application/vnd.api+json");

        resp.getWriter().write(adapter.toJson(Document.from(errObj)));
    }
}
