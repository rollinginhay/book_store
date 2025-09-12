package hn_152.bookstore.util.mapper.book;

import hn_152.bookstore.dto.response.book.BookDto;
import hn_152.bookstore.entity.book.Book;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {PublisherMapper.class, SeriesMapper.class})public interface BookMapper {
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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)Book partialUpdate(BookDto bookDto, @MappingTarget Book book);
}