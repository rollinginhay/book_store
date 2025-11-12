package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.Resource;
import jsonapi.ToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.entity.book.Series;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Series}
 */
@AllArgsConstructor
@Getter
@Resource(type = "series")
public class SeriesOwningDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    @Id
    private final Long id;
    private final String name;
    @ToMany(name = "books")
    private final List<BookDto> books;
}