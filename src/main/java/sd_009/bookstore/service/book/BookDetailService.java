package sd_009.bookstore.service.book;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailOwningDto;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.repository.BookDetailRepository;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.util.mapper.book.BookDetailMapper;
import sd_009.bookstore.util.mapper.book.BookDetailOwningMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.spec.Routes;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookDetailService {
    private final JsonApiAdapterProvider adapterProvider;

    private final BookDetailMapper bookDetailMapper;
    private final BookDetailOwningMapper bookDetailOwningMapper;
    private final BookDetailRepository bookDetailRepository;
    private final BookRepository bookRepository;

    @Transactional
    public String findByBook(Boolean enabled, BookDto bookDto) {
        Book book = bookRepository.findById(Long.parseLong(bookDto.getId())).orElseThrow();

        List<BookDetail> bookDetails = bookDetailRepository.findByBook(book);

        List<BookDetailOwningDto> dtos = bookDetails.stream().map(bookDetailOwningMapper::toDto).toList();

        Document<List<BookDetailOwningDto>> doc = Document.with(dtos).links(
                Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_BOOK_DETAILS_BY_BOOK_ID.toString(), book.getId()))
                        .build().toMap())
        ).build();

        return getListOwningAdapter().toJson(doc);
    }

    @Transactional
    public String save(BookDetailOwningDto bookDetailOwningDto) {

        BookDetail bookDetail = bookDetailOwningMapper.toEntity(bookDetailOwningDto);

        return getSingleAdapter().toJson(Document.with(bookDetailMapper.toDto(bookDetailRepository.save(bookDetail))).build());
    }

    @Transactional
    public String update(BookDetailDto bookDetailDto) {
        BookDetail bookDetail = bookDetailMapper.toEntity(bookDetailDto);
        if (bookDetail.getId() == null) {
            throw new BadRequestException("No identifier found");
        }
        return getSingleAdapter().toJson(Document.with(bookDetailMapper.toDto(bookDetailRepository.save(bookDetail))).build());
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

    private JsonAdapter<Document<List<BookDetailOwningDto>>> getListOwningAdapter() {
        return adapterProvider.listResourceAdapter(BookDetailOwningDto.class);
    }


}
