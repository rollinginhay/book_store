package sd_009.bookstore.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jsonapi.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.dto.jsonApiResource.book.GenreDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Genre;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.repository.GenreRepository;
import sd_009.bookstore.util.mapper.book.BookMapper;
import sd_009.bookstore.util.mapper.book.GenreMapper;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TestController {
    private final JsonApiAdapterProvider adapterProvider;

    private final GenreMapper genreMapper;
    private final BookMapper bookMapper;

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Hidden
    @GetMapping("/test")
    public ResponseEntity<String> testMapper() {

        Genre genre = genreRepository.findAll().get(0);

        GenreDto dto = genreMapper.toDto(genre);

        Document<GenreDto> doc = Document.with(dto).build();

        return ResponseEntity.ok().body(adapterProvider.singleResourceAdapter(GenreDto.class).toJson(doc));
    }

    @Hidden
    @GetMapping("/test2")
    public ResponseEntity<String> testMapperButMore() {

        Book book = bookRepository.findAll().get(0);

        BookDto dto = bookMapper.toDto(book);

        Document<BookDto> doc = Document.with(dto).build();

        return ResponseEntity.ok().body(adapterProvider.singleResourceAdapter(BookDto.class).toJson(doc));
    }
}
