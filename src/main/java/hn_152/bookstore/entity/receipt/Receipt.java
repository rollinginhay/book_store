package hn_152.bookstore.entity.receipt;


import hn_152.bookstore.entity.AuditableEntity;
import hn_152.bookstore.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity

public class Receipt extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ReceiptDetail> receiptDetails;

    private Long subTotal;

    private Long discount;

    private Long tax;

    private Long serviceCost;

    private Boolean hasShipping;

    private String shippingService;

    private String shippingId;

    private Long grandTotal;

    private String orderStatus;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @ManyToOne(fetch = FetchType.LAZY)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private User employee;

    private String customerName;

    private String customerPhone;

    private String customerAddress;

    private LocalDateTime paymentDate;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "receipt")
    private PaymentDetail paymentDetail;

}
