package sd_009.bookstore.config.exceptionHanding.exception;

public class DependencyConflictException extends RuntimeException {
    public DependencyConflictException(String message) {
        super(message != null && !message.isEmpty() ? message : "Phần tử có các phụ thuộc chưa được xử lý");
    }
}
