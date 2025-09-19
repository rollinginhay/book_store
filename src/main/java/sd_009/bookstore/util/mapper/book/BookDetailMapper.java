package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.entity.book.BookDetail;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookDetailMapper {
    BookDetail toEntity(BookDetailDto bookDetailDto);

    BookDetailDto toDto(BookDetail bookDetail);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BookDetail partialUpdate(BookDetailDto bookDetailDto, @MappingTarget BookDetail bookDetail);
}