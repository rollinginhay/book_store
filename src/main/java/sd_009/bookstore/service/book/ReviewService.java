package sd_009.bookstore.service.book;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.book.ReviewDto;
import sd_009.bookstore.dto.jsonApiResource.book.ReviewOwningDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Review;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.repository.ReviewRepository;
import sd_009.bookstore.util.mapper.book.ReviewMapper;
import sd_009.bookstore.util.mapper.book.ReviewOwningMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final JsonApiAdapterProvider adapterProvider;
    private final JsonApiValidator jsonApiValidator;

    private final ReviewMapper reviewMapper;
    private final ReviewOwningMapper reviewOwningMapper;
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    @Transactional
    public String findByBook(Boolean enabled, Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow();

        List<Review> reviews = reviewRepository.findByBook(book);

        List<ReviewOwningDto> dtos = reviews.stream().map(reviewOwningMapper::toDto).toList();

        Document<List<ReviewOwningDto>> doc = Document.with(dtos).links(
                Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_REVIEWS_BY_BOOK_ID.toString(), book.getId()))
                        .build().toMap())
        ).build();

        return getListOwningAdapter().toJson(doc);
    }

    @Transactional
    public String findById(Long id) {

        Review found = reviewRepository.findById(id).orElseThrow();

        ReviewOwningDto dto = reviewOwningMapper.toDto(found);

        Document<ReviewOwningDto> doc = Document
                .with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_REVIEW_BY_ID.toString(), id))
                        .build().toMap()))
                .build();

        return getSingleOwningAdapter().toJson(doc);
    }

    @Transactional
    public String save(String json) {
        ReviewDto dto = jsonApiValidator.readAndValidate(json, ReviewDto.class);

        Review saved = reviewRepository.save(reviewMapper.toEntity(dto));
        return getSingleAdapter().toJson(Document
                .with(reviewMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_REVIEW_BY_ID_PATH, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public String update(String json) {
        ReviewDto dto = jsonApiValidator.readAndValidate(json, ReviewDto.class);
        if (dto.getId() == null) {
            throw new BadRequestException("No identifier found");
        }
        Review existing = reviewRepository.findById(Long.valueOf(dto.getId())).orElseThrow();
        Review saved = reviewRepository.save(reviewMapper.partialUpdate(dto, existing));

        return getSingleAdapter().toJson(Document
                .with(reviewMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_REVIEW_BY_ID_PATH, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public void delete(Long id) {
        reviewRepository.findById(id).ifPresent(e -> {
            //need to be deleted from shopping carts
            //receipts query any bookdetail status
            //need to disable from campaign detail
            e.setEnabled(false);
            reviewRepository.save(e);
        });
    }

    private JsonAdapter<Document<ReviewDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(ReviewDto.class);
    }

    private JsonAdapter<Document<List<ReviewDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(ReviewDto.class);
    }

    private JsonAdapter<Document<ReviewOwningDto>> getSingleOwningAdapter() {
        return adapterProvider.singleResourceAdapter(ReviewOwningDto.class);
    }

    private JsonAdapter<Document<List<ReviewOwningDto>>> getListOwningAdapter() {
        return adapterProvider.listResourceAdapter(ReviewOwningDto.class);
    }

}
