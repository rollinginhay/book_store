package sd_009.bookstore.dto.jsonApiResource.user;

import jsonapi.Id;
import jsonapi.Resource;
import jsonapi.ToMany;
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
@Resource(type = "user")
public class UserDto implements Serializable {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    @Id
    private final String id;
    private final String email;
    private final String username;
    private final String personName;
    private final String phoneNumber;
    private final String address;
    private final String oauth2Id;
    private final Boolean isOauth2User;
    @ToMany(name = "roles")
    private final List<RoleDto> roles;

}