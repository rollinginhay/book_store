package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sd_009.bookstore.entity.book.Creator;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Creator}
 */
@AllArgsConstructor
@Getter
@Builder
@Resource(type = "creator")
public class CreatorDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    @Id
    private final Long id;
    private final String name;
    private final String role;
}