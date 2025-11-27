package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.entity.book.BookDetail;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface BookDetailMapper {

    @Mapping(target = "book", ignore = true)   // <<<<<< THÊM DÒNG NÀY
    BookDetailDto toDto(BookDetail entity);

    @Mapping(target = "book", ignore = true)
    BookDetail toEntity(BookDetailDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BookDetail partialUpdate(BookDetailDto dto, @MappingTarget BookDetail entity);
}
