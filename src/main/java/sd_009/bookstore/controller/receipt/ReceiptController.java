package sd_009.bookstore.controller.receipt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.service.receipt.ReceiptService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Receipt crud")
public class ReceiptController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final ReceiptService receiptService;

    @Operation(
            summary = "Get receipts by query",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get receipts resp", externalValue = "/jsonExample/receipt/get_receipts.json"))))
    @GetMapping(Routes.GET_RECEIPTS)
    public ResponseEntity<Object> getReceipts(@RequestParam(required = false, name = "q") String keyword,
                                              @RequestParam(name = "e") Boolean enabled,
                                              @RequestParam int page,
                                              @RequestParam int limit,
                                              @RequestParam(required = false) List<String> sort) {
        if (keyword == null) {
            keyword = "";
        }

        Sort sortInstance = Sort.unsorted();

        for (String query : sort) {
            String[] queries = query.split(";");
            String field = queries[0];
            String order = queries[1];

            if (order.equals("asc")) {
                sortInstance = sortInstance.and(Sort.by(field));
            } else {
                sortInstance = sortInstance.and(Sort.by(field).descending());
            }

        }
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(receiptService.find(enabled, keyword, PageRequest.of(page, limit).withSort(sortInstance)));
    }

    @Operation(
            summary = "Get receipt by id, with attached relationship",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get receipt by id resp", externalValue = "/jsonExample/receipt/get_receipt.json"))))
    @GetMapping(Routes.GET_RECEIPT_BY_ID)
    public ResponseEntity<Object> getReceiptById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(receiptService.findById(id));
    }

    @Operation(
            summary = "Create a new receipt",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create receipt req", externalValue = "/jsonExample/receipt/post_receipt.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create receipt resp", externalValue = "/jsonExample/receipt/get_receipt.json"))))
    @PostMapping(Routes.POST_RECEIPT_CREATE)
    public ResponseEntity<Object> createReceipt(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(receiptService.save(json));
    }

    @Operation(
            summary = "Update a receipt",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create receipt req", externalValue = "/jsonExample/receipt/put_receipt.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create receipt resp", externalValue = "/jsonExample/receipt/get_receipt.json"))))
    @PutMapping(Routes.PUT_RECEIPT_UPDATE)
    public ResponseEntity<Object> updateReceipt(@RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(receiptService.update(json));
    }

    @Operation(description = "Delete a receipt")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping(Routes.DELETE_RECEIPT_DELETE)
    public ResponseEntity<Object> deleteReceipt(@PathVariable Long id) {
        receiptService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }

    @Operation(
            summary = "Get receiptDetails of receipt",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get receiptDetails of receipt", externalValue = "/jsonExample/receiptDetail/get_receiptDetails.json"))))
    @GetMapping(Routes.MULTI_RECEIPT_RELATIONSHIP_RECEIPT_DETAIL)
    public ResponseEntity<Object> getReceiptDetailsByReceipt(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(receiptService.getDependents(id, "receiptDetail"));
    }

    @Operation(
            summary = "Get paymentDetails of receipt",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get paymentDetails of receipt", externalValue = "/jsonExample/paymentDetail/get_paymentDetails.json"))))
    @GetMapping(Routes.MULTI_RECEIPT_RELATIONSHIP_PAYMENT_DETAIL)
    public ResponseEntity<Object> getPaymentDetailsByReceipt(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(receiptService.getDependents(id, "paymentDetail"));
    }

    @Operation(
            summary = "Attach relationship to receipt",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                    @ExampleObject(name = "Attach genre req", externalValue = "/jsonExample/genre/get_genre.json"),
                    @ExampleObject(name = "Attach receiptDetail", externalValue = "/jsonExample/receiptDetail/get_receiptDetail.json")
            })),
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get receipt by id resp", externalValue = "/jsonExample/receipt/get_receipt.json"))))
    @PostMapping(Routes.MULTI_RECEIPT_RELATIONSHIP_GENERIC)
    public ResponseEntity<Object> attachRelationship(@PathVariable(name = "id") Long id, @PathVariable(name = "dependent") String dependent, @RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(receiptService.attachOrReplaceRelationship(id, json, dependent));
    }

    @Operation(
            summary = "Detach relationship from receipt",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                    @ExampleObject(name = "Detach genre req", externalValue = "/jsonExample/genre/get_genre.json"),
                    @ExampleObject(name = "Detach receiptDetail", externalValue = "/jsonExample/receiptDetail/get_receiptDetail.json")
            })),
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get receipt by id resp", externalValue = "/jsonExample/receipt/get_receipt.json"))))
    @DeleteMapping(Routes.MULTI_RECEIPT_RELATIONSHIP_GENERIC)
    public ResponseEntity<Object> detachRelationship(@PathVariable(name = "id") Long id, @PathVariable(name = "dependent") String dependent, @RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(receiptService.detachRelationShip(id, json, dependent));
    }

}
