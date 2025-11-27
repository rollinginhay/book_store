package sd_009.bookstore.dto.jsonApiResource.checkout;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GuestOrderResponse {
    private Long orderId;
    private Long total;
    private String message;
}
