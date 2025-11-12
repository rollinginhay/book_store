package sd_009.bookstore.dto.jsonApiResource.receipt;

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
    private final String note;
    @Id
    private final Long id;
    @ToOne(name = "bookDetail")
    private final BookDetailDto bookCopy;
    private final Long pricePerUnit;
    private final Long quantity;
}