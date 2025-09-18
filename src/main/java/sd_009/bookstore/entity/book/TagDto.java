package sd_009.bookstore.entity.book;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Tag}
 */
@Value
public class TagDto implements Serializable {
    String note;
    Long id;
    String name;
}