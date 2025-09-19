package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.GenreDto;
import sd_009.bookstore.entity.book.Genre;
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GenreMapper {
    Genre toEntity(GenreDto genreDto);

    GenreDto toDto(Genre genre);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Genre partialUpdate(GenreDto genreDto, @MappingTarget Genre genre);
}