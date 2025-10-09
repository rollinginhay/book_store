package sd_009.bookstore.config.exceptionHanding.exception;

public class IsDisabledException extends RuntimeException {
    public IsDisabledException(String message) {
        super("Element is currently disabled. Please reinstate");
    }
}
