package sd_009.bookstore.dto.jsonApiResource.voucher;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.entity.voucher.VoucherType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link sd_009.bookstore.entity.voucher.Voucher}
 */
@Getter
@AllArgsConstructor
@Resource(type = "voucher")
public class VoucherDto implements Serializable {
    @Id
    private final String id;
    private final String name;
    private final VoucherType voucherType;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    private Double minTotal;
    private Double percentage;
    private Double maxDiscount;
    private String code;
    private Boolean used;
}

