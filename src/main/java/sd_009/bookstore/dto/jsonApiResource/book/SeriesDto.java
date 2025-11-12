package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sd_009.bookstore.entity.book.Series;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Series}
 */
@AllArgsConstructor
@Getter
@Builder
@Resource(type = "series")
public class SeriesDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    @Id
    private final Long id;
    private final String name;
}