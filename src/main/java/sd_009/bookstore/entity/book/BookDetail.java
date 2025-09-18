package sd_009.bookstore.entity.book;

import sd_009.bookstore.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

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
    private Book book;

    private String isbn11;

    private String isbn13;

    private String bookFormat;

    private String dimensions;

    private Long printLength;

    private Long stock;

    private Long price;

    private String bookCondition;

}
