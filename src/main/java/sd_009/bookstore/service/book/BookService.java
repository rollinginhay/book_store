package sd_009.bookstore.service.book;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.repository.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getBooks(Integer page, Integer size, Sort sort) {
        return null;
    }
}
