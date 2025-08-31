package hn_152.bookstore.model.entity.receipt;


import hn_152.bookstore.model.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
//@Entity

public class PaymentDetail extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Receipt receipt;

    private String method;

    private String provider;

    private String providerId;

    private Long amount;
}
