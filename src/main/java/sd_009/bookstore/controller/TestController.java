package sd_009.bookstore.controller;

import jsonapi.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.dto.jsonApiResource.book.GenreDto;
import sd_009.bookstore.entity.book.Genre;
import sd_009.bookstore.repository.GenreRepository;
import sd_009.bookstore.util.mapper.book.GenreMapper;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TestController {
    private final GenreMapper genreMapper;
    private final JsonApiAdapterProvider adapterProvider;

    private final GenreRepository genreRepository;

    @GetMapping("/test")
    public ResponseEntity<String> testMapper() {

        Genre genre = genreRepository.findAll().get(0);

        GenreDto dto = genreMapper.toDto(genre);

        Document<GenreDto> doc = Document.with(dto).build();

        return ResponseEntity.ok().body(adapterProvider.singleResourceAdapter(GenreDto.class).toJson(doc));
    }
}
