package sd_009.bookstore.util.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sd_009.bookstore.util.validation.implementation.ValidBookTitleValidator;

import java.lang.annotation.*;

/**
 * Validates book title:
 * - 1-500 characters
 * - Cannot be only whitespace
 * - No leading/trailing whitespace
 * - No multiple consecutive spaces
 * - Cannot contain special characters like <, >, {, }, etc.
 */
@Documented
@Constraint(validatedBy = ValidBookTitleValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBookTitle {
    String message() default "Tiêu đề sách không hợp lệ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}