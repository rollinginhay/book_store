package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailOwningDto;
import sd_009.bookstore.entity.book.BookDetail;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookDetailOwningMapper {
    BookDetail toEntity(BookDetailOwningDto bookDetailOwningDto);

    BookDetailOwningDto toDto(BookDetail bookDetail);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BookDetail partialUpdate(BookDetailOwningDto bookDetailOwningDto, @MappingTarget BookDetail bookDetail);
}