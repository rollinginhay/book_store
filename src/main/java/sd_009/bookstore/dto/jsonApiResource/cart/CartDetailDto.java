package sd_009.bookstore.dto.jsonApiResource.cart;


import jsonapi.Id;
import jsonapi.RelationshipsObject;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private String id;

    private String userId;
    private String bookDetailId;
    private Long amount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boolean enabled;
    private String note;
}
