package sd_009.bookstore.controller.cart;

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
import sd_009.bookstore.service.cart.CartDetailService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Cart Detail CRUD", description = "Quản lý chi tiết giỏ hàng của người dùng")
public class CartDetailController {

    @Value("${config.jsonapi.contentType}")
    private String contentType;

    private final CartDetailService cartDetailService;

    // 🔹 Lấy toàn bộ cart detail theo user id
    @Operation(
            summary = "Get all cart details by user id",
            description = "Lấy toàn bộ sản phẩm có trong giỏ của 1 user cụ thể.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(examples = @ExampleObject(
                            name = "Get cart details by user id resp",
                            externalValue = "/jsonExample/cart/get_cart_details.json"
                    ))
            )
    )
    @GetMapping(Routes.GET_ALL_CART_DETAIL_BY_USER_ID)
    public ResponseEntity<Object> getCartByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(cartDetailService.findByUserId(userId));
    }

    // 🔹 Lấy 1 cart detail (bản thường)
    @Operation(
            summary = "Get cart detail by id",
            description = "Lấy chi tiết 1 dòng trong giỏ hàng theo ID.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(examples = {
                            @ExampleObject(
                                    name = "Get cart detail by id resp",
                                    externalValue = "/jsonExample/cart/get_cart_detail.json"
                            ),
                            @ExampleObject(
                                    name = "Get cart detail owning resp",
                                    externalValue = "/jsonExample/cart/get_cart_detail_owning.json"
                            )
                    })
            )
    )
    @GetMapping(Routes.GET_CART_DETAIL_BY_ID)
    public ResponseEntity<Object> getCartDetailById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(cartDetailService.findById(id));
    }

    // 🔹 Lấy 1 cart detail (bản owning, kèm user + bookDetail)
    @Operation(
            summary = "Get cart detail owning by id",
            description = "Lấy chi tiết 1 dòng trong giỏ hàng và bao gồm luôn thông tin user + bookDetail.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(examples = @ExampleObject(
                            name = "Get cart detail owning resp",
                            externalValue = "/jsonExample/cart/get_cart_detail_owning.json"
                    ))
            )
    )
    @GetMapping("/v1/cartDetail/{id}/owning")
    public ResponseEntity<Object> getCartDetailOwningById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(cartDetailService.findOwningById(id));
    }

    // 🔹 Thêm sản phẩm vào giỏ hàng
    @Operation(
            summary = "Create a new cart detail",
            description = "Thêm sản phẩm mới vào giỏ hàng.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(
                            name = "Create cart detail req",
                            externalValue = "/jsonExample/cart/post_cart_detail.json"
                    ))
            ),
            responses = @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(examples = @ExampleObject(
                            name = "Create cart detail resp",
                            externalValue = "/jsonExample/cart/get_cart_detail.json"
                    ))
            )
    )
    @PostMapping(Routes.POST_CART_DETAIL_CREATE)
    public ResponseEntity<Object> createCartDetail(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.valueOf(contentType))
                .body(cartDetailService.save(json));
    }

    // 🔹 Cập nhật sản phẩm trong giỏ hàng
    @Operation(
            summary = "Update a cart detail",
            description = "Cập nhật thông tin 1 dòng trong giỏ hàng (ví dụ: thay đổi số lượng).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(
                            name = "Update cart detail req",
                            externalValue = "/jsonExample/cart/put_cart_detail.json"
                    ))
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Updated",
                    content = @Content(examples = @ExampleObject(
                            name = "Update cart detail resp",
                            externalValue = "/jsonExample/cart/get_cart_detail.json"
                    ))
            )
    )
    @PutMapping(Routes.PUT_CART_DETAIL_UPDATE)
    public ResponseEntity<Object> updateCartDetail(@RequestBody String json) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(cartDetailService.update(json));
    }

    // 🔹 Xoá mềm (soft delete)
    @Operation(
            summary = "Delete a cart detail (soft delete)",
            description = "Xoá mềm (set enabled=false) 1 sản phẩm khỏi giỏ hàng."
    )
    @ApiResponse(responseCode = "200", description = "Deleted successfully")
    @DeleteMapping(Routes.DELETE_CART_DETAIL_DELETE)
    public ResponseEntity<Object> deleteCartDetail(@PathVariable Long id) {
        cartDetailService.delete(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(null);
    }
}
