package sd_009.bookstore.util.mapper;

import sd_009.bookstore.dto.jsonApiResource.user.response.AuthObject;
import sd_009.bookstore.entity.user.User;

public class UserMapper {
    public static AuthObject mapToAuthResponse(User user, String jwtToken) {
        return AuthObject.builder()
                .oauthId(user.getOauth2Id())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .jwtToken(jwtToken)
                .build();
    }
}
