package sd_009.bookstore.entity.cart;

import sd_009.bookstore.entity.AuditableEntity;
import sd_009.bookstore.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Cart extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.PERSIST)
    private List<CartDetail> cartDetails;
}
