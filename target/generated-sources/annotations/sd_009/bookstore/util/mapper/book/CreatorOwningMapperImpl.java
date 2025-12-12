package sd_009.bookstore.util.mapper.book;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.dto.jsonApiResource.book.CreatorOwningDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Creator;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class CreatorOwningMapperImpl implements CreatorOwningMapper {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Creator toEntity(CreatorOwningDto creatorOwningDto) {
        if ( creatorOwningDto == null ) {
            return null;
        }

        Creator.CreatorBuilder creator = Creator.builder();

        if ( creatorOwningDto.getId() != null ) {
            creator.id( Long.parseLong( creatorOwningDto.getId() ) );
        }
        creator.name( creatorOwningDto.getName() );
        creator.role( creatorOwningDto.getRole() );
        creator.books( bookDtoListToBookList( creatorOwningDto.getBooks() ) );

        return creator.build();
    }

    @Override
    public CreatorOwningDto toDto(Creator creator) {
        if ( creator == null ) {
            return null;
        }

        List<BookDto> books = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;
        String id = null;
        String name = null;
        String role = null;

        books = bookListToBookDtoList( creator.getBooks() );
        createdAt = creator.getCreatedAt();
        updatedAt = creator.getUpdatedAt();
        enabled = creator.getEnabled();
        note = creator.getNote();
        if ( creator.getId() != null ) {
            id = String.valueOf( creator.getId() );
        }
        name = creator.getName();
        role = creator.getRole();

        CreatorOwningDto creatorOwningDto = new CreatorOwningDto( createdAt, updatedAt, enabled, note, id, name, role, books );

        return creatorOwningDto;
    }

    @Override
    public Creator partialUpdate(CreatorOwningDto creatorOwningDto, Creator creator) {
        if ( creatorOwningDto == null ) {
            return creator;
        }

        if ( creatorOwningDto.getCreatedAt() != null ) {
            creator.setCreatedAt( creatorOwningDto.getCreatedAt() );
        }
        if ( creatorOwningDto.getUpdatedAt() != null ) {
            creator.setUpdatedAt( creatorOwningDto.getUpdatedAt() );
        }
        if ( creatorOwningDto.getEnabled() != null ) {
            creator.setEnabled( creatorOwningDto.getEnabled() );
        }
        if ( creatorOwningDto.getNote() != null ) {
            creator.setNote( creatorOwningDto.getNote() );
        }
        if ( creatorOwningDto.getId() != null ) {
            creator.setId( Long.parseLong( creatorOwningDto.getId() ) );
        }
        if ( creatorOwningDto.getName() != null ) {
            creator.setName( creatorOwningDto.getName() );
        }
        if ( creatorOwningDto.getRole() != null ) {
            creator.setRole( creatorOwningDto.getRole() );
        }
        if ( creator.getBooks() != null ) {
            List<Book> list = bookDtoListToBookList( creatorOwningDto.getBooks() );
            if ( list != null ) {
                creator.getBooks().clear();
                creator.getBooks().addAll( list );
            }
        }
        else {
            List<Book> list = bookDtoListToBookList( creatorOwningDto.getBooks() );
            if ( list != null ) {
                creator.setBooks( list );
            }
        }

        return creator;
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
