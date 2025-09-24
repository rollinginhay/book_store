package sd_009.bookstore.entity.book;

import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Embeddable
public class GenreClosureId {
    private Long ancestor;

    private Long descendant;
}
