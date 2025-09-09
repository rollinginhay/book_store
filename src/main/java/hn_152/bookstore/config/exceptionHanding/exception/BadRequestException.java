package hn_152.bookstore.config.exceptionHanding.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
