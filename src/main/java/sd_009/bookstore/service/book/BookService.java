package sd_009.bookstore.service.book;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import jsonapi.Meta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.exceptionHanding.exception.DependencyConflictException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.internal.JsonApiMetaObject;
import sd_009.bookstore.dto.jsonApiResource.book.*;
import sd_009.bookstore.entity.book.*;
import sd_009.bookstore.repository.*;
import sd_009.bookstore.util.mapper.book.BookDetailMapper;
import sd_009.bookstore.util.mapper.book.BookMapper;
import sd_009.bookstore.util.mapper.book.GenreMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.link.LinkParamMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final JsonApiAdapterProvider adapterProvider;
    private final JsonApiValidator validator;
    private final BookMapper bookMapper;
    private final GenreMapper genreMapper;
    private final BookDetailMapper bookDetailMapper;
    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;
    private final CreatorRepository creatorRepository;
    private final PublisherRepository publisherRepository;
    private final SeriesRepository seriesRepository;
    private final GenreRepository genreRepository;
    private final GenreClosureRepository genreClosureRepository;

    @Transactional
    public String find(Boolean enabled, String titleQuery, Pageable pageable,
                       String genreName, Long genreId) {

        Page<Book> page;

        // ⭐ ƯU TIÊN LỌC THEO ID (FE đang dùng filter.genre=ID)
        if (genreId != null) {
            return findBooksByGenre(genreId, pageable);
        }

        // 🔥 GIỮ LOGIC CŨ LỌC THEO TÊN
        if (genreName != null && !genreName.isBlank()) {

            var parent = genreRepository.findByName(genreName.trim())
                    .orElse(null);

            if (parent == null) {
                page = Page.empty(pageable);
            } else {

                List<Long> descendantIds =
                        genreClosureRepository.findAllDescendantIds(parent.getId());

                if (descendantIds == null || descendantIds.isEmpty()) {
                    descendantIds = List.of(parent.getId());
                }

                List<Genre> genres = genreRepository.findAllById(descendantIds);

                page = bookRepository.findDistinctByGenresInAndEnabled(genres, true, pageable);
            }
        }

        // 🔹 Nếu không có genre
        else if (titleQuery == null || titleQuery.trim().isEmpty()) {
            if (enabled != null) {
                page = bookRepository.findByEnabled(enabled, pageable);
            } else {
                page = bookRepository.findAll(pageable);
            }
        }

        // 🔹 Nếu có keyword
        else {
            Optional<Book> found = bookRepository.findByTitle(titleQuery.trim());
            List<Book> books = found.map(List::of).orElse(List.of());
            page = new org.springframework.data.domain.PageImpl<>(books, pageable, books.size());
        }

        List<BookDto> dtos = page.getContent()
                .stream()
                .map(b -> bookMapper.toDto(b, genreClosureRepository, genreMapper))
                .toList();

        LinkParamMapper<?> paramMapper = LinkParamMapper.<Book>builder()
                .keyword(titleQuery)
                .enabled(enabled)
                .page(page)
                .build();

        Document<List<BookDto>> doc = Document
                .with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLinkWithQuery(Routes.GET_BOOKS, paramMapper.getSelfParams()))
                        .first(LinkMapper.toLinkWithQuery(Routes.GET_BOOKS, paramMapper.getFirstParams()))
                        .last(LinkMapper.toLinkWithQuery(Routes.GET_BOOKS, paramMapper.getLastParams()))
                        .next(paramMapper.getNextParams() == null ? null
                                : LinkMapper.toLinkWithQuery(Routes.GET_BOOKS, paramMapper.getNextParams()))
                        .prev(paramMapper.getPrevParams() == null ? null
                                : LinkMapper.toLinkWithQuery(Routes.GET_BOOKS, paramMapper.getPrevParams()))
                        .build().toMap()))
                .meta(Meta.from(JsonApiMetaObject.builder()
                        .firstPage(0)
                        .lastPage(page.getTotalPages() - 1)
                        .totalPages(page.getTotalPages())
                        .build()))
                .build();

        return getListAdapter().toJson(doc);
    }




    public String findById(Long id) {
        Book found = bookRepository.findById(id).orElseThrow();

        BookDto dto = bookMapper.toDto(found, genreClosureRepository, genreMapper);



        Document<BookDto> doc = Document
                .with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_BOOK_BY_ID, id))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    @Transactional(readOnly = true)
    public String findBooksByGenre(Long genreId, Pageable pageable) {
        // 1️⃣ Lấy tất cả ID thể loại con (bao gồm chính nó)
        List<Long> descendantIds = genreClosureRepository.findAllDescendantIds(genreId);
        if (descendantIds == null || descendantIds.isEmpty()) {
            descendantIds = List.of(genreId);
        }

        // 2️⃣ Lấy entity Genre tương ứng
        List<Genre> genres = genreRepository.findAllById(descendantIds);

        // 3️⃣ Lấy sách thuộc các genre đó
        Page<Book> page = bookRepository.findDistinctByGenresInAndEnabled(genres, true, pageable);

        // 4️⃣ Map sang DTO
        List<BookDto> dtos = page.getContent()
                .stream()
                .map(b -> bookMapper.toDto(b, genreClosureRepository, genreMapper))
                .toList();


        // 5️⃣ Tạo JSON:API document (dùng hệ adapter của m)
        LinkParamMapper<Book> paramMapper = LinkParamMapper.<Book>builder()
                .page(page)
                .build();

        Document<List<BookDto>> doc = Document
                .with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLinkWithQuery(Routes.GET_BOOKS, paramMapper.getSelfParams()))
                        .first(LinkMapper.toLinkWithQuery(Routes.GET_BOOKS, paramMapper.getFirstParams()))
                        .last(LinkMapper.toLinkWithQuery(Routes.GET_BOOKS, paramMapper.getLastParams()))
                        .next(paramMapper.getNextParams() == null ? null
                                : LinkMapper.toLinkWithQuery(Routes.GET_BOOKS, paramMapper.getNextParams()))
                        .prev(paramMapper.getPrevParams() == null ? null
                                : LinkMapper.toLinkWithQuery(Routes.GET_BOOKS, paramMapper.getPrevParams()))
                        .build().toMap()))
                .meta(Meta.from(JsonApiMetaObject.builder()
                        .firstPage(0)
                        .lastPage(page.getTotalPages() - 1)
                        .totalPages(page.getTotalPages())
                        .build()))
                .build();

        // ✅ Trả về JSON
        return adapterProvider.listResourceAdapter(BookDto.class).toJson(doc);
    }



    @Transactional
    public String save(String json) {
        Book book = buildEntityWithRelationships(json);

        Book saved = bookRepository.save(book);

        return getSingleAdapter().toJson(Document
                .with(bookMapper.toDto(saved, genreClosureRepository, genreMapper))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_BOOK_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public String update(String json) {
        Book book = buildEntityWithRelationships(json);

        Book saved = bookRepository.save(book);
        return getSingleAdapter().toJson(Document
                .with(bookMapper.toDto(saved, genreClosureRepository, genreMapper))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_BOOK_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.findById(id).ifPresent(e -> {
            List<BookDetail> associated = bookDetailRepository.findAllByBook(e, Sort.unsorted());

            if (!associated.isEmpty()) {
                throw new DependencyConflictException("Cannot delete due to existing associations");
            }
            e.setEnabled(false);
            bookRepository.save(e);
        });
    }

    @Transactional
    public Book buildEntityWithRelationships(String json) {
        BookDto dto = validator.readAndValidate(json, BookDto.class);
        Publisher publisher = dto.getPublisher() == null ? null : publisherRepository.findById(Long.valueOf(dto.getPublisher().getId())).orElse(null);
        Series series = dto.getSeries() == null ? null : seriesRepository.findById(Long.valueOf(dto.getSeries().getId())).orElse(null);
        List<Genre> genres = dto.getGenres() == null ? List.of() : dto.getGenres().stream().map(e -> genreRepository.findById(Long.valueOf(e.getId()))).flatMap(Optional::stream).toList();
        List<Creator> creators = dto.getCreators() == null ? List.of() : dto.getCreators().stream().map(e -> creatorRepository.findById(Long.valueOf(e.getId()))).flatMap(Optional::stream).toList();

        Book book = bookMapper.toEntity(dto);
        if (book.getId() == 0) book.setId(null);

        book.setPublisher(publisher);
        book.setSeries(series);
        book.setGenres(genres);
        book.setCreators(creators);

        return book;
    }

    @Transactional
    public String attachRelationShip(Long bookId, String json, String relationship) {
        Book book = bookRepository.findById(bookId).orElseThrow();

        Class<?> dependentType;

        switch (relationship) {
            case "bookDetail" -> {
                dependentType = BookDetailDto.class;
            }
            case "creator" -> {
                dependentType = CreatorDto.class;
            }
            case "publisher" -> {
                dependentType = PublisherDto.class;
            }
            case "series" -> {
                dependentType = SeriesDto.class;
            }
            case "genre" -> {
                dependentType = GenreDto.class;
            }
            default ->
                    throw new BadRequestException("Invalid relationship type");
        }

        var dto = validator.readAndValidate(json, dependentType);

        switch (dto) {
            case BookDetailDto bookDetailDto -> {
                BookDetail bookDetail = bookDetailRepository.findById(Long.valueOf(bookDetailDto.getId())).orElseThrow();
                bookDetail.setBook(book);
                book.getBookCopies().add(bookDetail);
            }
            case CreatorDto creatorDto -> {
                Creator creator = creatorRepository.findById(Long.valueOf(creatorDto.getId())).orElseThrow();
                book.getCreators().add(creator);
            }
            case PublisherDto publisherDto -> {
                Publisher publisher = publisherRepository.findById(Long.valueOf(publisherDto.getId())).orElseThrow();
                book.setPublisher(publisher);
            }
            case SeriesDto seriesDto -> {
                Series series = seriesRepository.findById(Long.valueOf(seriesDto.getId())).orElseThrow();
                book.setSeries(series);
            }
            case GenreDto genreDto -> {
                Genre genre = genreRepository.findById(Long.valueOf(genreDto.getId())).orElseThrow();
                book.getGenres().add(genre);
            }
            case null, default ->
                    throw new BadRequestException("Unsupported relationship type");
        }
        Book saved = bookRepository.save(book);
        return getSingleAdapter().toJson(
                Document.with(bookMapper.toDto(saved, genreClosureRepository, genreMapper)).build()
        );

    }

    @Transactional
    public <T> String detachRelationShip(Long bookId, String json, String relationship) {
        Book book = bookRepository.findById(bookId).orElseThrow();

        Class<?> dependentType;

        switch (relationship) {
            case "bookDetail" -> {
                dependentType = BookDetailDto.class;
            }
            case "creator" -> {
                dependentType = CreatorDto.class;
            }
            case "publisher" -> {
                dependentType = PublisherDto.class;
            }
            case "series" -> {
                dependentType = SeriesDto.class;
            }
            case "genre" -> {
                dependentType = GenreDto.class;
            }
            default ->
                    throw new BadRequestException("Invalid relationship type");
        }

        var dto = validator.readAndValidate(json, dependentType);

        switch (dto) {
            case BookDetailDto bookDetailDto -> {
                BookDetail bookDetail = bookDetailRepository.findById(Long.valueOf(bookDetailDto.getId())).orElseThrow();
                bookDetail.setBook(null);
                book.getBookCopies().remove(bookDetail);
            }
            case CreatorDto creatorDto -> {
                Creator creator = creatorRepository.findById(Long.valueOf(creatorDto.getId())).orElseThrow();
                book.getCreators().remove(creator);
            }
            case PublisherDto publisherDto -> {
                Publisher publisher = publisherRepository.findById(Long.valueOf(publisherDto.getId())).orElseThrow();
                book.setPublisher(null);
            }
            case SeriesDto seriesDto -> {
                Series series = seriesRepository.findById(Long.valueOf(seriesDto.getId())).orElseThrow();
                book.setSeries(null);
            }
            case GenreDto genreDto -> {
                Genre genre = genreRepository.findById(Long.valueOf(genreDto.getId())).orElseThrow();
                book.getGenres().remove(genre);
            }
            case null, default ->
                    throw new BadRequestException("Unsupported relationship type");
        }

        Book saved = bookRepository.save(book);
        return getSingleAdapter().toJson(Document
                .with(bookMapper.toDto(saved, genreClosureRepository, genreMapper))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_BOOK_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    public String getDependents(Long bookId, String type) {
        Book book = bookRepository.findById(bookId).orElseThrow();

        switch (type) {
            case "bookDetail" -> {
                List<BookDetail> dependents = bookDetailRepository.findAllByBook(book, Sort.by("updatedAt").descending());
                List<BookDetailDto> dtos = dependents.stream().map(bookDetailMapper::toDto).toList();
                return adapterProvider.listResourceAdapter(BookDetailDto.class).toJson(Document
                        .with(dtos)
                        .links(Links.from(JsonApiLinksObject.builder()
                                .self(LinkMapper.toLink(Routes.MULTI_BOOK_RELATIONSHIP_BOOK_DETAIL, bookId))
                                .build().toMap()))
                        .build());
            }
            default ->
                    throw new BadRequestException("Unsupported relationship type");
        }
    }

    private JsonAdapter<Document<BookDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(BookDto.class);
    }

    private JsonAdapter<Document<List<BookDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(BookDto.class);
    }
}


