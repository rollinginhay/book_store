package sd_009.bookstore.entity.receipt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link ReceiptDetail}
 */
@AllArgsConstructor
@Getter
public class ReceiptDetailDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    private final Long id;
    private final BookDetailDto bookCopy;
    private final Long pricePerUnit;
    private final Long quantity;
}