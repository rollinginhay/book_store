package sd_009.bookstore.util.mapper.book;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.ReviewDto;
import sd_009.bookstore.entity.book.Review;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review toEntity(ReviewDto reviewDto) {
        if ( reviewDto == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        if ( reviewDto.getId() != null ) {
            review.id( Long.parseLong( reviewDto.getId() ) );
        }
        review.rating( reviewDto.getRating() );
        review.comment( reviewDto.getComment() );

        return review.build();
    }

    @Override
    public ReviewDto toDto(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewDto.ReviewDtoBuilder reviewDto = ReviewDto.builder();

        reviewDto.createdAt( review.getCreatedAt() );
        reviewDto.updatedAt( review.getUpdatedAt() );
        reviewDto.enabled( review.getEnabled() );
        reviewDto.note( review.getNote() );
        if ( review.getId() != null ) {
            reviewDto.id( String.valueOf( review.getId() ) );
        }
        reviewDto.rating( review.getRating() );
        reviewDto.comment( review.getComment() );

        return reviewDto.build();
    }

    @Override
    public Review partialUpdate(ReviewDto reviewDto, Review review) {
        if ( reviewDto == null ) {
            return review;
        }

        if ( reviewDto.getCreatedAt() != null ) {
            review.setCreatedAt( reviewDto.getCreatedAt() );
        }
        if ( reviewDto.getUpdatedAt() != null ) {
            review.setUpdatedAt( reviewDto.getUpdatedAt() );
        }
        if ( reviewDto.getEnabled() != null ) {
            review.setEnabled( reviewDto.getEnabled() );
        }
        if ( reviewDto.getNote() != null ) {
            review.setNote( reviewDto.getNote() );
        }
        if ( reviewDto.getId() != null ) {
            review.setId( Long.parseLong( reviewDto.getId() ) );
        }
        if ( reviewDto.getRating() != null ) {
            review.setRating( reviewDto.getRating() );
        }
        if ( reviewDto.getComment() != null ) {
            review.setComment( reviewDto.getComment() );
        }

        return review;
    }
}
