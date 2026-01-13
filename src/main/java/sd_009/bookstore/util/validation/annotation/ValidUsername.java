package sd_009.bookstore.util.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sd_009.bookstore.util.validation.implementation.ValidUsernameValidator;

import java.lang.annotation.*;

/**
 * Validates username format:
 * - 3-50 characters
 * - Letters, numbers, underscores only
 * - Must start with a letter
 * - Cannot end with underscore
 */
@Documented
@Constraint(validatedBy = ValidUsernameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    String message() default "Tên đăng nhập không hợp lệ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}