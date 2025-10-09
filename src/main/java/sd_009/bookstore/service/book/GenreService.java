package sd_009.bookstore.service.book;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.exceptionHanding.exception.DependencyConflictException;
import sd_009.bookstore.config.exceptionHanding.exception.DuplicateElementException;
import sd_009.bookstore.config.exceptionHanding.exception.IsDisabledException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.book.GenreDto;
import sd_009.bookstore.dto.jsonApiResource.book.GenreOwningDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Genre;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.repository.GenreRepository;
import sd_009.bookstore.util.mapper.book.GenreMapper;
import sd_009.bookstore.util.mapper.book.GenreOwningMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.link.LinkParamMapper;
import sd_009.bookstore.util.spec.Routes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final JsonApiAdapterProvider adapterProvider;

    private final GenreMapper genreMapper;
    private final GenreOwningMapper genreOwningMapper;

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Transactional
    public String find(Boolean enabled, String name, Pageable pageable) {

        Page<Genre> page;
        if (name == null || name.isEmpty()) {
            page = genreRepository.findByEnabled(enabled, pageable);
        } else {
            page = genreRepository.findByNameContainingAndEnabled(name, enabled, pageable);
        }
        List<GenreDto> dtos = page.getContent().stream().map(genreMapper::toDto).toList();

        LinkParamMapper<Genre> paramMapper = LinkParamMapper.<Genre>builder()
                .keyword(name)
                .enabled(enabled)
                .page(page)
                .build();

        Document<List<GenreDto>> doc = Document
                .with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLinkWithQuery(Routes.GET_GENRES.toString(), paramMapper.getSelfParams()))
                        .first(LinkMapper.toLinkWithQuery(Routes.GET_GENRES.toString(), paramMapper.getFirstParams()))
                        .last(LinkMapper.toLinkWithQuery(Routes.GET_GENRES.toString(), paramMapper.getLastParams()))
                        //has to manually check for null in case of invalid pages
                        .next(paramMapper.getNextParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_GENRES.toString(), paramMapper.getNextParams()))
                        .prev(paramMapper.getPrevParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_GENRES.toString(), paramMapper.getPrevParams()))
                        .build().toMap()))
                .build();
        return getListAdapter().toJson(doc);
    }

    @Transactional
    public String findById(Boolean enabled, Long id) {

        Genre found = genreRepository.findByEnabledAndId(enabled, id).orElseThrow();

        GenreOwningDto dto = genreOwningMapper.toDto(found);

        Document<GenreOwningDto> doc = Document
                .with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_GENRE_BY_ID.toString(), id))
                        .build().toMap()))
                .build();

        return getSingleOwningAdapter().toJson(doc);
    }

    @Transactional
    public String save(GenreDto genreDto) {
        Genre genre = genreMapper.toEntity(genreDto);

        Optional<Genre> existing = genreRepository.findByName(genre.getName());

        if (existing.isPresent()) {
            if (existing.get().getEnabled()) {
                throw new DuplicateElementException("Name already exists");
            }

            throw new IsDisabledException("Genre is disabled. Can be reinstated");
        }

        return getSingleAdapter().toJson(Document.with(genreMapper.toDto(genreRepository.save(genre))).build());
    }

    @Transactional
    public String update(GenreDto genreDto) {
        Genre genre = genreMapper.toEntity(genreDto);
        if (genre.getId() == null) {
            throw new BadRequestException("No identifier found");
        }
        return getSingleAdapter().toJson(Document.with(genreMapper.toDto(genreRepository.save(genre))).build());
    }

    @Transactional
    public void delete(Long id) {
        genreRepository.findById(id).ifPresent(e -> {
            List<Book> associated = bookRepository.findByGenres(Collections.singletonList(e));

            if (!associated.isEmpty()) {
                throw new DependencyConflictException("Cannot delete due to existing associations");
            }
            e.setEnabled(false);
            genreRepository.save(e);
        });
    }

    private JsonAdapter<Document<GenreDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(GenreDto.class);
    }

    private JsonAdapter<Document<List<GenreDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(GenreDto.class);
    }

    private JsonAdapter<Document<GenreOwningDto>> getSingleOwningAdapter() {
        return adapterProvider.singleResourceAdapter(GenreOwningDto.class);
    }
}
