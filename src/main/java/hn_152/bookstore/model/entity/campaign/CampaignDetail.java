package hn_152.bookstore.model.entity.campaign;

import hn_152.bookstore.model.entity.AuditableEntity;
import hn_152.bookstore.model.entity.book.BookDetail;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
//@Entity
public class CampaignDetail extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BookDetail bookDetail;

    private Double value;
}
