package sd_009.bookstore.entity.book;

import sd_009.bookstore.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Book extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String language;

    private String edition;

    private LocalDateTime published;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable
    private List<Creator> creators;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable
    private List<Genre> genres;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable
    private List<Tag> tags;

    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<Review> reviews;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Publisher publisher;

    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<BookDetail> bookCopies;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Series series;

    private String imageUrl;


}
