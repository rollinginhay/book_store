package sd_009.bookstore.dto.jsonApiResource.user;

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
public class RoleDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    private final Long id;
    private final String name;
}