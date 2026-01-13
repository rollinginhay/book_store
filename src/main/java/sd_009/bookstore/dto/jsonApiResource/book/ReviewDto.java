package sd_009.bookstore.dto.jsonApiResource.book;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sd_009.bookstore.entity.book.Review;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Review}
 */
@AllArgsConstructor
@Getter
@Builder
@Resource(type = "review")
public class ReviewDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private final String note;
    @Id
    private final String id;
    @NotNull(message = "Xếp hạng là bắt buộc")
    @Min(value = 1, message = "Xếp hạng phải từ 1 đến 5")
    @Max(value = 5, message = "Xếp hạng phải từ 1 đến 5")
    private final Integer rating;
    @Size(min = 5, max = 2000, message = "Bình luận phải từ 5 đến 2000 ký tự")
    private final String comment;
}