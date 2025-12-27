package sd_009.bookstore.entity.voucher;

import jakarta.persistence.*;
import lombok.*;
import sd_009.bookstore.entity.AuditableEntity;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Voucher extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Double minTotal;

    private Double percentage;

    private Double maxDiscount;

    private String code;

    private Boolean used = false;
}

