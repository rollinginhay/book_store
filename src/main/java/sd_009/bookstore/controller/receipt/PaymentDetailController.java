package sd_009.bookstore.controller.receipt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.service.receipt.PaymentDetailService;

@RestController
@RequiredArgsConstructor
@Tag(name = "paymentDetail crud")
public class PaymentDetailController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final PaymentDetailService paymentDetailService;

    @Operation(
            summary = "Get paymentDetail by id, with attached relationship",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get paymentDetail by id resp", externalValue = "/jsonExample/paymentDetail/get_paymentDetail.json"))))
    @GetMapping(Routes.GET_PAYMENT_DETAIL_BY_ID)
    public ResponseEntity<Object> getPaymentDetailById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(paymentDetailService.findById(id));
    }

    @Operation(
            summary = "Create a new paymentDetail",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create paymentDetail req", externalValue = "/jsonExample/paymentDetail/post_paymentDetail.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create paymentDetail resp", externalValue = "/jsonExample/paymentDetail/get_paymentDetail.json"))))
    @PostMapping(Routes.POST_PAYMENT_DETAIL_CREATE)
    public ResponseEntity<Object> createPaymentDetail(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(paymentDetailService.save(json));
    }

    @Operation(
            summary = "Update a paymentDetail",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create paymentDetail req", externalValue = "/jsonExample/paymentDetail/put_paymentDetail.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create paymentDetail resp", externalValue = "/jsonExample/paymentDetail/get_paymentDetail.json"))))
    @PutMapping(Routes.PUT_PAYMENT_DETAIL_UPDATE)
    public ResponseEntity<Object> updatePaymentDetail(@RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(paymentDetailService.update(json));
    }

    @Operation(description = "Delete a paymentDetail")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping(Routes.DELETE_PAYMENT_DETAIL_DELETE)
    public ResponseEntity<Object> deletePaymentDetail(@PathVariable Long id) {
        paymentDetailService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }

}
