package sd_009.bookstore.service.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sd_009.bookstore.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

}
