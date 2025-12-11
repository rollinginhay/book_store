package sd_009.bookstore.util.mapper.book;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.dto.jsonApiResource.book.TagOwningDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Tag;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class TagOwningMapperImpl implements TagOwningMapper {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Tag toEntity(TagOwningDto tagOwningDto) {
        if ( tagOwningDto == null ) {
            return null;
        }

        Tag.TagBuilder tag = Tag.builder();

        if ( tagOwningDto.getId() != null ) {
            tag.id( Long.parseLong( tagOwningDto.getId() ) );
        }
        tag.name( tagOwningDto.getName() );
        tag.books( bookDtoListToBookList( tagOwningDto.getBooks() ) );

        return tag.build();
    }

    @Override
    public TagOwningDto toDto(Tag tag) {
        if ( tag == null ) {
            return null;
        }

        List<BookDto> books = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;
        String id = null;
        String name = null;

        books = bookListToBookDtoList( tag.getBooks() );
        createdAt = tag.getCreatedAt();
        updatedAt = tag.getUpdatedAt();
        enabled = tag.getEnabled();
        note = tag.getNote();
        if ( tag.getId() != null ) {
            id = String.valueOf( tag.getId() );
        }
        name = tag.getName();

        TagOwningDto tagOwningDto = new TagOwningDto( createdAt, updatedAt, enabled, note, id, name, books );

        return tagOwningDto;
    }

    @Override
    public Tag partialUpdate(TagOwningDto tagOwningDto, Tag tag) {
        if ( tagOwningDto == null ) {
            return tag;
        }

        if ( tagOwningDto.getCreatedAt() != null ) {
            tag.setCreatedAt( tagOwningDto.getCreatedAt() );
        }
        if ( tagOwningDto.getUpdatedAt() != null ) {
            tag.setUpdatedAt( tagOwningDto.getUpdatedAt() );
        }
        if ( tagOwningDto.getEnabled() != null ) {
            tag.setEnabled( tagOwningDto.getEnabled() );
        }
        if ( tagOwningDto.getNote() != null ) {
            tag.setNote( tagOwningDto.getNote() );
        }
        if ( tagOwningDto.getId() != null ) {
            tag.setId( Long.parseLong( tagOwningDto.getId() ) );
        }
        if ( tagOwningDto.getName() != null ) {
            tag.setName( tagOwningDto.getName() );
        }
        if ( tag.getBooks() != null ) {
            List<Book> list = bookDtoListToBookList( tagOwningDto.getBooks() );
            if ( list != null ) {
                tag.getBooks().clear();
                tag.getBooks().addAll( list );
            }
        }
        else {
            List<Book> list = bookDtoListToBookList( tagOwningDto.getBooks() );
            if ( list != null ) {
                tag.setBooks( list );
            }
        }

        return tag;
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
