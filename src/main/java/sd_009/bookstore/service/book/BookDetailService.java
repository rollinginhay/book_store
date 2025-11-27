package sd_009.bookstore.service.book;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailOwningDto;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.repository.BookDetailRepository;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.repository.GenreClosureRepository;
import sd_009.bookstore.util.mapper.book.BookDetailMapper;
import sd_009.bookstore.util.mapper.book.BookDetailOwningMapper;
import sd_009.bookstore.util.mapper.book.BookMapper;
import sd_009.bookstore.util.mapper.book.GenreMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookDetailService {
    private static final Logger log = LoggerFactory.getLogger(BookDetailService.class);
    private final JsonApiAdapterProvider adapterProvider;
    private final JsonApiValidator jsonApiValidator;
    private final GenreMapper genreMapper;
    private final GenreClosureRepository genreClosureRepository;

    private final BookDetailMapper bookDetailMapper;
    private final BookDetailOwningMapper bookDetailOwningMapper;
    private final BookDetailRepository bookDetailRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Transactional
    public String findByBookId(Boolean enabled, Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow();

        List<BookDetail> bookDetails = bookDetailRepository.findAllByBook(book, Sort.by("updatedAt").descending());

        List<BookDetailOwningDto> dtos = bookDetails.stream().map(bookDetailOwningMapper::toDto).toList();

        Document<List<BookDetailOwningDto>> doc = Document.with(dtos).links(
                Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.MULTI_BOOK_RELATIONSHIP_BOOK_DETAIL, book.getId()))
                        .build().toMap())
        ).build();

        return getListOwningAdapter().toJson(doc);
    }

    @Transactional
    public String findById(Long id) {

        BookDetail found = bookDetailRepository.findById(id).orElseThrow();

        BookDetailDto dto = bookDetailMapper.toDto(found);

        Document<BookDetailDto> doc = Document
                .with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_BOOK_DETAIL_BY_ID, id))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    //only creatable and updatable when attached to a Book
    @Transactional
    public String save(String json) {
        BookDto bookDto = jsonApiValidator.readAndValidate(json, BookDto.class);
        Book book = bookRepository.findById(Long.valueOf(bookDto.getId())).orElseThrow();
        List<BookDetailDto> bookDetailDtos = bookDto.getBookCopies();

        bookDetailDtos.stream().map(bookDetailMapper::toEntity).forEach(e -> {
            if (e.getId() == 0) e.setId(null);
            e.setBook(book);
            bookDetailRepository.save(e);
        });

        Book updatedBook = bookRepository.findById(Long.valueOf(bookDto.getId())).orElseThrow();

        return adapterProvider.singleResourceAdapter(BookDto.class).toJson(Document
                .with(bookMapper.toDto(updatedBook, genreClosureRepository, genreMapper))

                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_BOOK_BY_ID, updatedBook.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public String update(String json) {
        BookDto bookDto = jsonApiValidator.readAndValidate(json, BookDto.class);
        Book book = bookRepository.findById(Long.valueOf(bookDto.getId())).orElseThrow();
        List<BookDetailDto> bookDetailDtos = bookDto.getBookCopies();

        bookDetailDtos.stream().map(bookDetailMapper::toEntity).forEach(e -> {
            if (e.getId() == 0) e.setId(null);
            log.info(e.toString());
            e.setBook(book);
            bookDetailRepository.save(e);
        });

        Book updatedBook = bookRepository.findById(Long.valueOf(bookDto.getId())).orElseThrow();

        return adapterProvider.singleResourceAdapter(BookDto.class).toJson(Document
                .with(bookMapper.toDto(updatedBook, genreClosureRepository, genreMapper))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_BOOK_BY_ID, updatedBook.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public void delete(Long id) {
        bookDetailRepository.findById(id).ifPresent(e -> {
            //need to be deleted from shopping carts
            //receipts query any bookdetail status
            //need to disable from campaign detail
            e.setEnabled(false);
            bookDetailRepository.save(e);
        });
    }

    private JsonAdapter<Document<BookDetailDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(BookDetailDto.class);
    }

    private JsonAdapter<Document<List<BookDetailDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(BookDetailDto.class);
    }

    private JsonAdapter<Document<BookDetailOwningDto>> getSingleOwningAdapter() {
        return adapterProvider.singleResourceAdapter(BookDetailOwningDto.class);
    }

    private JsonAdapter<Document<List<BookDetailOwningDto>>> getListOwningAdapter() {
        return adapterProvider.listResourceAdapter(BookDetailOwningDto.class);
    }


}
