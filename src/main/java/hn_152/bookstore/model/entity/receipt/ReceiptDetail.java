package hn_152.bookstore.model.entity.receipt;

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

public class ReceiptDetail extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    @ManyToOne(fetch = FetchType.LAZY)
    private Receipt receipt;

    @ManyToOne(fetch = FetchType.EAGER)
    private BookDetail bookCopy;

    private Long pricePerUnit;

    private Long quantity;

}
