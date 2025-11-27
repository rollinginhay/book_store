package sd_009.bookstore.controller.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.service.mail.EmailService;

@RestController
@RequestMapping("/v1/test-email")
@RequiredArgsConstructor
public class MailTestController {

    private final EmailService emailService;

    @GetMapping
    public String sendTest() {
        emailService.sendOrderEmail(
                "test@example.com",
                "Test Email from Mailtrap",
                "<h1>Mailtrap hoạt động rồi nhaaaa!</h1>"
        );
        return "OK";
    }
}
