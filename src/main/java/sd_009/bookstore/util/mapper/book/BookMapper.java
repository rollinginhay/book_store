package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.entity.book.Book;
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {CreatorMapper.class, GenreMapper.class, TagMapper.class, ReviewMapper.class, PublisherMapper.class, BookDetailMapper.class, SeriesMapper.class})
public interface BookMapper {
    Book toEntity(BookDto bookDto);

    @AfterMapping
    default void linkReviews(@MappingTarget Book book) {
        book.getReviews().forEach(review -> review.setBook(book));
    }

    @AfterMapping
    default void linkBookCopies(@MappingTarget Book book) {
        book.getBookCopies().forEach(bookCopy -> bookCopy.setBook(book));
    }

    BookDto toDto(Book book);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book partialUpdate(BookDto bookDto, @MappingTarget Book book);
}