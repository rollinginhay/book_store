package sd_009.bookstore.dto.jsonApiResource.receipt;

import jsonapi.Id;
import jsonapi.Resource;
import jsonapi.ToMany;
import jsonapi.ToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sd_009.bookstore.entity.receipt.OrderType;

import java.time.LocalDateTime;
import java.util.List;

@Resource(type = "receipt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptResponseDto {

    @Id
    private String id;

    private Double subTotal;
    private Double discount;
    private Double tax;
    private Double serviceCost;
    private Double grandTotal;

    private Boolean hasShipping;
    private String orderStatus;
    private OrderType orderType;

    // ⚠️ các field CHỈ DÙNG CHO VIEW
    private String paymentMethod;
    private String status;

    private String customerName;
    private String customerPhone;
    private String note;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ToOne(name = "paymentDetail")
    private PaymentDetailDto paymentDetail;

    @ToMany(name = "receiptDetails")
    private List<ReceiptDetailDto> receiptDetails;

    public String getCustomerName() {
        if (this.customerName == null || this.customerName.isBlank()) {
            return "Khách lẻ";
        }
        return this.customerName;
    }

    public String getCustomerPhone() {
        if (this.customerPhone == null || this.customerPhone.isBlank()) {
            return "-";
        }
        return this.customerPhone;
    }

}
