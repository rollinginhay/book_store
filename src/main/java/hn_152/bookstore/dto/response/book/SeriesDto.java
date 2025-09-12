package hn_152.bookstore.dto.response.book;

import hn_152.bookstore.entity.book.Series;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Series}
 */
public record SeriesDto(LocalDateTime createdAt, LocalDateTime updatedAt, Boolean enabled, String note, Long id, String name) implements Serializable {
  }