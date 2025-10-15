package sd_009.bookstore.dto.jsonApiResource.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import sd_009.bookstore.util.validation.annotation.ValidPassword;

public record LoginRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Password is required")
        @ValidPassword
        String password
) {
}
