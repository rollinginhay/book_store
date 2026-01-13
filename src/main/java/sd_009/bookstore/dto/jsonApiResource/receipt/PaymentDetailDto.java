package sd_009.bookstore.dto.jsonApiResource.receipt;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jsonapi.Id;
import jsonapi.Resource;
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
@Resource(type = "paymentDetail")
public class PaymentDetailDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private final String note;
    @Id
    private final String id;
    @NotNull(message = "Loại thanh toán là bắt buộc")
    private final PaymentType paymentType;
    private final String provider;
    private final String providerId;
    @NotNull(message = "Số tiền là bắt buộc")
    @DecimalMin(value = "0.01", message = "Số tiền phải lớn hơn 0")
    private final Double amount;
}