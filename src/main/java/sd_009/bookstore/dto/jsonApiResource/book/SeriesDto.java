package sd_009.bookstore.dto.jsonApiResource.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sd_009.bookstore.entity.book.Series;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Series}
 */
@AllArgsConstructor
@Getter
@Builder
@Resource(type = "series")
public class SeriesDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private final String note;
    @Id
    private final String id;
    @NotBlank(message = "Tên bộ sách là bắt buộc")
    @Size(min = 2, max = 255, message = "Tên bộ sách phải từ 2 đến 255 ký tự")
    private final String name;
}