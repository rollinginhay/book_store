package hn_152.bookstore.model.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiError(
        LocalDateTime timestamp,
        String message,
        String description
) {
}
