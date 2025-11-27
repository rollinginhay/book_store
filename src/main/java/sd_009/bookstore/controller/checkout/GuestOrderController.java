package sd_009.bookstore.controller.checkout;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.dto.jsonApiResource.checkout.GuestOrderRequest;
import sd_009.bookstore.dto.jsonApiResource.checkout.GuestOrderResponse;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.service.receipt.GuestOrderService;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class GuestOrderController {

    private final GuestOrderService guestOrderService;

    @PostMapping("/guest")
    public GuestOrderResponse createOrder(@RequestBody GuestOrderRequest req) {
        Receipt receipt = guestOrderService.createGuestOrder(req);

        return new GuestOrderResponse(
                receipt.getId(),
                receipt.getGrandTotal() != null ? receipt.getGrandTotal().longValue() : null,
                "Order created successfully"
        );

    }

}