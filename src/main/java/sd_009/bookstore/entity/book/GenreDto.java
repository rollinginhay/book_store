package sd_009.bookstore.entity.book;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Genre}
 */
@Value
public class GenreDto implements Serializable {
    String note;
    Long id;
    String name;
}