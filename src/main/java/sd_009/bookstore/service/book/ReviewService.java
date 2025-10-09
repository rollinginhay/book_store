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
import sd_009.bookstore.dto.jsonApiResource.book.ReviewDto;
import sd_009.bookstore.dto.jsonApiResource.book.ReviewOwningDto;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Review;
import sd_009.bookstore.repository.ReviewRepository;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.util.mapper.book.ReviewMapper;
import sd_009.bookstore.util.mapper.book.ReviewOwningMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.spec.Routes;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final JsonApiAdapterProvider adapterProvider;

    private final ReviewMapper reviewMapper;
    private final ReviewOwningMapper reviewOwningMapper;
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    @Transactional
    public String findByBook(Boolean enabled, BookDto bookDto) {
        Book book = bookRepository.findById(Long.parseLong(bookDto.getId())).orElseThrow();

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
    public String save(ReviewOwningDto reviewOwningDto) {

        Review review = reviewOwningMapper.toEntity(reviewOwningDto);

        return getSingleAdapter().toJson(Document.with(reviewMapper.toDto(reviewRepository.save(review))).build());
    }

    @Transactional
    public String update(ReviewDto reviewDto) {
        Review review = reviewMapper.toEntity(reviewDto);
        if (review.getId() == null) {
            throw new BadRequestException("No identifier found");
        }
        return getSingleAdapter().toJson(Document.with(reviewMapper.toDto(reviewRepository.save(review))).build());
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

    private JsonAdapter<Document<List<ReviewOwningDto>>> getListOwningAdapter() {
        return adapterProvider.listResourceAdapter(ReviewOwningDto.class);
    }

}
