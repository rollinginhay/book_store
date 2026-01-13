package sd_009.bookstore.dto.jsonApiResource.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sd_009.bookstore.entity.book.Publisher;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Publisher}
 */
@AllArgsConstructor
@Getter
@Builder
@Resource(type = "publisher")
public class PublisherDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private final String note;
    @Id
    private final String id;
    @NotBlank(message = "Tên nhà xuất bản là bắt buộc")
    @Size(min = 2, max = 255, message = "Tên nhà xuất bản phải từ 2 đến 255 ký tự")
    private final String name;
}