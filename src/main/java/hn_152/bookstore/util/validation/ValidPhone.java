package hn_152.bookstore.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface ValidPhone {
    String message() default "Invalid Phone Number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
