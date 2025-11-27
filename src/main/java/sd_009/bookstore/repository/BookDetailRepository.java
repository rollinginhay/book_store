package sd_009.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.BookDetail;

import java.util.List;

public interface BookDetailRepository extends JpaRepository<BookDetail, Long> {


    Page<BookDetail> findByEnabled(Boolean enabled, Pageable pageable);


    BookDetail book(Book book);

    List<BookDetail> findAllByBook(Book book, Sort sort);
}