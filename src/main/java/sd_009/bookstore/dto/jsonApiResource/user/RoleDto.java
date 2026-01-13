package sd_009.bookstore.dto.jsonApiResource.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.entity.user.Role;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Role}
 */
@AllArgsConstructor
@Getter
@Resource(type = "role")
public class RoleDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private final String note;
    @Id
    private final String id;
    @NotBlank(message = "Tên vai trò là bắt buộc")
    @Size(min = 2, max = 50, message = "Tên vai trò phải từ 2 đến 50 ký tự")
    @Pattern(regexp = "^[A-Z_]+$", message = "Tên vai trò phải là chữ in hoa và gạch dưới (ví dụ: ADMIN, EMPLOYEE)")
    private final String name;
}