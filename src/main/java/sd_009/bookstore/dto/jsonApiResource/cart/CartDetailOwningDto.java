package sd_009.bookstore.dto.jsonApiResource.cart;

import jsonapi.Id;
import jsonapi.Resource;
import jsonapi.ToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.dto.jsonApiResource.user.UserDto;
import sd_009.bookstore.entity.cart.CartDetail;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link CartDetail}
 */
@AllArgsConstructor
@Getter
@Resource(type = "cartDetail")
public class CartDetailOwningDto implements Serializable {

    @Id
    private final Long id;

    private final Integer quantity;
    private final Double amount;
    private final Boolean enabled;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // Liên kết 1-1
    @ToOne(name = "user")
    private final UserDto user;

    @ToOne(name = "bookDetail")
    private final BookDetailDto bookDetail;
}
