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

public class PaymentDetail extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Receipt receipt;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private String provider;

    private String providerId;

    private Long amount;
}
