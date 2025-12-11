package sd_009.bookstore.util.mapper.book;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.GenreDto;
import sd_009.bookstore.entity.book.Genre;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:04+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class GenreMapperImpl implements GenreMapper {

    @Override
    public Genre toEntity(GenreDto genreDto) {
        if ( genreDto == null ) {
            return null;
        }

        Genre.GenreBuilder genre = Genre.builder();

        if ( genreDto.getId() != null ) {
            genre.id( Long.parseLong( genreDto.getId() ) );
        }
        genre.name( genreDto.getName() );

        return genre.build();
    }

    @Override
    public GenreDto toDto(Genre genre) {
        if ( genre == null ) {
            return null;
        }

        GenreDto.GenreDtoBuilder genreDto = GenreDto.builder();

        genreDto.createdAt( genre.getCreatedAt() );
        genreDto.updatedAt( genre.getUpdatedAt() );
        genreDto.enabled( genre.getEnabled() );
        genreDto.note( genre.getNote() );
        if ( genre.getId() != null ) {
            genreDto.id( String.valueOf( genre.getId() ) );
        }
        genreDto.name( genre.getName() );

        return genreDto.build();
    }

    @Override
    public Genre partialUpdate(GenreDto genreDto, Genre genre) {
        if ( genreDto == null ) {
            return genre;
        }

        if ( genreDto.getCreatedAt() != null ) {
            genre.setCreatedAt( genreDto.getCreatedAt() );
        }
        if ( genreDto.getUpdatedAt() != null ) {
            genre.setUpdatedAt( genreDto.getUpdatedAt() );
        }
        if ( genreDto.getEnabled() != null ) {
            genre.setEnabled( genreDto.getEnabled() );
        }
        if ( genreDto.getNote() != null ) {
            genre.setNote( genreDto.getNote() );
        }
        if ( genreDto.getId() != null ) {
            genre.setId( Long.parseLong( genreDto.getId() ) );
        }
        if ( genreDto.getName() != null ) {
            genre.setName( genreDto.getName() );
        }

        return genre;
    }
}
