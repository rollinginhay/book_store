package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.entity.book.BookDetail;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface BookDetailMapper {

    // ENTITY → DTO
    // CẮT VÒNG LẶP: BookDetailDto.book → BookDto.bookCopies → BookDetailDto → ...
    @Mapping(target = "book.bookCopies", ignore = true)
    BookDetailDto toDto(BookDetail entity);

    // DTO → ENTITY (KHÔNG ignore book để không phá logic liên kết)
    @Mapping(target = "book", ignore = true)
    BookDetail toEntity(BookDetailDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BookDetail partialUpdate(BookDetailDto dto, @MappingTarget BookDetail entity);
}
