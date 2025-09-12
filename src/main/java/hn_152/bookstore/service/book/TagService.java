package hn_152.bookstore.service.book;

import hn_152.bookstore.dto.response.book.BookDto;
import hn_152.bookstore.entity.book.Tag;
import hn_152.bookstore.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.Meta;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

}
