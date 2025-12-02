package sd_009.bookstore.entity.book;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
