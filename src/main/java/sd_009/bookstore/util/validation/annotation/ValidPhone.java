package sd_009.bookstore.util.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sd_009.bookstore.util.validation.implementation.PhoneNumberConstraintValidator;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberConstraintValidator.class)
public @interface ValidPhone {
    String message() default "Invalid Phone Number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
