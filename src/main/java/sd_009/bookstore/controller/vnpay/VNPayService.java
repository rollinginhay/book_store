package sd_009.bookstore.controller.vnpay;

import org.springframework.stereotype.Service;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.repository.PaymentDetailRepository;
import sd_009.bookstore.repository.ReceiptRepository;

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

    /**
     * Tạo URL thanh toán VNPay cho receiptId và returnUrl
     */
    public String createPaymentForReceipt(Long receiptId, String returnBaseUrl) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));

        int total = receipt.getGrandTotal().intValue();
        String orderInfo = "Thanh toan don hang " + receipt.getId();

        // Tạo mã giao dịch VNPay (vnp_TxnRef)
        String txnRef = VNPayConfig.getRandomNumber(8);

        // Gửi receiptId + txnRef vào returnUrl
        String returnUrl = returnBaseUrl +
                "?receiptId=" + receiptId +
                "&txnRef=" + txnRef;

        return createVNPayUrl(total, orderInfo, returnUrl, txnRef);
    }

    /**
     * Build URL VNPay
     */
    public String createVNPayUrl(int total, String orderInfo,
                                 String returnUrl, String txnRef) {

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Amount", String.valueOf(total * 100)); // VNPay tính bằng xu
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", txnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        vnp_Params.put("vnp_Command", "pay");
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
            if (fieldValue != null && !fieldValue.isEmpty()) {

                // HASH DATA PHẢI URL ENCODE VALUE
                hashData.append(fieldName)
                        .append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8))
                        .append("&");

                // QUERY
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8))
                        .append("&");
            }
        }



        if (hashData.length() > 0) hashData.setLength(hashData.length() - 1);
        if (query.length() > 0) query.setLength(query.length() - 1);

        String secureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        System.out.println("===== VNPAY DEBUG =====");
        System.out.println("vnp_Amount      = " + vnp_Params.get("vnp_Amount"));
        System.out.println("vnp_TxnRef      = " + vnp_Params.get("vnp_TxnRef"));
        System.out.println("vnp_OrderInfo   = " + vnp_Params.get("vnp_OrderInfo"));
        System.out.println("vnp_ReturnUrl   = " + vnp_Params.get("vnp_ReturnUrl"));
        System.out.println("vnp_CreateDate  = " + vnp_Params.get("vnp_CreateDate"));
        System.out.println("-----------------------");
        System.out.println("HASH DATA       = " + hashData.toString());
        System.out.println("SECURE HASH     = " + secureHash);
        System.out.println("-----------------------");
        System.out.println("PAYMENT URL     = " +
                VNPayConfig.vnp_PayUrl + "?" + query + "&vnp_SecureHash=" + secureHash);
        System.out.println("=======================");
        return VNPayConfig.vnp_PayUrl + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    /**
     * Xác thực dữ liệu trả về từ VNPay
     */

    public int verifyReturn(Map<String, String> params) {
        String receivedHash = params.get("vnp_SecureHash");

        Map<String, String> fields = new HashMap<>(params);
        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        // ❗ BẮT BUỘC: CHỈ GIỮ vnp_*
        fields.entrySet().removeIf(
                entry -> !entry.getKey().startsWith("vnp_")
        );

        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (String key : fieldNames) {
            String value = fields.get(key);
            if (value != null && !value.isEmpty()) {
                hashData.append(key)
                        .append("=")
                        .append(URLEncoder.encode(value, StandardCharsets.UTF_8))
                        .append("&");
            }
        }
        if (hashData.length() > 0) hashData.setLength(hashData.length() - 1);

        String calculatedHash =
                VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());

        System.out.println("HASH DATA VERIFY = " + hashData);
        System.out.println("RECEIVED HASH   = " + receivedHash);
        System.out.println("CALCULATED HASH = " + calculatedHash);

        if (!calculatedHash.equals(receivedHash)) {
            return -1;
        }

        return "00".equals(params.get("vnp_TransactionStatus")) ? 1 : 0;
    }

}
