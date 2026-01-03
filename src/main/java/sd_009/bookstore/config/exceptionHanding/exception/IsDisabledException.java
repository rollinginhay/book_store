package sd_009.bookstore.config.exceptionHanding.exception;

public class IsDisabledException extends RuntimeException {
    public IsDisabledException(String message) {
        super(message != null && !message.isEmpty() ? message : "Phần tử đang bị vô hiệu hóa. Vui lòng kích hoạt lại");
    }
}
