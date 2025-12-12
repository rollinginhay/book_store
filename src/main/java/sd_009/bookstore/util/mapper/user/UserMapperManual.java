package sd_009.bookstore.util.mapper.user;

import sd_009.bookstore.dto.jsonApiResource.user.AuthObject;
import sd_009.bookstore.entity.user.Role;
import sd_009.bookstore.entity.user.User;

public class UserMapperManual {
    public static AuthObject mapToAuthResponse(User user, String jwtToken) {
        return AuthObject.builder()
                .userId(user.getId().toString())
                .oauthId(user.getOauth2Id())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .jwtToken(jwtToken)
                .build();
    }
}
