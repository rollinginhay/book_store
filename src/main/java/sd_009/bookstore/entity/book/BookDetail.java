package sd_009.bookstore.entity.book;

import jakarta.persistence.*;
import lombok.*;
import sd_009.bookstore.entity.AuditableEntity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class BookDetail extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Book book;

    private String isbn;

    private String bookFormat;

    private String dimensions;

    private Long printLength;

    private Long stock;

    private Long supplyPrice;

    private Long salePrice;

    private String bookCondition;

}
