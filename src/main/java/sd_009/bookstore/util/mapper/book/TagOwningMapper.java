package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.TagOwningDto;
import sd_009.bookstore.entity.book.Tag;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BookMapper.class})
public interface TagOwningMapper {
    Tag toEntity(TagOwningDto tagOwningDto);

    TagOwningDto toDto(Tag tag);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tag partialUpdate(TagOwningDto tagOwningDto, @MappingTarget Tag tag);
}