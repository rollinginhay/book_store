package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.ToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.entity.book.Review;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Review}
 */
@AllArgsConstructor
@Getter
public class ReviewOwningDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    @Id
    private final String id;
    private final Integer rating;
    private final String comment;
    @ToOne(name = "book")
    private final BookDto book;
}