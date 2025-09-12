package hn_152.bookstore.dto.internal;

public record OAuthPrincipalDetails(
        String email,
        String name,
        String oauthId
) {
}
