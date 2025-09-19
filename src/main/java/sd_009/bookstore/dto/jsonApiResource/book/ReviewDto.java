package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.Builder;
import sd_009.bookstore.entity.book.Review;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Review}
 */
@Builder
@Resource(type = "review")
public record ReviewDto(LocalDateTime createdAt, LocalDateTime updatedAt,
                        Boolean enabled, String note, @Id String id,
                        Integer rating,
                        String comment) implements Serializable {
}