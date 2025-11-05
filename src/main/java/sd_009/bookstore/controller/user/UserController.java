package sd_009.bookstore.controller.user;

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
import sd_009.bookstore.service.user.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "User CRUD", description = "Quản lý người dùng trong hệ thống")
public class UserController {

    @Value("${config.jsonapi.contentType}")
    private String contentType;

    private final UserService userService;

    // 🔹 Lấy toàn bộ user
    @Operation(
            summary = "Get all users",
            description = "Lấy danh sách tất cả người dùng.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(examples = @ExampleObject(
                            name = "Get all users resp",
                            externalValue = "/jsonExample/user/get_users.json"
                    ))
            )
    )
    @GetMapping(Routes.GET_USERS)
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(userService.findAll());
    }

    // 🔹 Lấy user theo ID
    @Operation(
            summary = "Get user by id",
            description = "Lấy thông tin chi tiết của 1 user theo ID.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(examples = @ExampleObject(
                            name = "Get user by id resp",
                            externalValue = "/jsonExample/user/get_user.json"
                    ))
            )
    )
    @GetMapping(Routes.GET_USER_BY_ID)
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(userService.findById(id));
    }

    // 🔹 Tạo user mới
    @Operation(
            summary = "Create a new user",
            description = "Tạo mới 1 người dùng trong hệ thống.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(
                            name = "Create user req",
                            externalValue = "/jsonExample/user/post_user.json"
                    ))
            ),
            responses = @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(examples = @ExampleObject(
                            name = "Create user resp",
                            externalValue = "/jsonExample/user/get_user_by_id.json"
                    ))
            )
    )
    @PostMapping(Routes.POST_USER_CREATE)
    public ResponseEntity<Object> createUser(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.valueOf(contentType))
                .body(userService.save(json));
    }

    // 🔹 Cập nhật user
    @Operation(
            summary = "Update a user",
            description = "Cập nhật thông tin người dùng.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(
                            name = "Update user req",
                            externalValue = "/jsonExample/user/put_user.json"
                    ))
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Updated"
            )
    )
    @PutMapping(Routes.PUT_USER_UPDATE)
    public ResponseEntity<Object> updateUser(@RequestBody String json) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(userService.update(json));
    }

    // 🔹 Xoá mềm user
    @Operation(
            summary = "Delete a user (soft delete)",
            description = "Xoá mềm người dùng bằng cách set enabled=false."
    )
    @ApiResponse(responseCode = "200", description = "Deleted successfully")
    @DeleteMapping(Routes.DELETE_USER_DELETE)
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(null);
    }
}
