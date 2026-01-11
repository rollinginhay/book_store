package sd_009.bookstore.controller.vnpay;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.receipt.OrderStatus;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.PaymentType;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.entity.receipt.ReceiptHistory;
import sd_009.bookstore.repository.BookDetailRepository;
import sd_009.bookstore.repository.PaymentDetailRepository;
import sd_009.bookstore.repository.ReceiptHistoryRepository;
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
    private final ReceiptHistoryRepository receiptHistoryRepository;

    public VNPayController(VNPayService vnPayService,
                           ReceiptRepository receiptRepository,
                           PaymentDetailRepository paymentDetailRepository,
                           BookDetailRepository bookDetailRepository,
                           ReceiptHistoryRepository receiptHistoryRepository) {
        this.vnPayService = vnPayService;
        this.receiptRepository = receiptRepository;
        this.paymentDetailRepository = paymentDetailRepository;
        this.bookDetailRepository = bookDetailRepository;
        this.receiptHistoryRepository = receiptHistoryRepository;
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

        // ✅ Lưu trạng thái cũ để ghi lịch sử
        OrderStatus oldStatus = receipt.getOrderStatus();
        OrderStatus newStatus;

        // ✅ LOGIC ĐÚNG: VNPay thanh toán thành công → chuyển sang AUTHORIZED (không phải PAID)
        // - PENDING → AUTHORIZED (trừ stock)
        // - AUTHORIZED → AUTHORIZED (giữ nguyên, đã trừ stock rồi)
        // - PAID chỉ được set khi giao hàng thành công, không phải khi thanh toán
        if (receipt.getOrderStatus() == OrderStatus.PENDING) {
            // Trường hợp đơn ở PENDING: chuyển sang AUTHORIZED và trừ stock
            receipt.getReceiptDetails().forEach(rd -> {
                BookDetail bookDetail = bookDetailRepository
                        .findById(rd.getBookCopy().getId())
                        .orElseThrow(() -> new RuntimeException("BookDetail not found: " + rd.getBookCopy().getId()));
                
                // Luôn đọc stock mới nhất từ DB
                Long currentStock = bookDetail.getStock();
                if (currentStock == null || currentStock < rd.getQuantity()) {
                    throw new RuntimeException(
                            "Không đủ tồn kho cho BookDetail ID = " + bookDetail.getId()
                    );
                }

                bookDetail.setStock(currentStock - rd.getQuantity());
                bookDetailRepository.save(bookDetail);
                System.out.println("✅ [VNPayController] Đã trừ stock khi thanh toán VNPay (PENDING → AUTHORIZED): BookDetail " + bookDetail.getId() + 
                    " - Stock cũ: " + currentStock + ", Số lượng trừ: " + rd.getQuantity() + 
                    ", Stock mới: " + bookDetail.getStock());
            });
            newStatus = OrderStatus.AUTHORIZED;
            System.out.println("✅ [VNPayController] Đơn PENDING → chuyển sang AUTHORIZED sau khi thanh toán VNPay thành công");
        } else if (receipt.getOrderStatus() == OrderStatus.AUTHORIZED) {
            // Trường hợp đơn đã là AUTHORIZED: giữ nguyên AUTHORIZED (đã trừ stock rồi)
            newStatus = OrderStatus.AUTHORIZED;
            System.out.println("✅ [VNPayController] Đơn đã là AUTHORIZED, giữ nguyên sau khi thanh toán VNPay thành công");
        } else {
            // Trường hợp khác (không nên xảy ra): giữ nguyên status hoặc chuyển sang AUTHORIZED
            newStatus = OrderStatus.AUTHORIZED;
            System.out.println("⚠️ [VNPayController] Đơn ở trạng thái " + oldStatus + ", chuyển sang AUTHORIZED");
        }

        // ✅ Update trạng thái đơn (AUTHORIZED, không phải PAID)
        receipt.setOrderStatus(newStatus);
        receipt.setPaymentDate(LocalDateTime.now());

        payment.setAmount(receipt.getGrandTotal());
        paymentDetailRepository.save(payment);

        Receipt savedReceipt = receiptRepository.save(receipt);

        // ✅ GHI LỊCH SỬ: VNPay thanh toán thành công → CHỈ ghi khi có thay đổi trạng thái
        // Tránh duplicate: nếu oldStatus == newStatus (AUTHORIZED → AUTHORIZED) thì không ghi
        if (oldStatus != newStatus) {
            try {
                ReceiptHistory history = ReceiptHistory.builder()
                        .receipt(savedReceipt)
                        .actorName("VNPay")
                        .oldStatus(oldStatus)
                        .newStatus(newStatus)
                        .build();
                receiptHistoryRepository.save(history);
                System.out.println("✅ [VNPayController] Đã lưu lịch sử: " + oldStatus + " → " + newStatus);
            } catch (Exception e) {
                System.err.println("⚠️ [VNPayController] Lỗi khi lưu lịch sử: " + e.getMessage());
            }
        } else {
            System.out.println("✅ [VNPayController] Không có thay đổi trạng thái (" + oldStatus + " → " + newStatus + "), bỏ qua ghi lịch sử");
        }

        return "Thanh toán thành công!";
    }
}
