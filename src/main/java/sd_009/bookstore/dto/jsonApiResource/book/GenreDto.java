package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sd_009.bookstore.entity.book.Genre;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Genre}
 */
@AllArgsConstructor
@Getter
@Builder
@Resource(type = "genre")
public class GenreDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    @Id
    private final String id;
    private final String name;
}