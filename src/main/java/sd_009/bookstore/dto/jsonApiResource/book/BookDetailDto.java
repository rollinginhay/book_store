package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.Builder;
import sd_009.bookstore.entity.book.BookDetail;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link BookDetail}
 */
@Builder
@Resource(type = "bookDetail")
public record BookDetailDto(LocalDateTime createdAt, LocalDateTime updatedAt,
                            Boolean enabled, String note, @Id String id,
                            String isbn11, String isbn13, String bookFormat,
                            String dimensions, Long printLength, Long stock,
                            Long price,
                            String bookCondition) implements Serializable {
}