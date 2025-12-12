package sd_009.bookstore.dto.jsonApiResource.cart;


import jsonapi.Id;
import jsonapi.RelationshipsObject;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link .entity.CartDetail}
 */
@Getter
@Setter
@NoArgsConstructor
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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String note;
}
