package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.Resource;
import jsonapi.ToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.entity.book.BookDetail;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link BookDetail}
 */
@AllArgsConstructor
@Getter
@Resource(type = "bookDetail")
public class BookDetailOwningDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    @Id
    private final String id;
    @ToOne(name = "book")
    private final BookDto book;
    private final String isbn11;
    private final String isbn13;
    private final String bookFormat;
    private final String dimensions;
    private final Long printLength;
    private final Long stock;
    private final Long price;
    private final String bookCondition;
}