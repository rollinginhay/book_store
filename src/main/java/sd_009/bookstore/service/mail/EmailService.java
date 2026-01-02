package sd_009.bookstore.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import sd_009.bookstore.entity.receipt.Receipt;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOrderEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            // FROM — tên hiển thị m muốn
            helper.setFrom(
                    "auduongthientuyetx2@gmail.com",
                    "Dino Bookstore"
            );


            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error sending email: " + e.getMessage());
        }

    }
    public void sendOrderStatusEmail(
            String to,
            Receipt receipt,
            String oldStatus,
            String newStatus
    ) {
        String subject = "Đơn hàng #" + receipt.getId() + " đã được cập nhật";
        String html = EmailBuilder.buildOrderStatusEmail(
                receipt,
                oldStatus,
                newStatus
        );

        sendOrderEmail(to, subject, html);
    }

}
