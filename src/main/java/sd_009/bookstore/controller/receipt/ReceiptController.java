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
import sd_009.bookstore.dto.jsonApiResource.receipt.UpdateReceiptStatusRequest;
import sd_009.bookstore.dto.jsonApiResource.receipt.UpdateRefundInfoRequest;
import sd_009.bookstore.service.receipt.ReceiptService;

import java.util.List;
import java.util.Map;

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
                                              @RequestParam(required = false, defaultValue = "") List<String> sort) {
        if (keyword == null) {
            keyword = "";
        }

        Sort sortInstance = Sort.unsorted();

        if (sort != null && !sort.isEmpty()) {
            for (String query : sort) {
                String[] queries = query.split(";");
                String field = queries[0];
                String order = queries.length > 1 ? queries[1] : "asc";

                sortInstance = order.equalsIgnoreCase("asc")
                        ? sortInstance.and(Sort.by(field))
                        : sortInstance.and(Sort.by(field).descending());
            }
        } else {
            sortInstance = Sort.by("updatedAt").descending();
        }

        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(receiptService.find(enabled, keyword, PageRequest.of(page, limit).withSort(sortInstance)));
    }

    @Operation(
            summary = "Get receipts for list view (FE)",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Get receipts resp",
                                    externalValue = "/jsonExample/receipt/get_receipts.json"
                            )
                    )
            )
    )

    // Load trang receipt admin
    @GetMapping(Routes.GET_RECEIPTS_LIST)
    public ResponseEntity<Object> getReceiptsForList(
            @RequestParam(name = "e", required = false) Boolean enabled,
            @RequestParam int page,
            @RequestParam int limit,
            @RequestParam(required = false) List<String> sort
    ) {

        Sort sortInstance = Sort.unsorted();
        if (sort != null) {
            for (String query : sort) {
                String[] queries = query.split(";");
                if (queries.length < 2) continue;

                String field = queries[0];
                String order = queries[1];

                sortInstance = "asc".equalsIgnoreCase(order)
                        ? sortInstance.and(Sort.by(field))
                        : sortInstance.and(Sort.by(field).descending());
            }
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(
                        receiptService.findForList(
                                enabled == null ? true : enabled,
                                PageRequest.of(page, limit).withSort(sortInstance)
                        )
                );
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
            summary = "Create a new receipt",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Create receipt req",
                                    externalValue = "/jsonExample/receipt/post_receipt.json"
                            )
                    )
            ),
            responses = @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Create receipt resp",
                                    externalValue = "/jsonExample/receipt/get_receipt.json"
                            )
                    )
            )
    )
    @PostMapping(Routes.POST_RECEIPT_CREATE_ONLINE)
    public ResponseEntity<Object> createReceiptOnline(@RequestBody String json) {
        String responseJson = receiptService.saveOneline(json);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.valueOf(contentType))
                .body(responseJson);
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
                    @ExampleObject(name = "Attach paymentDetail req", externalValue = "/jsonExample/paymentDetail/get_paymentDetail.json"),
                    @ExampleObject(name = "Attach receiptDetail req", externalValue = "/jsonExample/receiptDetail/get_receiptDetail.json")
            })),
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get receipt by id resp", externalValue = "/jsonExample/receipt/get_receipt.json"))))
    @PostMapping(Routes.MULTI_RECEIPT_RELATIONSHIP_GENERIC)
    public ResponseEntity<Object> attachRelationship(@PathVariable(name = "id") Long id, @PathVariable(name = "dependent") String dependent, @RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(receiptService.attachOrReplaceRelationship(id, json, dependent));
    }

    @Operation(
            summary = "Detach relationship from receipt",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                    @ExampleObject(name = "Detach paymentDetail req", externalValue = "/jsonExample/paymentDetail/get_paymentDetail.json"),
                    @ExampleObject(name = "Detach receiptDetail req", externalValue = "/jsonExample/receiptDetail/get_receiptDetail.json")
            })),
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get receipt by id resp", externalValue = "/jsonExample/receipt/get_receipt.json"))))
    @DeleteMapping(Routes.MULTI_RECEIPT_RELATIONSHIP_GENERIC)
    public ResponseEntity<Object> detachRelationship(@PathVariable(name = "id") Long id, @PathVariable(name = "dependent") String dependent, @RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(receiptService.detachRelationShip(id, json, dependent));
    }


    @Operation(summary = "Update order status (Admin)")
    @PatchMapping("/v1/receipt/{id}/status")
    public ResponseEntity<Object> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateReceiptStatusRequest req
    ) {
        receiptService.updateOrderStatus(id, req.orderStatus());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "success", true,
                        "message", "Cập nhật trạng thái thành công"
                ));

    }

    @Operation(summary = "Get receipt history (timeline)")
    @GetMapping("/v1/receipt/{id}/history")
    public ResponseEntity<Object> getReceiptHistory(@PathVariable Long id) {
        List<sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptHistoryDto> history = receiptService.getReceiptHistoryDto(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(history);
    }

    @Operation(summary = "Update refund information (Customer)")
    @PatchMapping("/v1/receipt/{id}/refund-info")
    public ResponseEntity<Object> updateRefundInfo(
            @PathVariable Long id,
            @RequestBody UpdateRefundInfoRequest req
    ) {
        receiptService.updateRefundInfo(id, req.refundBankAccount(), req.refundBankName(), req.refundAccountHolder());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "success", true,
                        "message", "Cập nhật thông tin hoàn tiền thành công"
                ));
    }

}
