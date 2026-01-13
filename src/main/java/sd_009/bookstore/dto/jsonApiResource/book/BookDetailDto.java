package sd_009.bookstore.dto.jsonApiResource.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.util.validation.annotation.ValidISBN;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link BookDetail}
 */

@AllArgsConstructor
@Getter
@Builder
@Resource(type = "bookDetail")
public class BookDetailDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private final String note;
    @Id
    private final String id;
    @NotBlank(message = "ISBN là bắt buộc")
    @ValidISBN
    private final String isbn;
    @NotBlank(message = "Định dạng sách là bắt buộc")
    private final String bookFormat;
    private final String dimensions;
    private final Long printLength;
    @NotNull(message = "Số lượng tồn kho là bắt buộc")
    @Min(value = 0, message = "Số lượng tồn kho không được âm")
    private final Long stock;
    @NotNull(message = "Giá nhập là bắt buộc")
    @Min(value = 0, message = "Giá nhập không được âm")
    private final Long supplyPrice;
    @NotNull(message = "Giá bán là bắt buộc")
    @Min(value = 1, message = "Giá bán phải lớn hơn 0")
    private final Long salePrice;
    private final String bookCondition;

}