package sd_009.bookstore.util.mapper.book;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.dto.jsonApiResource.book.ReviewOwningDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Review;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:04+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class ReviewOwningMapperImpl implements ReviewOwningMapper {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Review toEntity(ReviewOwningDto reviewOwningDto) {
        if ( reviewOwningDto == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        if ( reviewOwningDto.getId() != null ) {
            review.id( Long.parseLong( reviewOwningDto.getId() ) );
        }
        review.rating( reviewOwningDto.getRating() );
        review.comment( reviewOwningDto.getComment() );
        review.book( bookMapper.toEntity( reviewOwningDto.getBook() ) );

        return review.build();
    }

    @Override
    public ReviewOwningDto toDto(Review review) {
        if ( review == null ) {
            return null;
        }

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;
        String id = null;
        Integer rating = null;
        String comment = null;
        BookDto book = null;

        createdAt = review.getCreatedAt();
        updatedAt = review.getUpdatedAt();
        enabled = review.getEnabled();
        note = review.getNote();
        if ( review.getId() != null ) {
            id = String.valueOf( review.getId() );
        }
        rating = review.getRating();
        comment = review.getComment();
        book = bookMapper.toDto( review.getBook() );

        ReviewOwningDto reviewOwningDto = new ReviewOwningDto( createdAt, updatedAt, enabled, note, id, rating, comment, book );

        return reviewOwningDto;
    }

    @Override
    public Review partialUpdate(ReviewOwningDto reviewOwningDto, Review review) {
        if ( reviewOwningDto == null ) {
            return review;
        }

        if ( reviewOwningDto.getCreatedAt() != null ) {
            review.setCreatedAt( reviewOwningDto.getCreatedAt() );
        }
        if ( reviewOwningDto.getUpdatedAt() != null ) {
            review.setUpdatedAt( reviewOwningDto.getUpdatedAt() );
        }
        if ( reviewOwningDto.getEnabled() != null ) {
            review.setEnabled( reviewOwningDto.getEnabled() );
        }
        if ( reviewOwningDto.getNote() != null ) {
            review.setNote( reviewOwningDto.getNote() );
        }
        if ( reviewOwningDto.getId() != null ) {
            review.setId( Long.parseLong( reviewOwningDto.getId() ) );
        }
        if ( reviewOwningDto.getRating() != null ) {
            review.setRating( reviewOwningDto.getRating() );
        }
        if ( reviewOwningDto.getComment() != null ) {
            review.setComment( reviewOwningDto.getComment() );
        }
        if ( reviewOwningDto.getBook() != null ) {
            if ( review.getBook() == null ) {
                review.setBook( Book.builder().build() );
            }
            bookMapper.partialUpdate( reviewOwningDto.getBook(), review.getBook() );
        }

        return review;
    }
}
