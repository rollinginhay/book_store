package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.SeriesDto;
import sd_009.bookstore.entity.book.Series;
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SeriesMapper {
    Series toEntity(SeriesDto seriesDto);

    SeriesDto toDto(Series series);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Series partialUpdate(SeriesDto seriesDto, @MappingTarget Series series);
}