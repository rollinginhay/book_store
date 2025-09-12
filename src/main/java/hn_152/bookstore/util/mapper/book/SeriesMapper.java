package hn_152.bookstore.util.mapper.book;

import hn_152.bookstore.dto.response.book.SeriesDto;
import hn_152.bookstore.entity.book.Series;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SeriesMapper {
    Series toEntity(SeriesDto seriesDto);

    SeriesDto toDto(Series series);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Series partialUpdate(SeriesDto seriesDto, @MappingTarget Series series);
}