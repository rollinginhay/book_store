package sd_009.bookstore.entity.book;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class GenreClosure {
    @EmbeddedId
    private GenreClosureId id;

    @ManyToOne
    @MapsId("ancestor")
    private Genre ancestor;

    @ManyToOne
    @MapsId("descendant")
    private Genre descendant;

    private Long depth;
}
