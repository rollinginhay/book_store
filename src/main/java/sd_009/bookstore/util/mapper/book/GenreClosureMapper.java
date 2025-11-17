package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.GenreClosureDto;
import sd_009.bookstore.entity.book.GenreClosure;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING
)
public interface GenreClosureMapper {

    GenreClosure toEntity(GenreClosureDto dto);

    GenreClosureDto toDto(GenreClosure entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GenreClosure partialUpdate(GenreClosureDto dto, @MappingTarget GenreClosure entity);
}

