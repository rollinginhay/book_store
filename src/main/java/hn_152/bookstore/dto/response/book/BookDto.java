package hn_152.bookstore.dto.response.book;

import hn_152.bookstore.entity.book.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Book}
 */
public record BookDto(LocalDateTime createdAt, LocalDateTime updatedAt,
                      Boolean enabled, String note, Long id, String title,
                      String language, String edition, LocalDateTime published,
                      List<CreatorDto> creators, List<GenreDto> genres,
                      List<TagDto> tags, List<ReviewDto> reviews,
                      PublisherDto publisher, List<BookDetailDto> bookCopies,
                      SeriesDto series,
                      String imageUrl) implements Serializable {
    /**
     * DTO for {@link Creator}
     */
    public record CreatorDto(LocalDateTime createdAt, LocalDateTime updatedAt,
                             Boolean enabled, String note, Long id, String name,
                             String role) implements Serializable {
    }

    /**
     * DTO for {@link Genre}
     */
    public record GenreDto(LocalDateTime createdAt, LocalDateTime updatedAt,
                           Boolean enabled, String note, Long id,
                           String name) implements Serializable {
    }

    /**
     * DTO for {@link Tag}
     */
    public record TagDto(LocalDateTime createdAt, LocalDateTime updatedAt,
                         Boolean enabled, String note, Long id,
                         String name) implements Serializable {
    }

    /**
     * DTO for {@link Review}
     */
    public record ReviewDto(LocalDateTime createdAt, LocalDateTime updatedAt,
                            Boolean enabled, String note, Long id,
                            Integer rating,
                            String comment) implements Serializable {
    }

    /**
     * DTO for {@link BookDetail}
     */
    public record BookDetailDto(LocalDateTime createdAt,
                                LocalDateTime updatedAt, Boolean enabled,
                                String note, Long id, String bookTitle,
                                String bookImageUrl, String isbn11,
                                String isbn13, String bookFormat,
                                String dimensions, Long printLength, Long stock,
                                Long price,
                                String bookCondition) implements Serializable {
    }
}