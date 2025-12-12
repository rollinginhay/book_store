package sd_009.bookstore.util.mapper.book;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.SeriesDto;
import sd_009.bookstore.entity.book.Series;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class SeriesMapperImpl implements SeriesMapper {

    @Override
    public Series toEntity(SeriesDto seriesDto) {
        if ( seriesDto == null ) {
            return null;
        }

        Series.SeriesBuilder series = Series.builder();

        if ( seriesDto.getId() != null ) {
            series.id( Long.parseLong( seriesDto.getId() ) );
        }
        series.name( seriesDto.getName() );

        return series.build();
    }

    @Override
    public SeriesDto toDto(Series series) {
        if ( series == null ) {
            return null;
        }

        SeriesDto.SeriesDtoBuilder seriesDto = SeriesDto.builder();

        seriesDto.createdAt( series.getCreatedAt() );
        seriesDto.updatedAt( series.getUpdatedAt() );
        seriesDto.enabled( series.getEnabled() );
        seriesDto.note( series.getNote() );
        if ( series.getId() != null ) {
            seriesDto.id( String.valueOf( series.getId() ) );
        }
        seriesDto.name( series.getName() );

        return seriesDto.build();
    }

    @Override
    public Series partialUpdate(SeriesDto seriesDto, Series series) {
        if ( seriesDto == null ) {
            return series;
        }

        if ( seriesDto.getCreatedAt() != null ) {
            series.setCreatedAt( seriesDto.getCreatedAt() );
        }
        if ( seriesDto.getUpdatedAt() != null ) {
            series.setUpdatedAt( seriesDto.getUpdatedAt() );
        }
        if ( seriesDto.getEnabled() != null ) {
            series.setEnabled( seriesDto.getEnabled() );
        }
        if ( seriesDto.getNote() != null ) {
            series.setNote( seriesDto.getNote() );
        }
        if ( seriesDto.getId() != null ) {
            series.setId( Long.parseLong( seriesDto.getId() ) );
        }
        if ( seriesDto.getName() != null ) {
            series.setName( seriesDto.getName() );
        }

        return series;
    }
}
