package sd_009.bookstore.dto.jsonApiResource.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sd_009.bookstore.entity.book.Tag;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Tag}
 */
@AllArgsConstructor
@Getter
@Builder
@Resource(type = "tag")
public class TagDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private final String note;
    @Id
    private final String id;
    @NotBlank(message = "Tên nhãn là bắt buộc")
    @Size(min = 2, max = 100, message = "Tên nhãn phải từ 2 đến 100 ký tự")
    private final String name;
}