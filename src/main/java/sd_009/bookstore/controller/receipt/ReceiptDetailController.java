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
import sd_009.bookstore.service.receipt.ReceiptDetailService;

@RestController
@RequiredArgsConstructor
@Tag(name = "ReceiptDetail crud")
public class ReceiptDetailController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final ReceiptDetailService receiptDetailService;

    @Operation(
            summary = "Get receiptDetail by id, with attached relationship",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get receiptDetail by id resp", externalValue = "/jsonExample/receiptDetail/get_receiptDetail.json"))))
    @GetMapping(Routes.GET_RECEIPT_DETAIL_BY_ID)
    public ResponseEntity<Object> getReceiptDetailById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(receiptDetailService.findById(id));
    }

    @Operation(
            summary = "Create a new receiptDetail",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create receiptDetail req", externalValue = "/jsonExample/receiptDetail/post_receiptDetail.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create receiptDetail resp", externalValue = "/jsonExample/receiptDetail/get_receiptDetail.json"))))
    @PostMapping(Routes.POST_RECEIPT_DETAIL_CREATE)
    public ResponseEntity<Object> createReceiptDetail(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(receiptDetailService.save(json));
    }

    @Operation(
            summary = "Update a receiptDetail",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create receiptDetail req", externalValue = "/jsonExample/receiptDetail/put_receiptDetail.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create receiptDetail resp", externalValue = "/jsonExample/receiptDetail/get_receiptDetail.json"))))
    @PutMapping(Routes.PUT_RECEIPT_DETAIL_UPDATE)
    public ResponseEntity<Object> updateReceiptDetail(@RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(receiptDetailService.update(json));
    }

    @Operation(description = "Delete a receiptDetail")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping(Routes.DELETE_RECEIPT_DETAIL_DELETE)
    public ResponseEntity<Object> deleteReceiptDetail(@PathVariable Long id) {
        receiptDetailService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }

}
