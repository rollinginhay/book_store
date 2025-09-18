package sd_009.bookstore.entity.campaign;

import sd_009.bookstore.entity.AuditableEntity;
import sd_009.bookstore.entity.book.BookDetail;
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
    private Campaign campaign;
    
    @ManyToOne
    private BookDetail bookDetail;

    private Double value;
}
