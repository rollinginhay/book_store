package sd_009.bookstore.entity.cart;

import jakarta.persistence.*;
import lombok.*;
import sd_009.bookstore.entity.AuditableEntity;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.user.User;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class CartDetail extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private BookDetail bookDetail;

    private Long amount;
}
