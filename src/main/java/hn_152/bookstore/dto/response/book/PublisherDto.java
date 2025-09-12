package hn_152.bookstore.dto.response.book;

import hn_152.bookstore.entity.book.Publisher;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Publisher}
 */
public record PublisherDto(LocalDateTime createdAt, LocalDateTime updatedAt,
                           Boolean enabled, String note, Long id,
                           String name) implements Serializable {
}