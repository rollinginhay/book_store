package sd_009.bookstore.config.exceptionHanding.exception;

public class DuplicateElementException extends RuntimeException {
    public DuplicateElementException(String message) {
        super(message != null && !message.isEmpty() ? message : "Phần tử đã tồn tại");
    }
}
