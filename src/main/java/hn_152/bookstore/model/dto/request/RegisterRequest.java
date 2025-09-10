package hn_152.bookstore.model.dto.request;

import hn_152.bookstore.util.validation.ValidPassword;
import hn_152.bookstore.util.validation.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Password is required")
        @ValidPassword(message = "Password must contain at least 8 characters, an uppercase character, a number, and a special character")
        String password,

        @NotBlank(message = "Phone number is required")
        @ValidPhone(message = "Invalid phone number")
        String phoneNumber
) {
}
