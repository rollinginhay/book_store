package sd_009.bookstore.service.mail;

import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.entity.receipt.ReceiptDetail;

public class EmailBuilder {

    public static String buildOrderEmail(Receipt receipt, PaymentDetail paymentDetail) {

        StringBuilder sb = new StringBuilder();

        sb.append("<div style='font-family:Arial,sans-serif;background:#f7f7f7;padding:20px;'>");

        // ===== HEADER =====
        sb.append("<div style='max-width:650px;margin:auto;background:white;border-radius:12px;overflow:hidden;"
                + "box-shadow:0 4px 15px rgba(0,0,0,0.1)'>");

        sb.append("<div style=\"background:linear-gradient(135deg,#8b0000,#b30000,#cc0000);"
                + "padding:25px;text-align:center;color:white;\">"
                + "<h2 style='margin:0;font-size:24px;'>Thông báo xác nhận đơn hàng DinoBookStore!</h2>"
                + "</div>");

        // ===== GREETING =====
        sb.append("<div style='padding:25px;'>");
        sb.append("""
            <p style='font-size:14px; color:#333; margin:20px 0 6px 0;'>
                Kính gửi Quý khách 
            """);

                    sb.append("<strong style='color:#d60000;'>" + receipt.getCustomerName() + "</strong>");

                    sb.append("""
                ,
            </p>
            
            <p style='font-size:14px; color:#333; line-height:1.6; margin:0 0 20px 0;'>
                Cảm ơn bạn đã tin tưởng và mua sắm tại 
                <strong style='color:#d60000;'>DinoBookStore!</strong><br>
                Dưới đây là thông tin chi tiết về đơn hàng của bạn:
            </p>
            """);



        // ===== ORDER INFO =====

        sb.append("<p style='font-size:15px;color:#444;margin:0 0 10px 0;'>"
                + "Mã đơn hàng: <strong style='color:#d10000;'>#" + receipt.getId() + "</strong></p>");

        sb.append("<p style='font-size:15px;color:#444;margin:0 0 25px 0;'>"
                + "Thời gian đặt: <strong>"
                + receipt.getPaymentDate().toString().replace("T", " ")
                + "</strong></p>");

        // ===== CUSTOMER INFO =====
        sb.append("<div style='background:#fafafa;padding:20px;border-radius:8px;border:1px solid #eee;"
                + "margin-bottom:25px;'>");

        sb.append("<h3 style='margin:0 0 12px 0;font-size:17px;"
                + "padding-bottom:8px;border-bottom:1px solid #e5e5e5;'>Thông tin khách hàng</h3>");

        sb.append("<p style='margin:4px 0;font-size:14px;'><strong>Tên:</strong> "
                + receipt.getCustomerName() + "</p>");
        sb.append("<p style='margin:4px 0;font-size:14px;'><strong>SĐT:</strong> "
                + receipt.getCustomerPhone() + "</p>");
        sb.append("<p style='margin:4px 0;font-size:14px;'><strong>Địa chỉ:</strong> "
                + receipt.getCustomerAddress() + "</p>");

        sb.append("</div>");

        // ===== PRODUCT TABLE =====
        sb.append("<table width='100%' style='border-collapse:collapse;font-size:14px;'>");

        sb.append("<tr style='background:#f0f0f0;text-align:left;'>"
                + "<th style='padding:10px;border-bottom:1px solid #ddd;width:40px;'>STT</th>"
                + "<th style='padding:10px;border-bottom:1px solid #ddd;'>Sản phẩm</th>"
                + "<th style='padding:10px;border-bottom:1px solid #ddd;text-align:center;width:60px;'>SL</th>"
                + "<th style='padding:10px;border-bottom:1px solid #ddd;text-align:right;width:100px;'>Giá</th>"
                + "</tr>");

        int index = 1;
        for (ReceiptDetail d : receipt.getReceiptDetails()) {

            // Lấy tên sách từ BookDetail → Book
            String bookTitle = (d.getBookCopy() != null
                    && d.getBookCopy().getBook() != null)
                    ? d.getBookCopy().getBook().getTitle()
                    : "Sản phẩm không xác định";

            String productName = bookTitle + " — " + d.getBookCopy().getBookFormat();

            sb.append("<tr>"
                    + "<td style='padding:8px;border-bottom:1px solid #eee;text-align:center;'>" + index++ + "</td>"
                    + "<td style='padding:8px;border-bottom:1px solid #eee;'>"
                    + productName + "</td>"
                    + "<td style='padding:8px;border-bottom:1px solid #eee;text-align:center;'>"
                    + d.getQuantity() + "</td>"
                    + "<td style='padding:8px;border-bottom:1px solid #eee;text-align:right;'>"
                    + String.format("%,d₫", d.getPricePerUnit()) + "</td>"
                    + "</tr>");
        }



        sb.append("</table>");

        // ===== PAYMENT INFO =====
        sb.append("""
            <div style='background:#fafafa; padding:20px; border-radius:10px;
                        border:1px solid #eee; margin-top:25px;'>

                <h3 style='margin:0 0 12px 0; font-size:16px; color:#333;
                            padding-bottom:8px;border-bottom:1px solid #e5e5e5;'>
                    Thông tin thanh toán
                </h3>

                <p style='margin:6px 0;font-size:14px;'>
                    <strong>Tổng tiền hàng:</strong> """ + String.format("%,d₫", receipt.getSubTotal()) + """
                </p>

                <p style='margin:6px 0;font-size:14px;'>
                    <strong>Phí vận chuyển:</strong> """ + String.format("%,d₫", receipt.getServiceCost()) + """
                </p>

                <p style='margin:6px 0;font-size:14px;'>
                    <strong>Phương thức thanh toán:</strong> """ + paymentDetail.getPaymentType() + """
                </p>

                <p style='margin:10px 0;font-size:16px;color:#c40000;font-weight:bold;'>
                    Tổng thanh toán: """ + String.format("%,d₫", receipt.getGrandTotal()) + """
                </p>

            </div>
        """);

        // ===== NOTE =====
        sb.append("<div style='margin-top:30px;font-size:13px;color:#555;'>"
                + "<p><strong style='color:#cc0000;'>Lưu ý:</strong></p>"
                + "<ul style='padding-left:18px;margin-top:5px;color:#444;'>"
                + "<li>Nếu sản phẩm gặp vấn đề khi nhận hàng, bạn có thể trả hàng trong 3 ngày.</li>"
                + "<li>Đổi trả chỉ áp dụng với các sản phẩm có lỗi sản xuất, vận chuyển.</li>"
                + "<li>Vui lòng giữ bill khi có nhu cầu đổi trả sản phẩm.</li>"
                + "</ul>"
                + "</div>");

        // ===== BUTTON =====
        sb.append("<div style='text-align:center;margin-top:30px;'>"
                + "<a href='http://localhost:3000/order/" + receipt.getId() + "' "
                + "style='display:inline-block;padding:12px 22px;background:#cc0000;"
                + "color:white;text-decoration:none;border-radius:8px;font-weight:bold;'>"
                + "Xem chi tiết đơn hàng</a>"
                + "</div>");

        sb.append("<p style='text-align:center;margin-top:25px;color:#666;'>Cảm ơn bạn đã đặt hàng tại DinoBookStore❤️</p>");

        sb.append("</div></div>");

        return sb.toString();
    }

}
