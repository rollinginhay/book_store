package hn_152.bookstore.config.exceptionHanding;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
