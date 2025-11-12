package sd_009.bookstore.dto.jsonApiResource.cart;


import jsonapi.Id;
import jsonapi.Resource;
import lombok.*;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link .entity.CartDetail}
 */
@Getter
@AllArgsConstructor
@Resource(type = "cartDetail")
public class CartDetailDto implements Serializable {
    @Id
    private final String id;
    private final User user;
    private final BookDetail bookDetail;
    private final Long amount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
}

