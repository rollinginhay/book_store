package sd_009.bookstore.util.mapper.misc;

import com.squareup.moshi.JsonAdapter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
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
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class ErrorMapper {
    private final JsonApiAdapterProvider adapterProvider;

    public String toApiErrorDoc(Exception e, WebRequest req, HttpStatus status) {

        if (e instanceof ConstraintViolationException) {
            JsonAdapter<Document<Error>> adapter = adapterProvider.errorAdapter();
            return adapter
                    .toJson(Document
                            .from(((ConstraintViolationException) e).getConstraintViolations().stream()
                                    .map(err -> {
                                                String fieldName = null;
                                                Iterator<Path.Node> it = err.getPropertyPath().iterator();
                                                Path.Node last = null;
                                                while (it.hasNext()) {
                                                    last = it.next();
                                                }
                                                if (last != null) {
                                                    fieldName = last.getName();
                                                }

                                                return new Error(
                                                        null,
                                                        String.valueOf(status.value()),
                                                        null,
                                                        err.getMessage(),
                                                        fieldName);
                                            }
                                    )
                                    .toList()));
        }
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
                                            err.getField()))
                                    .toList()));
        }
        JsonAdapter<Document<Error>> adapter = adapterProvider.errorAdapter();
        String errorMessage = e.getMessage() != null && !e.getMessage().isEmpty() 
            ? e.getMessage() 
            : "Lỗi hệ thống. Vui lòng thử lại sau";
        return adapter.toJson(Document.from(Collections.singletonList(new Error(null, String.valueOf(status.value()), null, errorMessage, "server"))));
    }

    public void writeFilterErrorDoc(HttpServletResponse resp, int status, String message, String detail) throws IOException {

        Error err = new Error(null, String.valueOf(status), null, message, detail);


        String json = adapterProvider.errorAdapter().toJson(Document.from(err));

        resp.setStatus(status);
        resp.setContentType("application/vnd.api+json");

        resp.getWriter().write(json);
    }
}
