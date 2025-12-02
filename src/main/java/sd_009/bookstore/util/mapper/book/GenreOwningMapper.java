package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.GenreOwningDto;
import sd_009.bookstore.entity.book.Genre;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BookMapper.class})
public interface GenreOwningMapper {
    Genre toEntity(GenreOwningDto genreOwningDto);

    GenreOwningDto toDto(Genre genre);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Genre partialUpdate(GenreOwningDto genreOwningDto, @MappingTarget Genre genre);
}