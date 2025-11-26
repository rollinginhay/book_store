package sd_009.bookstore.entity.book;

import jakarta.persistence.*;
import lombok.*;
import sd_009.bookstore.entity.AuditableEntity;
import sd_009.bookstore.entity.user.User;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Review extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rating;

    private String comment;

    @ManyToOne
    @JoinColumn
    private Book book;

    @ManyToOne
    @JoinColumn
    private User reviewer;
}
