package sd_009.bookstore.dto.jsonApiResource.receipt;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jsonapi.Id;
import jsonapi.Resource;
import jsonapi.ToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.entity.receipt.ReceiptDetail;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link ReceiptDetail}
 */
@AllArgsConstructor
@Getter
@Resource(type = "receiptDetail")
public class ReceiptDetailDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private final String note;
    @Id
    private final String id;
    @ToOne(name = "bookDetail")
    @NotNull(message = "Chi tiết sách là bắt buộc")
    private final BookDetailDto bookCopy;
    @NotNull(message = "Giá mỗi đơn vị là bắt buộc")
    @DecimalMin(value = "0.01", message = "Giá mỗi đơn vị phải lớn hơn 0")
    private final Double pricePerUnit;
    @NotNull(message = "Số lượng là bắt buộc")
    @DecimalMin(value = "0.0", message = "Số lượng không được âm")
    private final Double quantity;

    private final Long bookDetailId;
}