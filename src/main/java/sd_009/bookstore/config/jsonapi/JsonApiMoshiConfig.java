package sd_009.bookstore.config.jsonapi;

import com.squareup.moshi.JsonAdapter.Factory;
import com.squareup.moshi.Moshi;
import jsonapi.JsonApiFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Date;

@Configuration
public class JsonApiMoshiConfig {
    @Bean
    public Factory jsonApiFactory() {
        return new JsonApiFactory.Builder()
                .build();
    }

    @Bean
    public Moshi moshi() {
        return new Moshi.Builder()
                .add(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .add(jsonApiFactory())
                .build();
    }
}
