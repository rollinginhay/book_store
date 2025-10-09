package sd_009.bookstore.config.exceptionHanding.exception;

public class DependencyConflictException extends RuntimeException {
    public DependencyConflictException(String message) {
        super("Element has unhandled dependencies");
    }
}
