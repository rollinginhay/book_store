package sd_009.bookstore.util.mapper.book;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.dto.jsonApiResource.book.CreatorDto;
import sd_009.bookstore.dto.jsonApiResource.book.GenreDto;
import sd_009.bookstore.dto.jsonApiResource.book.ReviewDto;
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
public class BookMapperImpl implements BookMapper {

    @Autowired
    private CreatorMapper creatorMapper;
    @Autowired
    private GenreMapper genreMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private PublisherMapper publisherMapper;
    @Autowired
    private BookDetailMapper bookDetailMapper;
    @Autowired
    private SeriesMapper seriesMapper;

    @Override
    public Book toEntity(BookDto bookDto) {
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
        book.publisher( publisherMapper.toEntity( bookDto.getPublisher() ) );
        book.bookCopies( bookDetailDtoListToBookDetailList( bookDto.getBookCopies() ) );
        book.series( seriesMapper.toEntity( bookDto.getSeries() ) );
        book.imageUrl( bookDto.getImageUrl() );

        Book bookResult = book.build();

        linkReviews( bookResult );
        linkBookCopies( bookResult );

        return bookResult;
    }

    @Override
    public BookDto toDto(Book book) {
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
        bookDto.publisher( publisherMapper.toDto( book.getPublisher() ) );
        bookDto.bookCopies( bookDetailListToBookDetailDtoList( book.getBookCopies() ) );
        bookDto.series( seriesMapper.toDto( book.getSeries() ) );
        bookDto.imageUrl( book.getImageUrl() );

        return bookDto.build();
    }

    @Override
    public Book partialUpdate(BookDto bookDto, Book book) {
        if ( bookDto == null ) {
            return book;
        }

        if ( bookDto.getCreatedAt() != null ) {
            book.setCreatedAt( bookDto.getCreatedAt() );
        }
        if ( bookDto.getUpdatedAt() != null ) {
            book.setUpdatedAt( bookDto.getUpdatedAt() );
        }
        if ( bookDto.getEnabled() != null ) {
            book.setEnabled( bookDto.getEnabled() );
        }
        if ( bookDto.getNote() != null ) {
            book.setNote( bookDto.getNote() );
        }
        if ( bookDto.getId() != null ) {
            book.setId( Long.parseLong( bookDto.getId() ) );
        }
        if ( bookDto.getTitle() != null ) {
            book.setTitle( bookDto.getTitle() );
        }
        if ( bookDto.getLanguage() != null ) {
            book.setLanguage( bookDto.getLanguage() );
        }
        if ( bookDto.getEdition() != null ) {
            book.setEdition( bookDto.getEdition() );
        }
        if ( bookDto.getPublished() != null ) {
            book.setPublished( bookDto.getPublished() );
        }
        if ( bookDto.getBlurb() != null ) {
            book.setBlurb( bookDto.getBlurb() );
        }
        if ( book.getCreators() != null ) {
            List<Creator> list = creatorDtoListToCreatorList( bookDto.getCreators() );
            if ( list != null ) {
                book.getCreators().clear();
                book.getCreators().addAll( list );
            }
        }
        else {
            List<Creator> list = creatorDtoListToCreatorList( bookDto.getCreators() );
            if ( list != null ) {
                book.setCreators( list );
            }
        }
        if ( book.getGenres() != null ) {
            List<Genre> list1 = genreDtoListToGenreList( bookDto.getGenres() );
            if ( list1 != null ) {
                book.getGenres().clear();
                book.getGenres().addAll( list1 );
            }
        }
        else {
            List<Genre> list1 = genreDtoListToGenreList( bookDto.getGenres() );
            if ( list1 != null ) {
                book.setGenres( list1 );
            }
        }
        if ( book.getTags() != null ) {
            List<Tag> list2 = tagDtoListToTagList( bookDto.getTags() );
            if ( list2 != null ) {
                book.getTags().clear();
                book.getTags().addAll( list2 );
            }
        }
        else {
            List<Tag> list2 = tagDtoListToTagList( bookDto.getTags() );
            if ( list2 != null ) {
                book.setTags( list2 );
            }
        }
        if ( book.getReviews() != null ) {
            List<Review> list3 = reviewDtoListToReviewList( bookDto.getReviews() );
            if ( list3 != null ) {
                book.getReviews().clear();
                book.getReviews().addAll( list3 );
            }
        }
        else {
            List<Review> list3 = reviewDtoListToReviewList( bookDto.getReviews() );
            if ( list3 != null ) {
                book.setReviews( list3 );
            }
        }
        if ( bookDto.getPublisher() != null ) {
            if ( book.getPublisher() == null ) {
                book.setPublisher( Publisher.builder().build() );
            }
            publisherMapper.partialUpdate( bookDto.getPublisher(), book.getPublisher() );
        }
        if ( book.getBookCopies() != null ) {
            List<BookDetail> list4 = bookDetailDtoListToBookDetailList( bookDto.getBookCopies() );
            if ( list4 != null ) {
                book.getBookCopies().clear();
                book.getBookCopies().addAll( list4 );
            }
        }
        else {
            List<BookDetail> list4 = bookDetailDtoListToBookDetailList( bookDto.getBookCopies() );
            if ( list4 != null ) {
                book.setBookCopies( list4 );
            }
        }
        if ( bookDto.getSeries() != null ) {
            if ( book.getSeries() == null ) {
                book.setSeries( Series.builder().build() );
            }
            seriesMapper.partialUpdate( bookDto.getSeries(), book.getSeries() );
        }
        if ( bookDto.getImageUrl() != null ) {
            book.setImageUrl( bookDto.getImageUrl() );
        }

        linkReviews( book );
        linkBookCopies( book );

        return book;
    }

    protected List<Creator> creatorDtoListToCreatorList(List<CreatorDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Creator> list1 = new ArrayList<Creator>( list.size() );
        for ( CreatorDto creatorDto : list ) {
            list1.add( creatorMapper.toEntity( creatorDto ) );
        }

        return list1;
    }

    protected List<Genre> genreDtoListToGenreList(List<GenreDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Genre> list1 = new ArrayList<Genre>( list.size() );
        for ( GenreDto genreDto : list ) {
            list1.add( genreMapper.toEntity( genreDto ) );
        }

        return list1;
    }

    protected List<Tag> tagDtoListToTagList(List<TagDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Tag> list1 = new ArrayList<Tag>( list.size() );
        for ( TagDto tagDto : list ) {
            list1.add( tagMapper.toEntity( tagDto ) );
        }

        return list1;
    }

    protected List<Review> reviewDtoListToReviewList(List<ReviewDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Review> list1 = new ArrayList<Review>( list.size() );
        for ( ReviewDto reviewDto : list ) {
            list1.add( reviewMapper.toEntity( reviewDto ) );
        }

        return list1;
    }

    protected List<BookDetail> bookDetailDtoListToBookDetailList(List<BookDetailDto> list) {
        if ( list == null ) {
            return null;
        }

        List<BookDetail> list1 = new ArrayList<BookDetail>( list.size() );
        for ( BookDetailDto bookDetailDto : list ) {
            list1.add( bookDetailMapper.toEntity( bookDetailDto ) );
        }

        return list1;
    }

    protected List<CreatorDto> creatorListToCreatorDtoList(List<Creator> list) {
        if ( list == null ) {
            return null;
        }

        List<CreatorDto> list1 = new ArrayList<CreatorDto>( list.size() );
        for ( Creator creator : list ) {
            list1.add( creatorMapper.toDto( creator ) );
        }

        return list1;
    }

    protected List<GenreDto> genreListToGenreDtoList(List<Genre> list) {
        if ( list == null ) {
            return null;
        }

        List<GenreDto> list1 = new ArrayList<GenreDto>( list.size() );
        for ( Genre genre : list ) {
            list1.add( genreMapper.toDto( genre ) );
        }

        return list1;
    }

    protected List<TagDto> tagListToTagDtoList(List<Tag> list) {
        if ( list == null ) {
            return null;
        }

        List<TagDto> list1 = new ArrayList<TagDto>( list.size() );
        for ( Tag tag : list ) {
            list1.add( tagMapper.toDto( tag ) );
        }

        return list1;
    }

    protected List<ReviewDto> reviewListToReviewDtoList(List<Review> list) {
        if ( list == null ) {
            return null;
        }

        List<ReviewDto> list1 = new ArrayList<ReviewDto>( list.size() );
        for ( Review review : list ) {
            list1.add( reviewMapper.toDto( review ) );
        }

        return list1;
    }

    protected List<BookDetailDto> bookDetailListToBookDetailDtoList(List<BookDetail> list) {
        if ( list == null ) {
            return null;
        }

        List<BookDetailDto> list1 = new ArrayList<BookDetailDto>( list.size() );
        for ( BookDetail bookDetail : list ) {
            list1.add( bookDetailMapper.toDto( bookDetail ) );
        }

        return list1;
    }
}
