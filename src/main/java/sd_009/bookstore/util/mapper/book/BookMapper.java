package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Genre;
import sd_009.bookstore.repository.GenreClosureRepository;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                CreatorMapper.class, GenreMapper.class, TagMapper.class,
                ReviewMapper.class, PublisherMapper.class, BookDetailMapper.class, SeriesMapper.class
        }
)
public interface BookMapper {

    Book toEntity(BookDto dto);

    @Mapping(target = "genres", ignore = true)
    BookDto toDto(Book book,
                  @Context GenreClosureRepository repo,
                  @Context GenreMapper genreMapper);

    @AfterMapping
    default void mapGenres(Book book,
                           @MappingTarget BookDto.BookDtoBuilder builder,
                           @Context GenreClosureRepository repo,
                           @Context GenreMapper genreMapper) {

        // ⭐⭐ lấy đúng genres mà sách đang có trong DB
        var genres = book.getGenres()
                .stream()
                .map(genreMapper::toDto)
                .toList();

        builder.genres(genres);
    }


    // ⭐⭐⭐ ADD LẠI METHOD NÀY — KHÔNG CÓ LÀ SẼ LỖI ⭐⭐⭐
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book partialUpdate(BookDto bookDto, @MappingTarget Book book);
}
