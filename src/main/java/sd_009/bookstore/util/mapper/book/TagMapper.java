package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.TagDto;
import sd_009.bookstore.entity.book.Tag;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {
    Tag toEntity(TagDto tagDto);

    TagDto toDto(Tag tag);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tag partialUpdate(TagDto tagDto, @MappingTarget Tag tag);
}