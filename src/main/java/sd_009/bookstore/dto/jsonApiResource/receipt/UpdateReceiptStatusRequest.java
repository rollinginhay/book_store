package sd_009.bookstore.dto.jsonApiResource.receipt;

import sd_009.bookstore.entity.receipt.OrderStatus;

public record UpdateReceiptStatusRequest(
        OrderStatus orderStatus
) {}