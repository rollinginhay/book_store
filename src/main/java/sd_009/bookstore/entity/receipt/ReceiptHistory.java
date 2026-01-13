package sd_009.bookstore.entity.receipt;

import jakarta.persistence.*;
import lombok.*;
import sd_009.bookstore.entity.AuditableEntity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "receipt_history")
public class ReceiptHistory extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;

    @Column(name = "actor_name", length = 255)
    private String actorName;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", columnDefinition = "VARCHAR(255)")
    private OrderStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", columnDefinition = "VARCHAR(255)")
    private OrderStatus newStatus;
}

