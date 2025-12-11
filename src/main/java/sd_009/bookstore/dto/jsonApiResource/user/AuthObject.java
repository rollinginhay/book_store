package sd_009.bookstore.dto.jsonApiResource.user;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Collection;

@Builder
public record AuthObject(
        String userId,
        String oauthId,
        String email,
        String username,
        Collection<String> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String jwtToken
) {
}
