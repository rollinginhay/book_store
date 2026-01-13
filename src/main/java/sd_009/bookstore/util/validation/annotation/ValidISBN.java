package sd_009.bookstore.util.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sd_009.bookstore.util.validation.implementation.ValidISBNValidator;

import java.lang.annotation.*;

/**
 * Validates ISBN (International Standard Book Number):
 * - ISBN-10: 10 digits (last can be X or x)
 * - ISBN-13: 13 digits (any prefix accepted)
 * - Accepts 8-17 characters for edge cases and non-standard ISBNs
 * - Automatically removes hyphens, spaces, dots, underscores
 * - No strict checksum validation (permissive mode)
 */
@Documented
@Constraint(validatedBy = ValidISBNValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidISBN {
    String message() default "ISBN không hợp lệ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}