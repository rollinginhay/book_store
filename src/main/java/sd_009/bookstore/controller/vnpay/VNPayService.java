package sd_009.bookstore.controller.vnpay;

import org.springframework.stereotype.Service;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDto;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.PaymentType;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.repository.PaymentDetailRepository;
import sd_009.bookstore.repository.ReceiptRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
@Service
public class VNPayService {

    private final ReceiptRepository receiptRepository;
    private final PaymentDetailRepository paymentDetailRepository;

    public VNPayService(ReceiptRepository receiptRepository,
                        PaymentDetailRepository paymentDetailRepository) {
        this.receiptRepository = receiptRepository;
        this.paymentDetailRepository = paymentDetailRepository;
    }


    public String createPaymentForReceipt(Long receiptId, String returnBaseUrl) {

        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));

        int total = receipt.getGrandTotal().intValue();
        String orderInfo = "Thanh toán đơn hàng #" + receipt.getId();

        // Tạo mã giao dịch VNPay (vnp_TxnRef)
        String txnRef = VNPayConfig.getRandomNumber(8);

        // Lưu PaymentDetail (PENDING)
        PaymentDetail payment = PaymentDetail.builder()
                .amount(receipt.getGrandTotal())
                .paymentType(PaymentType.TRANSFER)
                .provider("VNPAY")
                .providerId(txnRef) // lưu vnp_TxnRef
                .receipt(receipt)
                .build();

        paymentDetailRepository.save(payment);

        // Gửi receiptId + txnRef vào returnUrl
        String returnUrl = returnBaseUrl +
                "?receiptId=" + receiptId +
                "&txnRef=" + txnRef;

        // Trả về link thanh toán
        return createVNPayUrl(total, orderInfo, returnUrl, txnRef);
    }


    // Hàm tạo URL VNPay (dùng đúng mã txnRef)
    public String createVNPayUrl(int total, String orderInfo,
                                 String returnUrl, String txnRef) {

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Amount", String.valueOf(total * 100));
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", txnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");
        vnp_Params.put("vnp_Version", "2.1.0");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));

        cld.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);

            // ❗ Không encode hashData
            hashData.append(fieldName)
                    .append("=")
                    .append(fieldValue)
                    .append("&");

            // ✔ Encode UTF8 cho query

        }

        hashData.setLength(hashData.length() - 1);
        query.setLength(query.length() - 1);

        String secureHash = VNPayConfig.hmacSHA512(
                VNPayConfig.vnp_HashSecret,
                hashData.toString()
        );

        return VNPayConfig.vnp_PayUrl + "?" + query + "&vnp_SecureHash=" + secureHash;
    }


    // Xác minh return từ VNPay
    public int verifyReturn(Map<String, String> params) {

        String receivedHash = params.get("vnp_SecureHash");

        Map<String, String> fields = new HashMap<>(params);
        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        String calculatedHash = VNPayConfig.hashAllFields(fields);

        if (!calculatedHash.equals(receivedHash)) return -1; // Sai chữ ký

        return params.get("vnp_TransactionStatus").equals("00") ? 1 : 0;
    }

}
