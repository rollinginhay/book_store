package sd_009.bookstore.dto.jsonApiResource.checkout;

import lombok.Data;
import java.util.List;

@Data
public class GuestOrderRequest {
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String note;
    private String paymentMethod;

    private List<Item> items;

    @Data
    public static class Item {
        private Long bookDetailId;
        private Long quantity;
        private Long pricePerUnit;
    }
}