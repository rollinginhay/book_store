package hn_152.bookstore.model.entity.book;

import hn_152.bookstore.model.entity.AuditableEntity;
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

    private String language;

    private String edition;

    private LocalDateTime published;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Creator> creators;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Genre> genres;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Tag> tags;

    @OneToMany
    private List<Review> reviews;

    @ManyToOne(fetch = FetchType.EAGER)
    private Publisher publisher;

    @OneToMany
    private List<BookDetail> bookCopies;

    @ManyToOne
    private Series series;

    private String imageUrl;


}
