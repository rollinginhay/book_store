package sd_009.bookstore.dto.internal;

public record OAuthPrincipalDetails(
        String email,
        String name,
        String oauthId
) {
}
