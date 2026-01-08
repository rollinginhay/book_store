package sd_009.bookstore.controller.vnpay;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.receipt.OrderStatus;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.PaymentType;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.repository.BookDetailRepository;
import sd_009.bookstore.repository.PaymentDetailRepository;
import sd_009.bookstore.repository.ReceiptRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/vnpay")
public class VNPayController {

    private final VNPayService vnPayService;
    private final ReceiptRepository receiptRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final BookDetailRepository bookDetailRepository;

    public VNPayController(VNPayService vnPayService,
                           ReceiptRepository receiptRepository,
                           PaymentDetailRepository paymentDetailRepository,
                           BookDetailRepository bookDetailRepository) {
        this.vnPayService = vnPayService;
        this.receiptRepository = receiptRepository;
        this.paymentDetailRepository = paymentDetailRepository;
        this.bookDetailRepository = bookDetailRepository;
    }

    @PostMapping("/pay-receipt/{id}")
    public Map<String, Object> payReceipt(@PathVariable Long id,
                                          @RequestParam String returnUrl) {

        String paymentUrl = vnPayService.createPaymentForReceipt(id, returnUrl);

        return Map.of("status", "success", "paymentUrl", paymentUrl);
    }
    @GetMapping("/return")
    @Transactional
    public String paymentReturn(
            @RequestParam Map<String, String> params,
            @RequestParam("receiptId") Long receiptId,
            @RequestParam("txnRef") String txnRef
    ) {

        Map<String, String> vnpParams = new HashMap<>();
        params.forEach((k, v) -> {
            if (k.startsWith("vnp_")) vnpParams.put(k, v);
        });

        int verify = vnPayService.verifyReturn(vnpParams);

        if (verify == -1) {
            return "Sai chữ ký!";
        }

        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        PaymentDetail payment =
                paymentDetailRepository.findByProviderId(txnRef);

        if (payment == null) {
            payment = new PaymentDetail();
            payment.setProviderId(txnRef);
            payment.setProvider("VNPAY");
            payment.setReceipt(receipt);
            payment.setId(receipt.getPaymentDetail().getId());
            payment.setPaymentType(PaymentType.TRANSFER);
        }

        // ❌ Thanh toán thất bại
        if (verify != 1) {
            receipt.setOrderStatus(OrderStatus.FAILED);
            receiptRepository.save(receipt);
            return "Thanh toán thất bại!";
        }

        // 🛑 Chống callback trùng
        if (receipt.getOrderStatus() == OrderStatus.PAID) {
            return "Đơn hàng đã được xử lý";
        }

        // 🔥 TRỪ KHO book_detail.stock
        receipt.getReceiptDetails().forEach(rd -> {
            BookDetail bookDetail = rd.getBookCopy();

            if (bookDetail.getStock() < rd.getQuantity()) {
                throw new RuntimeException(
                        "Không đủ tồn kho cho BookDetail ID = " + bookDetail.getId()
                );
            }

            bookDetail.setStock(
                    bookDetail.getStock() - rd.getQuantity()
            );

            bookDetailRepository.save(bookDetail);
        });

        // ✅ Update trạng thái đơn
        receipt.setOrderStatus(OrderStatus.PAID);
        receipt.setPaymentDate(LocalDateTime.now());

        payment.setAmount(receipt.getGrandTotal());
        paymentDetailRepository.save(payment);

        receiptRepository.save(receipt);

        return "Thanh toán thành công!";
    }
}
