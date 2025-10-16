package sd_009.bookstore.dto.jsonApiResource.receipt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.PaymentType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link PaymentDetail}
 */
@AllArgsConstructor
@Getter
public class PaymentDetailDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    private final Long id;
    private final PaymentType paymentType;
    private final String provider;
    private final String providerId;
    private final Long amount;
}