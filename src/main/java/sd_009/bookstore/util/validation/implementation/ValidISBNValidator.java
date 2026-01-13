package sd_009.bookstore.util.validation.implementation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sd_009.bookstore.util.validation.annotation.ValidISBN;

import java.util.regex.Pattern;

public class ValidISBNValidator implements ConstraintValidator<ValidISBN, String> {

    // More permissive patterns - just check digit count and format
    private static final Pattern ISBN10_PATTERN = Pattern.compile("^[0-9]{9}[0-9Xx]$");
    private static final Pattern ISBN13_PATTERN = Pattern.compile("^[0-9]{13}$");

    @Override
    public void initialize(ValidISBN constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        if (isbn == null || isbn.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "ISBN là bắt buộc"
            ).addConstraintViolation();
            return false;
        }

        // Remove hyphens, spaces, and other common separators
        String cleanIsbn = isbn.replaceAll("[\\s\\-_.]", "");

        // Accept 10 or 13 digit ISBNs
        if (cleanIsbn.length() == 10) {
            return validateISBN10Format(cleanIsbn, context);
        } else if (cleanIsbn.length() == 13) {
            return validateISBN13Format(cleanIsbn, context);
        } else if (cleanIsbn.length() >= 8 && cleanIsbn.length() <= 17) {
            // Very permissive: accept anything between 8-17 characters
            // This handles edge cases and non-standard ISBNs
            return validateGenericFormat(cleanIsbn, context);
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "ISBN phải có từ 8 đến 17 ký tự"
            ).addConstraintViolation();
            return false;
        }
    }

    private boolean validateISBN10Format(String isbn, ConstraintValidatorContext context) {
        // Just check format, no checksum validation
        if (!ISBN10_PATTERN.matcher(isbn).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "ISBN-10 phải có 10 ký tự (9 chữ số và 1 chữ số hoặc chữ X)"
            ).addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateISBN13Format(String isbn, ConstraintValidatorContext context) {
        // Just check format, no strict prefix or checksum validation
        if (!ISBN13_PATTERN.matcher(isbn).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "ISBN-13 phải có 13 chữ số"
            ).addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateGenericFormat(String isbn, ConstraintValidatorContext context) {
        // Very permissive: accept any alphanumeric string within length range
        // This handles non-standard ISBNs, older formats, or regional variations
        if (!isbn.matches("^[0-9Xx]+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "ISBN chỉ được chứa chữ số và chữ X"
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}