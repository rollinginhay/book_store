package sd_009.bookstore.util.mapper.book;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailOwningDto;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.dto.jsonApiResource.book.CreatorDto;
import sd_009.bookstore.dto.jsonApiResource.book.GenreDto;
import sd_009.bookstore.dto.jsonApiResource.book.PublisherDto;
import sd_009.bookstore.dto.jsonApiResource.book.ReviewDto;
import sd_009.bookstore.dto.jsonApiResource.book.SeriesDto;
import sd_009.bookstore.dto.jsonApiResource.book.TagDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.book.Creator;
import sd_009.bookstore.entity.book.Genre;
import sd_009.bookstore.entity.book.Publisher;
import sd_009.bookstore.entity.book.Review;
import sd_009.bookstore.entity.book.Series;
import sd_009.bookstore.entity.book.Tag;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class BookDetailOwningMapperImpl implements BookDetailOwningMapper {

    @Override
    public BookDetail toEntity(BookDetailOwningDto bookDetailOwningDto) {
        if ( bookDetailOwningDto == null ) {
            return null;
        }

        BookDetail.BookDetailBuilder bookDetail = BookDetail.builder();

        if ( bookDetailOwningDto.getId() != null ) {
            bookDetail.id( Long.parseLong( bookDetailOwningDto.getId() ) );
        }
        bookDetail.book( bookDtoToBook( bookDetailOwningDto.getBook() ) );
        bookDetail.bookFormat( bookDetailOwningDto.getBookFormat() );
        bookDetail.dimensions( bookDetailOwningDto.getDimensions() );
        bookDetail.printLength( bookDetailOwningDto.getPrintLength() );
        bookDetail.stock( bookDetailOwningDto.getStock() );
        bookDetail.bookCondition( bookDetailOwningDto.getBookCondition() );

        return bookDetail.build();
    }

    @Override
    public BookDetailOwningDto toDto(BookDetail bookDetail) {
        if ( bookDetail == null ) {
            return null;
        }

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;
        String id = null;
        BookDto book = null;
        String bookFormat = null;
        String dimensions = null;
        Long printLength = null;
        Long stock = null;
        String bookCondition = null;

        createdAt = bookDetail.getCreatedAt();
        updatedAt = bookDetail.getUpdatedAt();
        enabled = bookDetail.getEnabled();
        note = bookDetail.getNote();
        if ( bookDetail.getId() != null ) {
            id = String.valueOf( bookDetail.getId() );
        }
        book = bookToBookDto( bookDetail.getBook() );
        bookFormat = bookDetail.getBookFormat();
        dimensions = bookDetail.getDimensions();
        printLength = bookDetail.getPrintLength();
        stock = bookDetail.getStock();
        bookCondition = bookDetail.getBookCondition();

        String isbn11 = null;
        String isbn13 = null;
        Long price = null;

        BookDetailOwningDto bookDetailOwningDto = new BookDetailOwningDto( createdAt, updatedAt, enabled, note, id, book, isbn11, isbn13, bookFormat, dimensions, printLength, stock, price, bookCondition );

        return bookDetailOwningDto;
    }

    @Override
    public BookDetail partialUpdate(BookDetailOwningDto bookDetailOwningDto, BookDetail bookDetail) {
        if ( bookDetailOwningDto == null ) {
            return bookDetail;
        }

        if ( bookDetailOwningDto.getCreatedAt() != null ) {
            bookDetail.setCreatedAt( bookDetailOwningDto.getCreatedAt() );
        }
        if ( bookDetailOwningDto.getUpdatedAt() != null ) {
            bookDetail.setUpdatedAt( bookDetailOwningDto.getUpdatedAt() );
        }
        if ( bookDetailOwningDto.getEnabled() != null ) {
            bookDetail.setEnabled( bookDetailOwningDto.getEnabled() );
        }
        if ( bookDetailOwningDto.getNote() != null ) {
            bookDetail.setNote( bookDetailOwningDto.getNote() );
        }
        if ( bookDetailOwningDto.getId() != null ) {
            bookDetail.setId( Long.parseLong( bookDetailOwningDto.getId() ) );
        }
        if ( bookDetailOwningDto.getBook() != null ) {
            if ( bookDetail.getBook() == null ) {
                bookDetail.setBook( Book.builder().build() );
            }
            bookDtoToBook1( bookDetailOwningDto.getBook(), bookDetail.getBook() );
        }
        if ( bookDetailOwningDto.getBookFormat() != null ) {
            bookDetail.setBookFormat( bookDetailOwningDto.getBookFormat() );
        }
        if ( bookDetailOwningDto.getDimensions() != null ) {
            bookDetail.setDimensions( bookDetailOwningDto.getDimensions() );
        }
        if ( bookDetailOwningDto.getPrintLength() != null ) {
            bookDetail.setPrintLength( bookDetailOwningDto.getPrintLength() );
        }
        if ( bookDetailOwningDto.getStock() != null ) {
            bookDetail.setStock( bookDetailOwningDto.getStock() );
        }
        if ( bookDetailOwningDto.getBookCondition() != null ) {
            bookDetail.setBookCondition( bookDetailOwningDto.getBookCondition() );
        }

        return bookDetail;
    }

    protected Creator creatorDtoToCreator(CreatorDto creatorDto) {
        if ( creatorDto == null ) {
            return null;
        }

        Creator.CreatorBuilder creator = Creator.builder();

        if ( creatorDto.getId() != null ) {
            creator.id( Long.parseLong( creatorDto.getId() ) );
        }
        creator.name( creatorDto.getName() );
        creator.role( creatorDto.getRole() );

        return creator.build();
    }

    protected List<Creator> creatorDtoListToCreatorList(List<CreatorDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Creator> list1 = new ArrayList<Creator>( list.size() );
        for ( CreatorDto creatorDto : list ) {
            list1.add( creatorDtoToCreator( creatorDto ) );
        }

        return list1;
    }

    protected Genre genreDtoToGenre(GenreDto genreDto) {
        if ( genreDto == null ) {
            return null;
        }

        Genre.GenreBuilder genre = Genre.builder();

        if ( genreDto.getId() != null ) {
            genre.id( Long.parseLong( genreDto.getId() ) );
        }
        genre.name( genreDto.getName() );

        return genre.build();
    }

    protected List<Genre> genreDtoListToGenreList(List<GenreDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Genre> list1 = new ArrayList<Genre>( list.size() );
        for ( GenreDto genreDto : list ) {
            list1.add( genreDtoToGenre( genreDto ) );
        }

        return list1;
    }

    protected Tag tagDtoToTag(TagDto tagDto) {
        if ( tagDto == null ) {
            return null;
        }

        Tag.TagBuilder tag = Tag.builder();

        if ( tagDto.getId() != null ) {
            tag.id( Long.parseLong( tagDto.getId() ) );
        }
        tag.name( tagDto.getName() );

        return tag.build();
    }

    protected List<Tag> tagDtoListToTagList(List<TagDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Tag> list1 = new ArrayList<Tag>( list.size() );
        for ( TagDto tagDto : list ) {
            list1.add( tagDtoToTag( tagDto ) );
        }

        return list1;
    }

    protected Review reviewDtoToReview(ReviewDto reviewDto) {
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

    protected List<Review> reviewDtoListToReviewList(List<ReviewDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Review> list1 = new ArrayList<Review>( list.size() );
        for ( ReviewDto reviewDto : list ) {
            list1.add( reviewDtoToReview( reviewDto ) );
        }

        return list1;
    }

    protected Publisher publisherDtoToPublisher(PublisherDto publisherDto) {
        if ( publisherDto == null ) {
            return null;
        }

        Publisher.PublisherBuilder publisher = Publisher.builder();

        if ( publisherDto.getId() != null ) {
            publisher.id( Long.parseLong( publisherDto.getId() ) );
        }
        publisher.name( publisherDto.getName() );

        return publisher.build();
    }

    protected BookDetail bookDetailDtoToBookDetail(BookDetailDto bookDetailDto) {
        if ( bookDetailDto == null ) {
            return null;
        }

        BookDetail.BookDetailBuilder bookDetail = BookDetail.builder();

        if ( bookDetailDto.getId() != null ) {
            bookDetail.id( Long.parseLong( bookDetailDto.getId() ) );
        }
        bookDetail.isbn( bookDetailDto.getIsbn() );
        bookDetail.bookFormat( bookDetailDto.getBookFormat() );
        bookDetail.dimensions( bookDetailDto.getDimensions() );
        bookDetail.printLength( bookDetailDto.getPrintLength() );
        bookDetail.stock( bookDetailDto.getStock() );
        bookDetail.supplyPrice( bookDetailDto.getSupplyPrice() );
        bookDetail.salePrice( bookDetailDto.getSalePrice() );
        bookDetail.bookCondition( bookDetailDto.getBookCondition() );

        return bookDetail.build();
    }

    protected List<BookDetail> bookDetailDtoListToBookDetailList(List<BookDetailDto> list) {
        if ( list == null ) {
            return null;
        }

        List<BookDetail> list1 = new ArrayList<BookDetail>( list.size() );
        for ( BookDetailDto bookDetailDto : list ) {
            list1.add( bookDetailDtoToBookDetail( bookDetailDto ) );
        }

        return list1;
    }

    protected Series seriesDtoToSeries(SeriesDto seriesDto) {
        if ( seriesDto == null ) {
            return null;
        }

        Series.SeriesBuilder series = Series.builder();

        if ( seriesDto.getId() != null ) {
            series.id( Long.parseLong( seriesDto.getId() ) );
        }
        series.name( seriesDto.getName() );

        return series.build();
    }

    protected Book bookDtoToBook(BookDto bookDto) {
        if ( bookDto == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        if ( bookDto.getId() != null ) {
            book.id( Long.parseLong( bookDto.getId() ) );
        }
        book.title( bookDto.getTitle() );
        book.language( bookDto.getLanguage() );
        book.edition( bookDto.getEdition() );
        book.published( bookDto.getPublished() );
        book.blurb( bookDto.getBlurb() );
        book.creators( creatorDtoListToCreatorList( bookDto.getCreators() ) );
        book.genres( genreDtoListToGenreList( bookDto.getGenres() ) );
        book.tags( tagDtoListToTagList( bookDto.getTags() ) );
        book.reviews( reviewDtoListToReviewList( bookDto.getReviews() ) );
        book.publisher( publisherDtoToPublisher( bookDto.getPublisher() ) );
        book.bookCopies( bookDetailDtoListToBookDetailList( bookDto.getBookCopies() ) );
        book.series( seriesDtoToSeries( bookDto.getSeries() ) );
        book.imageUrl( bookDto.getImageUrl() );

        return book.build();
    }

    protected CreatorDto creatorToCreatorDto(Creator creator) {
        if ( creator == null ) {
            return null;
        }

        CreatorDto.CreatorDtoBuilder creatorDto = CreatorDto.builder();

        creatorDto.createdAt( creator.getCreatedAt() );
        creatorDto.updatedAt( creator.getUpdatedAt() );
        creatorDto.enabled( creator.getEnabled() );
        creatorDto.note( creator.getNote() );
        if ( creator.getId() != null ) {
            creatorDto.id( String.valueOf( creator.getId() ) );
        }
        creatorDto.name( creator.getName() );
        creatorDto.role( creator.getRole() );

        return creatorDto.build();
    }

    protected List<CreatorDto> creatorListToCreatorDtoList(List<Creator> list) {
        if ( list == null ) {
            return null;
        }

        List<CreatorDto> list1 = new ArrayList<CreatorDto>( list.size() );
        for ( Creator creator : list ) {
            list1.add( creatorToCreatorDto( creator ) );
        }

        return list1;
    }

    protected GenreDto genreToGenreDto(Genre genre) {
        if ( genre == null ) {
            return null;
        }

        GenreDto.GenreDtoBuilder genreDto = GenreDto.builder();

        genreDto.createdAt( genre.getCreatedAt() );
        genreDto.updatedAt( genre.getUpdatedAt() );
        genreDto.enabled( genre.getEnabled() );
        genreDto.note( genre.getNote() );
        if ( genre.getId() != null ) {
            genreDto.id( String.valueOf( genre.getId() ) );
        }
        genreDto.name( genre.getName() );

        return genreDto.build();
    }

    protected List<GenreDto> genreListToGenreDtoList(List<Genre> list) {
        if ( list == null ) {
            return null;
        }

        List<GenreDto> list1 = new ArrayList<GenreDto>( list.size() );
        for ( Genre genre : list ) {
            list1.add( genreToGenreDto( genre ) );
        }

        return list1;
    }

    protected TagDto tagToTagDto(Tag tag) {
        if ( tag == null ) {
            return null;
        }

        TagDto.TagDtoBuilder tagDto = TagDto.builder();

        tagDto.createdAt( tag.getCreatedAt() );
        tagDto.updatedAt( tag.getUpdatedAt() );
        tagDto.enabled( tag.getEnabled() );
        tagDto.note( tag.getNote() );
        if ( tag.getId() != null ) {
            tagDto.id( String.valueOf( tag.getId() ) );
        }
        tagDto.name( tag.getName() );

        return tagDto.build();
    }

    protected List<TagDto> tagListToTagDtoList(List<Tag> list) {
        if ( list == null ) {
            return null;
        }

        List<TagDto> list1 = new ArrayList<TagDto>( list.size() );
        for ( Tag tag : list ) {
            list1.add( tagToTagDto( tag ) );
        }

        return list1;
    }

    protected ReviewDto reviewToReviewDto(Review review) {
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

    protected List<ReviewDto> reviewListToReviewDtoList(List<Review> list) {
        if ( list == null ) {
            return null;
        }

        List<ReviewDto> list1 = new ArrayList<ReviewDto>( list.size() );
        for ( Review review : list ) {
            list1.add( reviewToReviewDto( review ) );
        }

        return list1;
    }

    protected PublisherDto publisherToPublisherDto(Publisher publisher) {
        if ( publisher == null ) {
            return null;
        }

        PublisherDto.PublisherDtoBuilder publisherDto = PublisherDto.builder();

        publisherDto.createdAt( publisher.getCreatedAt() );
        publisherDto.updatedAt( publisher.getUpdatedAt() );
        publisherDto.enabled( publisher.getEnabled() );
        publisherDto.note( publisher.getNote() );
        if ( publisher.getId() != null ) {
            publisherDto.id( String.valueOf( publisher.getId() ) );
        }
        publisherDto.name( publisher.getName() );

        return publisherDto.build();
    }

    protected BookDetailDto bookDetailToBookDetailDto(BookDetail bookDetail) {
        if ( bookDetail == null ) {
            return null;
        }

        BookDetailDto.BookDetailDtoBuilder bookDetailDto = BookDetailDto.builder();

        bookDetailDto.createdAt( bookDetail.getCreatedAt() );
        bookDetailDto.updatedAt( bookDetail.getUpdatedAt() );
        bookDetailDto.enabled( bookDetail.getEnabled() );
        bookDetailDto.note( bookDetail.getNote() );
        if ( bookDetail.getId() != null ) {
            bookDetailDto.id( String.valueOf( bookDetail.getId() ) );
        }
        bookDetailDto.isbn( bookDetail.getIsbn() );
        bookDetailDto.bookFormat( bookDetail.getBookFormat() );
        bookDetailDto.dimensions( bookDetail.getDimensions() );
        bookDetailDto.printLength( bookDetail.getPrintLength() );
        bookDetailDto.stock( bookDetail.getStock() );
        bookDetailDto.supplyPrice( bookDetail.getSupplyPrice() );
        bookDetailDto.salePrice( bookDetail.getSalePrice() );
        bookDetailDto.bookCondition( bookDetail.getBookCondition() );

        return bookDetailDto.build();
    }

    protected List<BookDetailDto> bookDetailListToBookDetailDtoList(List<BookDetail> list) {
        if ( list == null ) {
            return null;
        }

        List<BookDetailDto> list1 = new ArrayList<BookDetailDto>( list.size() );
        for ( BookDetail bookDetail : list ) {
            list1.add( bookDetailToBookDetailDto( bookDetail ) );
        }

        return list1;
    }

    protected SeriesDto seriesToSeriesDto(Series series) {
        if ( series == null ) {
            return null;
        }

        SeriesDto.SeriesDtoBuilder seriesDto = SeriesDto.builder();

        seriesDto.createdAt( series.getCreatedAt() );
        seriesDto.updatedAt( series.getUpdatedAt() );
        seriesDto.enabled( series.getEnabled() );
        seriesDto.note( series.getNote() );
        if ( series.getId() != null ) {
            seriesDto.id( String.valueOf( series.getId() ) );
        }
        seriesDto.name( series.getName() );

        return seriesDto.build();
    }

    protected BookDto bookToBookDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDto.BookDtoBuilder bookDto = BookDto.builder();

        bookDto.createdAt( book.getCreatedAt() );
        bookDto.updatedAt( book.getUpdatedAt() );
        bookDto.enabled( book.getEnabled() );
        bookDto.note( book.getNote() );
        if ( book.getId() != null ) {
            bookDto.id( String.valueOf( book.getId() ) );
        }
        bookDto.blurb( book.getBlurb() );
        bookDto.title( book.getTitle() );
        bookDto.language( book.getLanguage() );
        bookDto.edition( book.getEdition() );
        bookDto.published( book.getPublished() );
        bookDto.creators( creatorListToCreatorDtoList( book.getCreators() ) );
        bookDto.genres( genreListToGenreDtoList( book.getGenres() ) );
        bookDto.tags( tagListToTagDtoList( book.getTags() ) );
        bookDto.reviews( reviewListToReviewDtoList( book.getReviews() ) );
        bookDto.publisher( publisherToPublisherDto( book.getPublisher() ) );
        bookDto.bookCopies( bookDetailListToBookDetailDtoList( book.getBookCopies() ) );
        bookDto.series( seriesToSeriesDto( book.getSeries() ) );
        bookDto.imageUrl( book.getImageUrl() );

        return bookDto.build();
    }

    protected void publisherDtoToPublisher1(PublisherDto publisherDto, Publisher mappingTarget) {
        if ( publisherDto == null ) {
            return;
        }

        if ( publisherDto.getCreatedAt() != null ) {
            mappingTarget.setCreatedAt( publisherDto.getCreatedAt() );
        }
        if ( publisherDto.getUpdatedAt() != null ) {
            mappingTarget.setUpdatedAt( publisherDto.getUpdatedAt() );
        }
        if ( publisherDto.getEnabled() != null ) {
            mappingTarget.setEnabled( publisherDto.getEnabled() );
        }
        if ( publisherDto.getNote() != null ) {
            mappingTarget.setNote( publisherDto.getNote() );
        }
        if ( publisherDto.getId() != null ) {
            mappingTarget.setId( Long.parseLong( publisherDto.getId() ) );
        }
        if ( publisherDto.getName() != null ) {
            mappingTarget.setName( publisherDto.getName() );
        }
    }

    protected void seriesDtoToSeries1(SeriesDto seriesDto, Series mappingTarget) {
        if ( seriesDto == null ) {
            return;
        }

        if ( seriesDto.getCreatedAt() != null ) {
            mappingTarget.setCreatedAt( seriesDto.getCreatedAt() );
        }
        if ( seriesDto.getUpdatedAt() != null ) {
            mappingTarget.setUpdatedAt( seriesDto.getUpdatedAt() );
        }
        if ( seriesDto.getEnabled() != null ) {
            mappingTarget.setEnabled( seriesDto.getEnabled() );
        }
        if ( seriesDto.getNote() != null ) {
            mappingTarget.setNote( seriesDto.getNote() );
        }
        if ( seriesDto.getId() != null ) {
            mappingTarget.setId( Long.parseLong( seriesDto.getId() ) );
        }
        if ( seriesDto.getName() != null ) {
            mappingTarget.setName( seriesDto.getName() );
        }
    }

    protected void bookDtoToBook1(BookDto bookDto, Book mappingTarget) {
        if ( bookDto == null ) {
            return;
        }

        if ( bookDto.getCreatedAt() != null ) {
            mappingTarget.setCreatedAt( bookDto.getCreatedAt() );
        }
        if ( bookDto.getUpdatedAt() != null ) {
            mappingTarget.setUpdatedAt( bookDto.getUpdatedAt() );
        }
        if ( bookDto.getEnabled() != null ) {
            mappingTarget.setEnabled( bookDto.getEnabled() );
        }
        if ( bookDto.getNote() != null ) {
            mappingTarget.setNote( bookDto.getNote() );
        }
        if ( bookDto.getId() != null ) {
            mappingTarget.setId( Long.parseLong( bookDto.getId() ) );
        }
        if ( bookDto.getTitle() != null ) {
            mappingTarget.setTitle( bookDto.getTitle() );
        }
        if ( bookDto.getLanguage() != null ) {
            mappingTarget.setLanguage( bookDto.getLanguage() );
        }
        if ( bookDto.getEdition() != null ) {
            mappingTarget.setEdition( bookDto.getEdition() );
        }
        if ( bookDto.getPublished() != null ) {
            mappingTarget.setPublished( bookDto.getPublished() );
        }
        if ( bookDto.getBlurb() != null ) {
            mappingTarget.setBlurb( bookDto.getBlurb() );
        }
        if ( mappingTarget.getCreators() != null ) {
            List<Creator> list = creatorDtoListToCreatorList( bookDto.getCreators() );
            if ( list != null ) {
                mappingTarget.getCreators().clear();
                mappingTarget.getCreators().addAll( list );
            }
        }
        else {
            List<Creator> list = creatorDtoListToCreatorList( bookDto.getCreators() );
            if ( list != null ) {
                mappingTarget.setCreators( list );
            }
        }
        if ( mappingTarget.getGenres() != null ) {
            List<Genre> list1 = genreDtoListToGenreList( bookDto.getGenres() );
            if ( list1 != null ) {
                mappingTarget.getGenres().clear();
                mappingTarget.getGenres().addAll( list1 );
            }
        }
        else {
            List<Genre> list1 = genreDtoListToGenreList( bookDto.getGenres() );
            if ( list1 != null ) {
                mappingTarget.setGenres( list1 );
            }
        }
        if ( mappingTarget.getTags() != null ) {
            List<Tag> list2 = tagDtoListToTagList( bookDto.getTags() );
            if ( list2 != null ) {
                mappingTarget.getTags().clear();
                mappingTarget.getTags().addAll( list2 );
            }
        }
        else {
            List<Tag> list2 = tagDtoListToTagList( bookDto.getTags() );
            if ( list2 != null ) {
                mappingTarget.setTags( list2 );
            }
        }
        if ( mappingTarget.getReviews() != null ) {
            List<Review> list3 = reviewDtoListToReviewList( bookDto.getReviews() );
            if ( list3 != null ) {
                mappingTarget.getReviews().clear();
                mappingTarget.getReviews().addAll( list3 );
            }
        }
        else {
            List<Review> list3 = reviewDtoListToReviewList( bookDto.getReviews() );
            if ( list3 != null ) {
                mappingTarget.setReviews( list3 );
            }
        }
        if ( bookDto.getPublisher() != null ) {
            if ( mappingTarget.getPublisher() == null ) {
                mappingTarget.setPublisher( Publisher.builder().build() );
            }
            publisherDtoToPublisher1( bookDto.getPublisher(), mappingTarget.getPublisher() );
        }
        if ( mappingTarget.getBookCopies() != null ) {
            List<BookDetail> list4 = bookDetailDtoListToBookDetailList( bookDto.getBookCopies() );
            if ( list4 != null ) {
                mappingTarget.getBookCopies().clear();
                mappingTarget.getBookCopies().addAll( list4 );
            }
        }
        else {
            List<BookDetail> list4 = bookDetailDtoListToBookDetailList( bookDto.getBookCopies() );
            if ( list4 != null ) {
                mappingTarget.setBookCopies( list4 );
            }
        }
        if ( bookDto.getSeries() != null ) {
            if ( mappingTarget.getSeries() == null ) {
                mappingTarget.setSeries( Series.builder().build() );
            }
            seriesDtoToSeries1( bookDto.getSeries(), mappingTarget.getSeries() );
        }
        if ( bookDto.getImageUrl() != null ) {
            mappingTarget.setImageUrl( bookDto.getImageUrl() );
        }
    }
}
