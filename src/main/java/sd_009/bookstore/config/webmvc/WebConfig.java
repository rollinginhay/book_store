package sd_009.bookstore.config.webmvc;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

//@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final MediaType JSON_API = MediaType.valueOf("application/vnd.api+json");

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer config) {
        // set a sensible default if client Accept header is absent
        config.favorParameter(false).ignoreAcceptHeader(false).defaultContentType(JSON_API);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // remove existing StringHttpMessageConverter(s)
        converters.removeIf(c -> c instanceof StringHttpMessageConverter);

        // create one that only advertises JSON types
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringConverter.setSupportedMediaTypes(List.of(JSON_API, MediaType.APPLICATION_JSON));

        converters.add(0, stringConverter);
    }
}
