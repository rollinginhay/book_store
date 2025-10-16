package sd_009.bookstore.dto.jsonApiResource.receipt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.dto.jsonApiResource.user.UserDto;
import sd_009.bookstore.entity.receipt.OrderType;
import sd_009.bookstore.entity.receipt.Receipt;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Receipt}
 */
@AllArgsConstructor
@Getter
public class ReceiptDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    private final String id;
    private final Long subTotal;
    private final Long discount;
    private final Long tax;
    private final Long serviceCost;
    private final Boolean hasShipping;
    private final String shippingService;
    private final String shippingId;
    private final Long grandTotal;
    private final String orderStatus;
    private final OrderType orderType;
    private final UserDto customer;
    private final UserDto employee;
    private final String customerName;
    private final String customerPhone;
    private final String customerAddress;
    private final LocalDateTime paymentDate;
    private final PaymentDetailDto paymentDetail;
    private final List<ReceiptDetailDto> receiptDetails;
}