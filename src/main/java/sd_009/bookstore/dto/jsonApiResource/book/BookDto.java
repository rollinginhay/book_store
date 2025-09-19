package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.Resource;
import jsonapi.ToMany;
import jsonapi.ToOne;
import lombok.Builder;
import sd_009.bookstore.entity.book.Book;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Book}
 */
@Builder
@Resource(type = "book")
public record BookDto(LocalDateTime createdAt, LocalDateTime updatedAt,
                      Boolean enabled, String note, @Id String id, String title,
                      String language, String edition, LocalDateTime published,
                      @ToMany(name = "creators") List<CreatorDto> creators,
                      @ToMany(name = "genres") List<GenreDto> genres,
                      @ToMany(name = "tags") List<TagDto> tags,
                      @ToMany(name = "reviews") List<ReviewDto> reviews,
                      @ToOne(name = "publisher") PublisherDto publisher,
                      @ToMany(name = "bookCopies") List<BookDetailDto> bookCopies,
                      SeriesDto series,
                      String imageUrl) implements Serializable {
}