package hn_152.bookstore.util.mapper;

import hn_152.bookstore.dto.response.auth.AuthResponse;
import hn_152.bookstore.entity.user.User;

public class UserMapper {
    public static AuthResponse mapToAuthResponse(User user, String jwtToken) {
        return AuthResponse.builder()
                .oauthId(user.getOauth2Id())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .jwtToken(jwtToken)
                .build();
    }
}
