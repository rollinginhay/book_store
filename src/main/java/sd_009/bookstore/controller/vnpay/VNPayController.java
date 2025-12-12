package sd_009.bookstore.controller.vnpay;

import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.entity.receipt.OrderStatus;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.repository.PaymentDetailRepository;
import sd_009.bookstore.repository.ReceiptRepository;

import java.time.LocalDateTime;
import java.util.Map;
@RestController
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
    public String paymentReturn(@RequestParam Map<String,String> params) {

        Long receiptId = Long.valueOf(params.get("receiptId"));
        String txnRef = params.get("txnRef");

        Receipt receipt = receiptRepository.findById(receiptId).orElse(null);
        PaymentDetail payment = paymentDetailRepository.findByProviderId(txnRef);

        int verify = vnPayService.verifyReturn(params);

        if (verify == 1) {
            receipt.setOrderStatus(OrderStatus.PAID);
            receipt.setPaymentDate(LocalDateTime.now());

            payment.setAmount(receipt.getGrandTotal());
            payment.setProviderId(txnRef);
            payment.setProvider("VNPAY");
            paymentDetailRepository.save(payment);

            receiptRepository.save(receipt);

            return "Thanh toán thành công";
        } else if (verify == 0) {
            receipt.setOrderStatus(OrderStatus.FAILED);
            receiptRepository.save(receipt);
            return "Thanh toán thất bại";
        } else {
            return "Sai chữ ký!";
        }
    }
}
