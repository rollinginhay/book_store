package sd_009.bookstore.config.jsonapi;

import com.squareup.moshi.JsonAdapter.Factory;
import com.squareup.moshi.Moshi;
import jsonapi.JsonApiFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sd_009.bookstore.dto.jsonApiResource.book.*;

import java.time.LocalDateTime;

@Configuration
public class JsonApiMoshiConfig {
    @Bean
    public Factory jsonApiFactory() {
        return new JsonApiFactory.Builder()
                .addType(BookDetailDto.class)
                .addType(BookDto.class)
                .addType(CreatorDto.class)
                .addType(GenreDto.class)
                .addType(PublisherDto.class)
                .addType(ReviewDto.class)
                .addType(SeriesDto.class)
                .addType(TagDto.class)
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
