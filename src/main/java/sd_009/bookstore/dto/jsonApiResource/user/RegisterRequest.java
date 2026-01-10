package sd_009.bookstore.dto.jsonApiResource.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import sd_009.bookstore.util.validation.annotation.ValidPassword;
import sd_009.bookstore.util.validation.annotation.ValidPhone;

public record RegisterRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Password is required")
        @ValidPassword(message = "Password must contain at least 8 characters, an uppercase character, a number, and a special character")
        String password,

        @ValidPhone(message = "Invalid phone number")
        String phoneNumber
) {

                }
