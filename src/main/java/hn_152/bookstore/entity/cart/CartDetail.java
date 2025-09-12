package hn_152.bookstore.entity.cart;

import hn_152.bookstore.entity.AuditableEntity;
import hn_152.bookstore.entity.book.BookDetail;
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
