package sd_009.bookstore.entity.receipt;

public enum OrderStatus {

    PENDING("Chờ xác nhận"),
    AUTHORIZED("Đã xác nhận"),
    IN_TRANSIT("Đang vận chuyển"),
    PAID("Đã hoàn thành"),
    FAILED("Giao thất bại"),
    REFUNDED("Đã hoàn tiền"),
    CANCELLED("Đã hủy"),
    WAITING_REFUND_INFO("Chờ thông tin hoàn tiền");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
