package sd_009.bookstore.dto.jsonApiResource.receipt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sd_009.bookstore.entity.receipt.OrderStatus;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptHistoryDto implements Serializable {
    private OrderStatus oldStatus;
    private OrderStatus newStatus;
    private String actorName;
    private String createdAt;
    private String updatedAt;
}
