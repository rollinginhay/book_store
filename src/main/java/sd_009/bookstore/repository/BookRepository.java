package sd_009.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sd_009.bookstore.entity.book.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    Page<Book> findByEnabled(Boolean enabled, Pageable pageable);

    Page<Book> findByTitleContainingAndEnabled(String title, Boolean enabled, Pageable pageable);

    List<Book> findByGenres(List<Genre> genres);

    List<Book> findByCreators(List<Creator> creators);

    List<Book> findByPublisher(Publisher publisher);

    List<Book> findBySeries(Series series);

    List<Book> findByBookCopies(List<BookDetail> bookCopies);

    List<Book> findByEnabledAndPublisher(Boolean enabled, Publisher publisher);

    Optional<Book> findByEnabledAndId(Boolean enabled, Long id);

    Optional<Book> findByTitle(String title);
}