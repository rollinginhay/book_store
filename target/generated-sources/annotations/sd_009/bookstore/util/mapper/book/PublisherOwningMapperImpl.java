package sd_009.bookstore.util.mapper.book;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.dto.jsonApiResource.book.PublisherOwningDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Publisher;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class PublisherOwningMapperImpl implements PublisherOwningMapper {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Publisher toEntity(PublisherOwningDto publisherOwningDto) {
        if ( publisherOwningDto == null ) {
            return null;
        }

        Publisher.PublisherBuilder publisher = Publisher.builder();

        if ( publisherOwningDto.getId() != null ) {
            publisher.id( Long.parseLong( publisherOwningDto.getId() ) );
        }
        publisher.name( publisherOwningDto.getName() );
        publisher.books( bookDtoListToBookList( publisherOwningDto.getBooks() ) );

        Publisher publisherResult = publisher.build();

        linkBooks( publisherResult );

        return publisherResult;
    }

    @Override
    public PublisherOwningDto toDto(Publisher publisher) {
        if ( publisher == null ) {
            return null;
        }

        List<BookDto> books = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;
        String id = null;
        String name = null;

        books = bookListToBookDtoList( publisher.getBooks() );
        createdAt = publisher.getCreatedAt();
        updatedAt = publisher.getUpdatedAt();
        enabled = publisher.getEnabled();
        note = publisher.getNote();
        if ( publisher.getId() != null ) {
            id = String.valueOf( publisher.getId() );
        }
        name = publisher.getName();

        PublisherOwningDto publisherOwningDto = new PublisherOwningDto( createdAt, updatedAt, enabled, note, id, name, books );

        return publisherOwningDto;
    }

    @Override
    public Publisher partialUpdate(PublisherOwningDto publisherOwningDto, Publisher publisher) {
        if ( publisherOwningDto == null ) {
            return publisher;
        }

        if ( publisherOwningDto.getCreatedAt() != null ) {
            publisher.setCreatedAt( publisherOwningDto.getCreatedAt() );
        }
        if ( publisherOwningDto.getUpdatedAt() != null ) {
            publisher.setUpdatedAt( publisherOwningDto.getUpdatedAt() );
        }
        if ( publisherOwningDto.getEnabled() != null ) {
            publisher.setEnabled( publisherOwningDto.getEnabled() );
        }
        if ( publisherOwningDto.getNote() != null ) {
            publisher.setNote( publisherOwningDto.getNote() );
        }
        if ( publisherOwningDto.getId() != null ) {
            publisher.setId( Long.parseLong( publisherOwningDto.getId() ) );
        }
        if ( publisherOwningDto.getName() != null ) {
            publisher.setName( publisherOwningDto.getName() );
        }
        if ( publisher.getBooks() != null ) {
            List<Book> list = bookDtoListToBookList( publisherOwningDto.getBooks() );
            if ( list != null ) {
                publisher.getBooks().clear();
                publisher.getBooks().addAll( list );
            }
        }
        else {
            List<Book> list = bookDtoListToBookList( publisherOwningDto.getBooks() );
            if ( list != null ) {
                publisher.setBooks( list );
            }
        }

        linkBooks( publisher );

        return publisher;
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
