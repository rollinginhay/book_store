package sd_009.bookstore.util.mapper.misc;

import com.squareup.moshi.JsonAdapter;
import jakarta.servlet.http.HttpServletResponse;
import jsonapi.Document;
import jsonapi.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class ErrorMapper {
    private final JsonApiAdapterProvider adapterProvider;

    public String toApiErrorDoc(Exception e, WebRequest req, HttpStatus status) {


        if (e instanceof MethodArgumentNotValidException) {
            JsonAdapter<Document<Error>> adapter = adapterProvider.errorAdapter();
            return adapter
                    .toJson(Document
                            .from(((MethodArgumentNotValidException) e).getFieldErrors().stream()
                                    .map(err -> new Error(
                                            null,
                                            String.valueOf(status.value()),
                                            null,
                                            err.getDefaultMessage(),
                                            req.getDescription(false)))
                                    .toList()));
        }

        JsonAdapter<Document<Error>> adapter = adapterProvider.errorAdapter();
        return adapter.toJson(Document.from(Collections.singletonList(new Error(null, String.valueOf(status.value()), null, e.getMessage(), req.getDescription(false)))));
    }

    public void writeFilterErrorDoc(HttpServletResponse resp, int status, String message, String detail) throws IOException {

        Error err = new Error(null, String.valueOf(status), null, message, detail);


        String json = adapterProvider.errorAdapter().toJson(Document.from(err));

        resp.setStatus(status);
        resp.setContentType("application/vnd.api+json");

        resp.getWriter().write(json);
    }
}
