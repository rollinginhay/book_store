package sd_009.bookstore.controller;

import com.squareup.moshi.JsonAdapter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jsonapi.Document;
import jsonapi.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.util.mapper.misc.ErrorMapper;

/**
 * Requests from browser has Accept:text/html header prompts rendering html. Custom error mapping forces json response
 */
@RestController
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {
    private final ErrorMapper errorMapper;
    private final JsonApiAdapterProvider adapterProvider;

    @GetMapping("/error")
    public ResponseEntity<String> error(HttpServletRequest req) {
        Object status = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = req.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object path = req.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        int statusCode;
        try {
            statusCode = Integer.parseInt(status.toString());
        } catch (Exception e) {
            statusCode = 500;
        }

        //Tomcat doesnt always populate error message
        String messageText = message.toString().isEmpty() ? HttpStatus.valueOf(statusCode).getReasonPhrase() : message.toString();

        Error err = new Error(null, status.toString(), null, messageText, path.toString());

        return new ResponseEntity<>(adapterProvider.errorAdapter().toJson(Document.from(err)), HttpStatusCode.valueOf(statusCode));
    }
}
