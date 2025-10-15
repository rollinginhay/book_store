package sd_009.bookstore.util.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sd_009.bookstore.util.validation.implementation.PasswordConstraintValidator;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface ValidPassword {
    String message() default "Invalid Password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
