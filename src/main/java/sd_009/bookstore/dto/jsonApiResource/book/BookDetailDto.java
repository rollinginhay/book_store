package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sd_009.bookstore.entity.book.BookDetail;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link BookDetail}
 */

@AllArgsConstructor
@Getter
@Builder
@Resource(type = "bookDetail")
public class BookDetailDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    @Id
    private final String id;
    private final String isbn;
    private final String bookFormat;
    private final String dimensions;
    private final Long printLength;
    private final Long stock;
    private final Long price;
    private final String bookCondition;
}