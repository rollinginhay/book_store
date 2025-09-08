package hn_152.bookstore.model.dto;

public record OAuthPrincipalDetails(
        String email,
        String name,
        String oauthId
) {
}
