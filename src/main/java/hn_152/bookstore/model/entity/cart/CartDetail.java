package hn_152.bookstore.model.entity.cart;

import hn_152.bookstore.model.entity.AuditableEntity;
import hn_152.bookstore.model.entity.book.BookDetail;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    private BookDetail bookDetail;

    private Long amount;
}
