package hn_152.bookstore.entity.book;

import hn_152.bookstore.entity.AuditableEntity;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Creator> creators;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Genre> genres;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Tag> tags;

    @OneToMany(mappedBy = "book")
    private List<Review> reviews;

    @ManyToOne
    private Publisher publisher;

    @OneToMany(mappedBy = "book")
    private List<BookDetail> bookCopies;

    @ManyToOne
    private Series series;

    private String imageUrl;


}
