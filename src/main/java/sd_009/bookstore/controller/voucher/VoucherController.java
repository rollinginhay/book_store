package sd_009.bookstore.controller.voucher;

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
import sd_009.bookstore.service.voucher.VoucherService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Voucher CRUD", description = "Quản lý phiếu giảm giá")
public class VoucherController {

    @Value("${config.jsonapi.contentType}")
    private String contentType;

    private final VoucherService voucherService;

    // 🔹 Lấy tất cả voucher
    @Operation(
            summary = "Get all vouchers",
            description = "Lấy danh sách tất cả các phiếu giảm giá.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(examples = @ExampleObject(
                            name = "Get vouchers resp",
                            externalValue = "/jsonExample/voucher/get_vouchers.json"
                    ))
            )
    )
    @GetMapping(Routes.GET_VOUCHERS)
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(voucherService.findAll());
    }

    // 🔹 Lấy voucher theo ID
    @Operation(
            summary = "Get voucher by ID",
            description = "Lấy thông tin chi tiết 1 phiếu giảm giá bằng ID.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(examples = @ExampleObject(
                            name = "Get voucher by id resp",
                            externalValue = "/jsonExample/voucher/get_voucher.json"
                    ))
            )
    )
    @GetMapping(Routes.GET_VOUCHER_BY_ID)
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(voucherService.findById(id));
    }

    // 🔹 Tạo mới voucher
    @Operation(
            summary = "Create voucher",
            description = "Tạo mới 1 phiếu giảm giá.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(
                            name = "Create voucher req",
                            externalValue = "/jsonExample/voucher/post_voucher.json"
                    ))
            ),
            responses = @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(examples = @ExampleObject(
                            name = "Create voucher resp",
                            externalValue = "/jsonExample/voucher/get_voucher.json"
                    ))
            )
    )
    @PostMapping(Routes.POST_VOUCHER_CREATE)
    public ResponseEntity<Object> create(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.valueOf(contentType))
                .body(voucherService.save(json));
    }

    // 🔹 Cập nhật voucher
    @Operation(
            summary = "Update voucher",
            description = "Cập nhật thông tin phiếu giảm giá.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(
                            name = "Update voucher req",
                            externalValue = "/jsonExample/voucher/put_voucher.json"
                    ))
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Updated",
                    content = @Content(examples = @ExampleObject(
                            name = "Update voucher resp",
                            externalValue = "/jsonExample/voucher/get_voucher.json"
                    ))
            )
    )
    @PutMapping(Routes.PUT_VOUCHER_UPDATE)
    public ResponseEntity<Object> update(@RequestBody String json) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(voucherService.update(json));
    }

    // 🔹 Xóa mềm voucher
    @Operation(
            summary = "Soft delete voucher",
            description = "Xóa mềm 1 phiếu giảm giá (enabled=false)."
    )
    @DeleteMapping(Routes.DELETE_VOUCHER_DELETE)
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        voucherService.delete(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(null);
    }
}

