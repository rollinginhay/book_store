package hn_152.bookstore.model.entity.cart;

import hn_152.bookstore.model.entity.AuditableEntity;
import hn_152.bookstore.model.entity.user.User;
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

    @OneToMany
    private List<CartDetail> cartDetails;
}
