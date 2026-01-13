package sd_009.bookstore.dto.jsonApiResource.receipt;

import jakarta.validation.constraints.*;
import jsonapi.Id;
import jsonapi.Resource;
import jsonapi.ToMany;
import jsonapi.ToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;
import sd_009.bookstore.dto.jsonApiResource.user.UserDto;
import sd_009.bookstore.entity.receipt.OrderType;
import sd_009.bookstore.entity.receipt.Receipt;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Receipt}
 */
@Resource(type = "receipt")
@AllArgsConstructor
@Getter
public class ReceiptDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    @Id
    private final String id;
    private final Double subTotal;
    private final Double discount;
    private final Double tax;
    private final Double serviceCost;
    @NotNull(message = "Trạng thái vận chuyển là bắt buộc")
    private final Boolean hasShipping;
    private final String shippingService;
    private final String shippingId;
    private final Double grandTotal;
    private final String orderStatus;
    @NotNull(message = "Loại đơn hàng là bắt buộc")
    private final OrderType orderType;
    @ToOne(name = "customer")
    private final UserDto customer;
    @ToOne(name = "employee")
    @Nullable
    private final UserDto employee;
    @Size(min = 2, max = 100, message = "Tên khách hàng phải từ 2 đến 100 ký tự")
    private final String customerName;
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại phải có thực")
    private final String customerPhone;
    @Size(max = 500, message = "Địa chỉ khách hàng tối đa 500 ký tự")
    private final String customerAddress;
    @NotNull
    @PastOrPresent(message = "Ngày thanh toán không hợp lệ")
    private final LocalDateTime paymentDate;
    @ToOne(name = "paymentDetail")
    private final PaymentDetailDto paymentDetail;
    @ToMany(name = "receiptDetails")
    private final List<ReceiptDetailDto> receiptDetails;
}