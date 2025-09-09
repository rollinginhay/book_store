package hn_152.bookstore.model.entity.receipt;


import hn_152.bookstore.model.entity.AuditableEntity;
import hn_152.bookstore.model.entity.user.User;
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
    private String id;

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

    private String orderType;

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
