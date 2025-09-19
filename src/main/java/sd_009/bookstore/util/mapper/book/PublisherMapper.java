package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.PublisherDto;
import sd_009.bookstore.entity.book.Publisher;
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PublisherMapper {
    Publisher toEntity(PublisherDto publisherDto);

    PublisherDto toDto(Publisher publisher);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Publisher partialUpdate(PublisherDto publisherDto, @MappingTarget Publisher publisher);
}