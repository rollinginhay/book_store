package sd_009.bookstore.util.mapper.book;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.dto.jsonApiResource.book.GenreOwningDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Genre;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class GenreOwningMapperImpl implements GenreOwningMapper {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Genre toEntity(GenreOwningDto genreOwningDto) {
        if ( genreOwningDto == null ) {
            return null;
        }

        Genre.GenreBuilder genre = Genre.builder();

        if ( genreOwningDto.getId() != null ) {
            genre.id( Long.parseLong( genreOwningDto.getId() ) );
        }
        genre.name( genreOwningDto.getName() );
        genre.books( bookDtoListToBookList( genreOwningDto.getBooks() ) );

        return genre.build();
    }

    @Override
    public GenreOwningDto toDto(Genre genre) {
        if ( genre == null ) {
            return null;
        }

        List<BookDto> books = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;
        String id = null;
        String name = null;

        books = bookListToBookDtoList( genre.getBooks() );
        createdAt = genre.getCreatedAt();
        updatedAt = genre.getUpdatedAt();
        enabled = genre.getEnabled();
        note = genre.getNote();
        if ( genre.getId() != null ) {
            id = String.valueOf( genre.getId() );
        }
        name = genre.getName();

        GenreOwningDto genreOwningDto = new GenreOwningDto( createdAt, updatedAt, enabled, note, id, name, books );

        return genreOwningDto;
    }

    @Override
    public Genre partialUpdate(GenreOwningDto genreOwningDto, Genre genre) {
        if ( genreOwningDto == null ) {
            return genre;
        }

        if ( genreOwningDto.getCreatedAt() != null ) {
            genre.setCreatedAt( genreOwningDto.getCreatedAt() );
        }
        if ( genreOwningDto.getUpdatedAt() != null ) {
            genre.setUpdatedAt( genreOwningDto.getUpdatedAt() );
        }
        if ( genreOwningDto.getEnabled() != null ) {
            genre.setEnabled( genreOwningDto.getEnabled() );
        }
        if ( genreOwningDto.getNote() != null ) {
            genre.setNote( genreOwningDto.getNote() );
        }
        if ( genreOwningDto.getId() != null ) {
            genre.setId( Long.parseLong( genreOwningDto.getId() ) );
        }
        if ( genreOwningDto.getName() != null ) {
            genre.setName( genreOwningDto.getName() );
        }
        if ( genre.getBooks() != null ) {
            List<Book> list = bookDtoListToBookList( genreOwningDto.getBooks() );
            if ( list != null ) {
                genre.getBooks().clear();
                genre.getBooks().addAll( list );
            }
        }
        else {
            List<Book> list = bookDtoListToBookList( genreOwningDto.getBooks() );
            if ( list != null ) {
                genre.setBooks( list );
            }
        }

        return genre;
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
