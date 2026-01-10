package sd_009.bookstore.entity.receipt;


import jakarta.persistence.*;
import lombok.*;
import sd_009.bookstore.entity.AuditableEntity;
import sd_009.bookstore.entity.user.User;

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


    private Double subTotal;

    private Double discount;

    private Double tax;

    private Double serviceCost;

    private Boolean hasShipping;

    private String shippingService;

    private String shippingId;

    private Double grandTotal;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User customer;

    @ManyToOne
    private User employee;

    private String customerName;

    private String customerPhone;

    private String customerAddress;

    private LocalDateTime paymentDate;

    @OneToOne(mappedBy = "receipt", cascade = CascadeType.PERSIST)
    private PaymentDetail paymentDetail;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.PERSIST)
    private List<ReceiptDetail> receiptDetails;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.PERSIST)
    private List<ReceiptHistory> receiptHistories;

}
