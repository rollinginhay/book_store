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
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.repository.BookDetailRepository;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.repository.CreatorRepository;
import sd_009.bookstore.util.mapper.book.BookMapper;
import sd_009.bookstore.util.mapper.book.CreatorMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.link.LinkParamMapper;
import sd_009.bookstore.util.spec.Routes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final JsonApiAdapterProvider adapterProvider;
    private final BookMapper bookMapper;
    private final CreatorMapper creatorMapper;
    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;
    private final CreatorRepository creatorRepository;

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

    public String findById(Boolean enabled, Long id) {
        Book found = bookRepository.findByEnabledAndId(enabled, id).orElseThrow();

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
    public String save(BookDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);

        Optional<Book> existing = bookRepository.findByTitle(book.getTitle());

        if (existing.isPresent()) {
            if (existing.get().getEnabled()) {
                throw new DuplicateElementException("Name already exists");
            }

            throw new IsDisabledException("Book is disabled. Can be reinstated");
        }

        return getSingleAdapter().toJson(Document.with(bookMapper.toDto(bookRepository.save(book))).build());
    }

    @Transactional
    public String update(BookDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        if (book.getId() == null) {
            throw new BadRequestException("No identifier found");
        }
        return getSingleAdapter().toJson(Document.with(bookMapper.toDto(bookRepository.save(book))).build());
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

    private JsonAdapter<Document<BookDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(BookDto.class);
    }

    private JsonAdapter<Document<List<BookDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(BookDto.class);
    }
}


