package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.entity.book.Publisher;
import sd_009.bookstore.dto.jsonApiResource.book.PublisherOwningDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BookMapper.class})
public interface PublisherOwningMapper {
    Publisher toEntity(PublisherOwningDto publisherOwningDto);

    @AfterMapping
    default void linkBooks(@MappingTarget Publisher publisher) {
        publisher.getBooks().forEach(book -> book.setPublisher(publisher));
    }

    PublisherOwningDto toDto(Publisher publisher);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Publisher partialUpdate(PublisherOwningDto publisherOwningDto, @MappingTarget Publisher publisher);
}