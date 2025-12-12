package sd_009.bookstore.dto.jsonApiResource.receipt;

import jsonapi.Id;
import jsonapi.Resource;
import jsonapi.ToMany;
import jsonapi.ToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@NoArgsConstructor
@Getter
@Setter
public class ReceiptDto implements Serializable {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean enabled;
    private String note;

    @Id
    private String id;

    private Double subTotal;
    private Double discount;
    private Double tax;
    private Double serviceCost;
    private Boolean hasShipping;
    private String shippingService;
    private String shippingId;
    private Double grandTotal;

    // 🔥 2 field BẮT BUỘC phải có (json gửi lên)
    private String paymentMethod;
    private String status;

    private String orderStatus;
    private OrderType orderType;

    @ToOne(name = "customer")
    private UserDto customer;

    @ToOne(name = "employee")
    @Nullable
    private UserDto employee;

    private String customerName;
    private String customerPhone;
    private String customerAddress;

    private LocalDateTime paymentDate;

    @ToOne(name = "paymentDetail")
    private PaymentDetailDto paymentDetail;

    @ToMany(name = "receiptDetails")
    private List<ReceiptDetailDto> receiptDetails;
}
