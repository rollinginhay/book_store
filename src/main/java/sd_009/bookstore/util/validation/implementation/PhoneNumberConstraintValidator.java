package sd_009.bookstore.util.validation.implementation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sd_009.bookstore.util.validation.annotation.ValidPhone;

import java.util.regex.Pattern;

public class PhoneNumberConstraintValidator implements ConstraintValidator<ValidPhone, String> {
    private final String phonePattern = "^(032|033|034|035|036|037|038|039|096|097|098|086|083|084|085|081|082|088|091|094|070|079|077|076|078|090|093|089|056|058|092|059|099)[0-9]{7}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Cho phép null hoặc empty (optional field)
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return Pattern.compile(phonePattern).matcher(value).matches();
    }
}
