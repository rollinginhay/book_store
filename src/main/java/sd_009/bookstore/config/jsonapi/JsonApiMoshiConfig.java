package sd_009.bookstore.config.jsonapi;

import com.squareup.moshi.JsonAdapter.Factory;
import com.squareup.moshi.Moshi;
import jsonapi.JsonApiFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sd_009.bookstore.dto.jsonApiResource.book.*;
import sd_009.bookstore.dto.jsonApiResource.campaign.CampaignDetailDto;
import sd_009.bookstore.dto.jsonApiResource.campaign.CampaignDto;
import sd_009.bookstore.dto.jsonApiResource.cart.CartDetailDto;
import sd_009.bookstore.dto.jsonApiResource.receipt.PaymentDetailDto;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDetailDto;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDto;
import sd_009.bookstore.dto.jsonApiResource.user.RoleDto;
import sd_009.bookstore.dto.jsonApiResource.user.UserDto;

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
                .addType(ReceiptDto.class)
                .addType(ReceiptDetailDto.class)
                .addType(PaymentDetailDto.class)
                .addType(CampaignDto.class)
                .addType(CampaignDetailDto.class)
                .addType(UserDto.class)
                .addType(CartDetailDto.class)
                .addType(RoleDto.class)
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
