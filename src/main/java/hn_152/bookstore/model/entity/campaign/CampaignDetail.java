package hn_152.bookstore.model.entity.campaign;

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
public class CampaignDetail extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BookDetail bookDetail;

    private Double value;
}
