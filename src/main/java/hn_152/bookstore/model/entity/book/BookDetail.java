package hn_152.bookstore.model.entity.book;

import hn_152.bookstore.model.entity.AuditableEntity;
import hn_152.bookstore.model.entity.receipt.ReceiptDetail;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.aop.target.LazyInitTargetSource;

import java.util.List;

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
