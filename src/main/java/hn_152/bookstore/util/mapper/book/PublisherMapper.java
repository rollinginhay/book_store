package hn_152.bookstore.util.mapper.book;

import hn_152.bookstore.dto.response.book.PublisherDto;
import hn_152.bookstore.entity.book.Publisher;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PublisherMapper {
    Publisher toEntity(PublisherDto publisherDto);

    PublisherDto toDto(Publisher publisher);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Publisher partialUpdate(PublisherDto publisherDto, @MappingTarget Publisher publisher);
}