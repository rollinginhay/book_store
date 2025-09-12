package hn_152.bookstore.service.book;

import hn_152.bookstore.dto.response.book.BookDto;
import hn_152.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<BookDto> getAllEnabled() {

    }
}
