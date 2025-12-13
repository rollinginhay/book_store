package sd_009.bookstore.dto.jsonApiResource.cart;


import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO for {@link .entity.CartDetail}
 */
@Getter
@Setter
@AllArgsConstructor
@Resource(type = "cartDetail")
public class CartDetailDto {

    @Id
    private final String id;
    private final String userId;
    private final String bookDetailId;
    private final Long amount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
}
