package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import sd_009.bookstore.entity.book.Tag;
import sd_009.bookstore.dto.jsonApiResource.book.TagDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {
    Tag toEntity(TagDto tagDto);

    TagDto toDto(Tag tag);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tag partialUpdate(TagDto tagDto, @MappingTarget Tag tag);
}