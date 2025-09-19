package sd_009.bookstore.entity.receipt;

import jakarta.persistence.*;
import lombok.*;
import sd_009.bookstore.entity.AuditableEntity;
import sd_009.bookstore.entity.book.BookDetail;

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
