package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.Builder;
import sd_009.bookstore.entity.book.Tag;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Tag}
 */
@Builder
@Resource(type = "tag")
public record TagDto(LocalDateTime createdAt, LocalDateTime updatedAt,
                     Boolean enabled, String note, @Id String id,
                     String name) implements Serializable {
}