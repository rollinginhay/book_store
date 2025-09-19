package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.Builder;
import sd_009.bookstore.entity.book.Publisher;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Publisher}
 */
@Builder
@Resource(type = "publisher")
public record PublisherDto(LocalDateTime createdAt, LocalDateTime updatedAt,
                           Boolean enabled, String note, @Id String id,
                           String name) implements Serializable {
}