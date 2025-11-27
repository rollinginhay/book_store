package sd_009.bookstore.service.receipt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.dto.jsonApiResource.checkout.GuestOrderRequest;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.entity.receipt.ReceiptDetail;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.repository.BookDetailRepository;
import sd_009.bookstore.repository.ReceiptRepository;
import sd_009.bookstore.repository.ReceiptDetailRepository;
import sd_009.bookstore.service.mail.EmailBuilder;
import sd_009.bookstore.service.mail.EmailService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestOrderService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptDetailRepository receiptDetailRepository;
    private final BookDetailRepository bookDetailRepository;
    private final EmailService emailService;

    @Transactional
    public Receipt createGuestOrder(GuestOrderRequest req) {

        // ===== 1. TẠO RECEIPT =====
        Receipt receipt = new Receipt();
        PaymentDetail paymentDetail = new PaymentDetail();
        receipt.setCustomerName(req.getFullName());
        receipt.setCustomerPhone(req.getPhone());
        receipt.setCustomerAddress(req.getAddress());
        receipt.setOrderType(null);        // Nếu có default -> gán
        receipt.setOrderStatus(null);      // Nếu có default -> gán
        receipt.setPaymentDate(LocalDateTime.now());
        receipt.setEnabled(true);

        receipt = receiptRepository.save(receipt);

        // ===== 2. TẠO RECEIPT DETAILS =====
        List<ReceiptDetail> details = new ArrayList<>();
        double subTotal = 0;

        for (GuestOrderRequest.Item item : req.getItems()) {

            BookDetail bd = bookDetailRepository.findById(item.getBookDetailId())
                    .orElseThrow();

            ReceiptDetail d = new ReceiptDetail();
            d.setReceipt(receipt);
            d.setBookCopy(bd);
            d.setQuantity(item.getQuantity());
            d.setPricePerUnit(item.getPricePerUnit());

            subTotal += item.getPricePerUnit() * item.getQuantity();

            receiptDetailRepository.save(d);
            details.add(d);
        }

        // ===== 3. CẬP NHẬT TIỀN =====
        receipt.setSubTotal(subTotal);
        receipt.setGrandTotal(subTotal); // nếu chưa có discount/tax
        receiptRepository.save(receipt);
        // QUAN TRỌNG: Thêm dòng này
        receipt.setReceiptDetails(details);
        // ===== 4. GỬI EMAIL =====
        if (req.getEmail() != null && !req.getEmail().isBlank()) {
            String html = EmailBuilder.buildOrderEmail(receipt,paymentDetail);

            emailService.sendOrderEmail(
                    req.getEmail(),
                    "Xác nhận đơn hàng #" + receipt.getId(),
                    html
            );
        }

        return receipt;
    }
}