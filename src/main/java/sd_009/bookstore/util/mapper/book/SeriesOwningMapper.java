package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.entity.book.Series;
import sd_009.bookstore.dto.jsonApiResource.book.SeriesOwningDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BookMapper.class})
public interface SeriesOwningMapper {
    Series toEntity(SeriesOwningDto seriesOwningDto);

    @AfterMapping
    default void linkBooks(@MappingTarget Series series) {
        series.getBooks().forEach(book -> book.setSeries(series));
    }

    SeriesOwningDto toDto(Series series);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Series partialUpdate(SeriesOwningDto seriesOwningDto, @MappingTarget Series series);
}