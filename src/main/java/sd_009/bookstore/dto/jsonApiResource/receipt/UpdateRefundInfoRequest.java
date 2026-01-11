package sd_009.bookstore.dto.jsonApiResource.receipt;

public record UpdateRefundInfoRequest(
        String refundBankAccount,    // STK hoàn tiền
        String refundBankName,       // Tên ngân hàng
        String refundAccountHolder   // Tên chủ tài khoản
) {}
