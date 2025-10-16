package sd_009.bookstore.dto.jsonApiResource.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.entity.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link User}
 */
@AllArgsConstructor
@Getter
public class UserDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    private final Long id;
    private final String email;
    private final String password;
    private final String username;
    private final String personName;
    private final String phoneNumber;
    private final String address;
    private final String oauth2Id;
    private final Boolean isOauth2User;
    private final List<RoleDto> roles;
}