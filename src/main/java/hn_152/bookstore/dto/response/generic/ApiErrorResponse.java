package hn_152.bookstore.dto.response.generic;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiErrorResponse(
        LocalDateTime timestamp,
        String message,
        String description
) {
}
