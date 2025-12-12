package sd_009.bookstore.util.mapper.book;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.dto.jsonApiResource.book.SeriesOwningDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Series;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class SeriesOwningMapperImpl implements SeriesOwningMapper {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Series toEntity(SeriesOwningDto seriesOwningDto) {
        if ( seriesOwningDto == null ) {
            return null;
        }

        Series.SeriesBuilder series = Series.builder();

        if ( seriesOwningDto.getId() != null ) {
            series.id( Long.parseLong( seriesOwningDto.getId() ) );
        }
        series.name( seriesOwningDto.getName() );
        series.books( bookDtoListToBookList( seriesOwningDto.getBooks() ) );

        Series seriesResult = series.build();

        linkBooks( seriesResult );

        return seriesResult;
    }

    @Override
    public SeriesOwningDto toDto(Series series) {
        if ( series == null ) {
            return null;
        }

        List<BookDto> books = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;
        String id = null;
        String name = null;

        books = bookListToBookDtoList( series.getBooks() );
        createdAt = series.getCreatedAt();
        updatedAt = series.getUpdatedAt();
        enabled = series.getEnabled();
        note = series.getNote();
        if ( series.getId() != null ) {
            id = String.valueOf( series.getId() );
        }
        name = series.getName();

        SeriesOwningDto seriesOwningDto = new SeriesOwningDto( createdAt, updatedAt, enabled, note, id, name, books );

        return seriesOwningDto;
    }

    @Override
    public Series partialUpdate(SeriesOwningDto seriesOwningDto, Series series) {
        if ( seriesOwningDto == null ) {
            return series;
        }

        if ( seriesOwningDto.getCreatedAt() != null ) {
            series.setCreatedAt( seriesOwningDto.getCreatedAt() );
        }
        if ( seriesOwningDto.getUpdatedAt() != null ) {
            series.setUpdatedAt( seriesOwningDto.getUpdatedAt() );
        }
        if ( seriesOwningDto.getEnabled() != null ) {
            series.setEnabled( seriesOwningDto.getEnabled() );
        }
        if ( seriesOwningDto.getNote() != null ) {
            series.setNote( seriesOwningDto.getNote() );
        }
        if ( seriesOwningDto.getId() != null ) {
            series.setId( Long.parseLong( seriesOwningDto.getId() ) );
        }
        if ( seriesOwningDto.getName() != null ) {
            series.setName( seriesOwningDto.getName() );
        }
        if ( series.getBooks() != null ) {
            List<Book> list = bookDtoListToBookList( seriesOwningDto.getBooks() );
            if ( list != null ) {
                series.getBooks().clear();
                series.getBooks().addAll( list );
            }
        }
        else {
            List<Book> list = bookDtoListToBookList( seriesOwningDto.getBooks() );
            if ( list != null ) {
                series.setBooks( list );
            }
        }

        linkBooks( series );

        return series;
    }

    protected List<Book> bookDtoListToBookList(List<BookDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Book> list1 = new ArrayList<Book>( list.size() );
        for ( BookDto bookDto : list ) {
            list1.add( bookMapper.toEntity( bookDto ) );
        }

        return list1;
    }

    protected List<BookDto> bookListToBookDtoList(List<Book> list) {
        if ( list == null ) {
            return null;
        }

        List<BookDto> list1 = new ArrayList<BookDto>( list.size() );
        for ( Book book : list ) {
            list1.add( bookMapper.toDto( book ) );
        }

        return list1;
    }
}
