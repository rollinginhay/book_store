package sd_009.bookstore.service.book;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.exceptionHanding.exception.DependencyConflictException;
import sd_009.bookstore.config.exceptionHanding.exception.DuplicateElementException;
import sd_009.bookstore.config.exceptionHanding.exception.IsDisabledException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.book.*;
import sd_009.bookstore.entity.book.*;
import sd_009.bookstore.repository.*;
import sd_009.bookstore.util.mapper.book.*;
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
    private final CreatorMapper creatorMapper;
    private final PublisherMapper publisherMapper;
    private final GenreMapper genreMapper;
    private final SeriesMapper seriesMapper;
    private final ReviewMapper reviewMapper;
    private final BookDetailMapper bookDetailMapper;
    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;
    private final ReviewRepository reviewRepository;
    private final CreatorRepository creatorRepository;
    private final PublisherRepository publisherRepository;
    private final SeriesRepository seriesRepository;
    private final GenreRepository genreRepository;

    @Transactional
    public String find(Boolean enabled, String titleQuery, Pageable pageable) {
        Page<Book> page;
        if (titleQuery == null || titleQuery.isEmpty()) {
            page = bookRepository.findByEnabled(enabled, pageable);
        } else {
            page = bookRepository.findByTitleContainingAndEnabled(titleQuery, enabled, pageable);
        }
        List<BookDto> dtos = page.getContent().stream().map(bookMapper::toDto).toList();

        LinkParamMapper<?> paramMapper = LinkParamMapper.<Book>builder()
                .keyword(titleQuery)
                .enabled(enabled)
                .page(page)
                .build();

        Document<List<BookDto>> doc = Document
                .with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLinkWithQuery(Routes.GET_BOOKS.toString(), paramMapper.getSelfParams()))
                        .first(LinkMapper.toLinkWithQuery(Routes.GET_BOOKS.toString(), paramMapper.getFirstParams()))
                        .last(LinkMapper.toLinkWithQuery(Routes.GET_BOOKS.toString(), paramMapper.getLastParams()))
                        //has to manually check for null in case of invalid pages
                        .next(paramMapper.getNextParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_BOOKS.toString(), paramMapper.getNextParams()))
                        .prev(paramMapper.getPrevParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_BOOKS.toString(), paramMapper.getPrevParams()))
                        .build().toMap()))
                .build();

        return getListAdapter().toJson(doc);
    }

    public String findById(Long id) {
        Book found = bookRepository.findById(id).orElseThrow();

        BookDto dto = bookMapper.toDto(found);

        Document<BookDto> doc = Document
                .with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_BOOKS.toString(), id))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    @Transactional
    public String save(String json) {
        BookDto dto = validator.readAndValidate(json, BookDto.class);

        Optional<Book> existing = bookRepository.findByTitle(dto.getTitle());

        if (existing.isPresent()) {
            if (existing.get().getEnabled()) {
                throw new DuplicateElementException("Name already exists");
            }

            throw new IsDisabledException("Book is disabled. Can be reinstated");
        }

        Book saved = bookRepository.save(bookMapper.toEntity(dto));
        return getSingleAdapter().toJson(Document
                .with(bookMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_BOOK_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public String update(String json) {
        BookDto dto = validator.readAndValidate(json, BookDto.class);

        Book existing = bookRepository.findById(Long.valueOf(dto.getId())).orElseThrow();

        Book saved = bookRepository.save(bookMapper.partialUpdate(dto, existing));
        return getSingleAdapter().toJson(Document
                .with(bookMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_BOOK_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.findById(id).ifPresent(e -> {
            List<BookDetail> associated = bookDetailRepository.findByBook(e);

            if (!associated.isEmpty()) {
                throw new DependencyConflictException("Cannot delete due to existing associations");
            }
            e.setEnabled(false);
            bookRepository.save(e);
        });
    }

    @Transactional
    public String attachOrReplaceRelationship(Long bookId, String json, String relationship) {
        Book book = bookRepository.findById(bookId).orElseThrow();

        Class<?> dependentType;

        switch (relationship) {
            case "bookDetail" -> {
                dependentType = BookDetailDto.class;
            }
            case "review" -> {
                dependentType = ReviewDto.class;
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
            case ReviewDto reviewDto -> {
                Review review = reviewRepository.findById(Long.valueOf(reviewDto.getId())).orElseThrow();
                review.setBook(book);
                book.getReviews().add(review);
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
        return getSingleAdapter().toJson(Document.with(bookMapper.toDto(saved)).build());
    }

    @Transactional
    public <T> String detachRelationShip(Long bookId, String json, String relationship) {
        Book book = bookRepository.findById(bookId).orElseThrow();

        Class<?> dependentType;

        switch (relationship) {
            case "bookDetail" -> {
                dependentType = BookDetailDto.class;
            }
            case "review" -> {
                dependentType = ReviewDto.class;
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
            case ReviewDto reviewDto -> {
                Review review = reviewRepository.findById(Long.valueOf(reviewDto.getId())).orElseThrow();
                review.setBook(null);
                book.getReviews().remove(review);
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
        return getSingleAdapter().toJson(Document.with(bookMapper.toDto(bookRepository.save(book))).build());
    }

    public String getDependents(Long bookId, String type) {
        Book book = bookRepository.findById(bookId).orElseThrow();

        switch (type) {
            case "bookDetail" -> {
                List<BookDetail> dependents = bookDetailRepository.findByBook(book);
                List<BookDetailDto> dtos = dependents.stream().map(bookDetailMapper::toDto).toList();
                return adapterProvider.listResourceAdapter(BookDetailDto.class).toJson(Document
                        .with(dtos)
                        .links(Links.from(JsonApiLinksObject.builder()
                                .self(LinkMapper.toLink(Routes.MULTI_BOOK_RELATIONSHIP_BOOK_DETAIL, bookId))
                                .build().toMap()))
                        .build());
            }
            case "review" -> {
                List<Review> dependents = reviewRepository.findByBook(book);
                List<ReviewDto> dtos = dependents.stream().map(reviewMapper::toDto).toList();
                return adapterProvider.listResourceAdapter(ReviewDto.class).toJson(Document
                        .with(dtos)
                        .links(Links.from(JsonApiLinksObject.builder()
                                .self(LinkMapper.toLink(Routes.MULTI_BOOK_RELATIONSHIP_REVIEW, bookId))
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


