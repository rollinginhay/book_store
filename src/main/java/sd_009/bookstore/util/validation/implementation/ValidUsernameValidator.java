package sd_009.bookstore.util.validation.implementation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sd_009.bookstore.util.validation.annotation.ValidUsername;

import java.util.regex.Pattern;

public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {

    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[a-zA-Z][a-zA-Z0-9_]{2,48}[a-zA-Z0-9]$"
    );

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.isBlank()) {
            return false;
        }

        // Check length (3-50 characters)
        if (username.length() < 3 || username.length() > 50) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Tên đăng nhập phải từ 3 đến 50 ký tự"
            ).addConstraintViolation();
            return false;
        }

        // Check pattern: starts with letter, ends with letter or number
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Tên đăng nhập phải bắt đầu bằng chữ cái và chỉ chứa chữ cái, số, gạch dưới"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}