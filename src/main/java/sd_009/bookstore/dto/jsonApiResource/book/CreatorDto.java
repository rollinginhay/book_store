package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.Builder;
import sd_009.bookstore.entity.book.Creator;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Creator}
 */
@Builder
@Resource(type = "creator")
public record CreatorDto(LocalDateTime createdAt, LocalDateTime updatedAt,
                         Boolean enabled, String note, @Id String id, String name,
                         String role) implements Serializable {
}