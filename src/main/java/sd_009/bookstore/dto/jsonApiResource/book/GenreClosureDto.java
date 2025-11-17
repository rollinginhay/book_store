package sd_009.bookstore.dto.jsonApiResource.book;

import lombok.*;
import sd_009.bookstore.entity.book.Genre;
import sd_009.bookstore.entity.book.GenreClosureId;

import java.io.Serializable;

/**
 * DTO for {@link .entity.GenreClosure}
 */
@Getter
@AllArgsConstructor
public class GenreClosureDto implements Serializable {
    private final GenreClosureId id;
    private final Genre ancestor;
    private final Genre descendant;
    private final Long depth;
}

