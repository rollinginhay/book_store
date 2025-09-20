package sd_009.bookstore.dto.jsonApiResource.book;

import jsonapi.Id;
import jsonapi.Resource;
import jsonapi.ToMany;
import jsonapi.ToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sd_009.bookstore.entity.book.Book;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Book}
 */
@AllArgsConstructor
@Getter
@Builder
@Resource(type = "book")
public class BookDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    @Id
    private final String id;
    private final String title;
    private final String language;
    private final String edition;
    private final LocalDateTime published;
    @ToMany(name = "creators")
    private final List<CreatorDto> creators;
    @ToMany(name = "genres")
    private final List<GenreDto> genres;
    @ToMany(name = "tags")
    private final List<TagDto> tags;
    @ToMany(name = "reviews")
    private final List<ReviewDto> reviews;
    @ToOne(name = "publisher")
    private final PublisherDto publisher;
    @ToMany(name = "bookCopies")
    private final List<BookDetailDto> bookCopies;
    @ToOne(name = "series")
    private final SeriesDto series;
    private final String imageUrl;
}