package sd_009.bookstore.util.validation.implementation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sd_009.bookstore.util.validation.annotation.ValidBookTitle;

import java.util.regex.Pattern;

public class ValidBookTitleValidator implements ConstraintValidator<ValidBookTitle, String> {

    // Disallow HTML/XML tags and dangerous characters
    private static final Pattern FORBIDDEN_CHARS = Pattern.compile("[<>{}\\[\\]\\\\|]");
    private static final Pattern MULTIPLE_SPACES = Pattern.compile("\\s{2,}");

    @Override
    public void initialize(ValidBookTitle constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        if (title == null || title.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Tiêu đề sách là bắt buộc"
            ).addConstraintViolation();
            return false;
        }

        // Check length
        if (title.length() < 1 || title.length() > 500) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Tiêu đề sách phải từ 1 đến 500 ký tự"
            ).addConstraintViolation();
            return false;
        }

        // Check for leading/trailing whitespace
        if (!title.equals(title.trim())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Tiêu đề sách không được có khoảng trắng ở đầu hoặc cuối"
            ).addConstraintViolation();
            return false;
        }

        // Check for multiple consecutive spaces
        if (MULTIPLE_SPACES.matcher(title).find()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Tiêu đề sách không được có nhiều khoảng trắng liên tiếp"
            ).addConstraintViolation();
            return false;
        }

        // Check for forbidden characters
        if (FORBIDDEN_CHARS.matcher(title).find()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Tiêu đề sách không được chứa ký tự đặc biệt như <, >, {, }, [, ], \\, |"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}