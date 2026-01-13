package sd_009.bookstore.dto.jsonApiResource.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import jsonapi.Id;
import jsonapi.Resource;
import jsonapi.ToMany;
import jsonapi.ToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.util.validation.annotation.ValidBookTitle;

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
    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private final String note;
    @Id
    private final String id;
    @Size(max = 2000, message = "Mô tả không được vượt quá 2000 ký tự")
    private final String blurb;
    @NotBlank(message = "Tiêu đề là bắt buộc")
    @Size(min = 1, max = 500, message = "Tiêu đề phải từ 1 đến 500 ký tự")
    @ValidBookTitle
    private final String title;
    private final String language;
    @Size(max = 100, message = "Phiên bản không được vượt quá 100 ký tự")
    private final String edition;
    @PastOrPresent(message = "Ngày xuất bản không được trong tương lai")
    private final LocalDateTime published;
    @ToMany(name = "creators")
    private final List<CreatorDto> creators;
    @ToMany(name = "genres")
    private final List<GenreDto> genres;
    @ToMany(name = "tags")
    private final List<TagDto> tags;
    @ToOne(name = "publisher")
    private final PublisherDto publisher;
    @ToMany(name = "bookCopies")
    private final List<BookDetailDto> bookCopies;
    @ToOne(name = "series")
    private final SeriesDto series;
    private final String imageUrl;
}