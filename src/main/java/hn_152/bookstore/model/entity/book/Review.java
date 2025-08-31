package hn_152.bookstore.model.entity.book;

import hn_152.bookstore.model.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

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
    private Book book;
}
