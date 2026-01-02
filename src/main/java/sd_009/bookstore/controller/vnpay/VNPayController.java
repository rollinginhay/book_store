package sd_009.bookstore.controller.vnpay;

import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.entity.receipt.OrderStatus;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.PaymentType;
import sd_009.bookstore.entity.receipt.Receipt;
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

    public VNPayController(VNPayService vnPayService,
                           ReceiptRepository receiptRepository,
                           PaymentDetailRepository paymentDetailRepository) {
        this.vnPayService = vnPayService;
        this.receiptRepository = receiptRepository;
        this.paymentDetailRepository = paymentDetailRepository;
    }

    @PostMapping("/pay-receipt/{id}")
    public Map<String, Object> payReceipt(@PathVariable Long id,
                                          @RequestParam String returnUrl) {

        String paymentUrl = vnPayService.createPaymentForReceipt(id, returnUrl);

        return Map.of("status", "success", "paymentUrl", paymentUrl);
    }
@GetMapping("/return")
public String paymentReturn(
        @RequestParam Map<String, String> params,
        @RequestParam("receiptId") Long receiptId,
        @RequestParam("txnRef") String txnRef
) {

    System.out.println("==== CALLBACK VNPay ====");
    System.out.println("Params: " + params);
    System.out.println("ReceiptId: " + receiptId + ", TxnRef: " + txnRef);

    Map<String, String> vnpParams = new HashMap<>();
    for (Map.Entry<String, String> entry : params.entrySet()) {
        if (entry.getKey().startsWith("vnp_")) {
            vnpParams.put(entry.getKey(), entry.getValue());
        }
    }

    int verify = vnPayService.verifyReturn(vnpParams);

    System.out.println("Verify result = " + verify);
    // 1️⃣ Verify chữ ký
    if (verify == -1) {
        return "Sai chữ ký âcccccc!";
    }


    // 2️⃣ Lấy receipt
    Receipt receipt = receiptRepository.findById(receiptId).orElse(null);
    if (receipt == null) return "Không tìm thấy hóa đơn";

    // 3️⃣ Lấy hoặc tạo PaymentDetail
    PaymentDetail payment = paymentDetailRepository.findByProviderId(txnRef);
    if (payment == null) {
        payment = new PaymentDetail();
        payment.setProviderId(txnRef);
        payment.setProvider("VNPAY");
        payment.setReceipt(receipt);
        payment.setId(receipt.getPaymentDetail().getId());
        payment.setPaymentType(PaymentType.TRANSFER);
    }

    // 4️⃣ Xử lý trạng thái
    if (verify == 1) {
        receipt.setOrderStatus(OrderStatus.PAID);
        receipt.setPaymentDate(LocalDateTime.now());
        payment.setAmount(receipt.getGrandTotal());

        paymentDetailRepository.save(payment);
        receiptRepository.save(receipt);

        return "Thanh toán thành công!";
    } else {
        receipt.setOrderStatus(OrderStatus.FAILED);
        receiptRepository.save(receipt);
        return "Thanh toán thất bại!";
    }
}
}
