package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.entity.book.Publisher;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Publisher}
 */
@AllArgsConstructor
@Getter
@Resource(type = "publisher")
public class PublisherOwningDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    private final String id;
    private final String name;
    private final List<BookDto> books;
}