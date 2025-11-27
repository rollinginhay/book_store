package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.repository.GenreClosureRepository;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                CreatorMapper.class, GenreMapper.class, TagMapper.class,
                ReviewMapper.class, PublisherMapper.class,
                BookDetailMapper.class, SeriesMapper.class
        }
)
public interface BookMapper {

    Book toEntity(BookDto dto);

    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "id", source = "book.id")
    BookDto toDto(Book book,
                  @Context GenreClosureRepository repo,
                  @Context GenreMapper genreMapper);

    @AfterMapping
    default void after(Book book,
                       @MappingTarget BookDto.BookDtoBuilder builder,
                       @Context GenreClosureRepository repo,
                       @Context GenreMapper genreMapper) {

        builder.genres(
                book.getGenres()
                        .stream()
                        .map(genreMapper::toDto)
                        .toList()
        );
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book partialUpdate(BookDto bookDto, @MappingTarget Book book);
}
