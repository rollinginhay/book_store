package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.CreatorOwningDto;
import sd_009.bookstore.entity.book.Creator;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BookMapper.class})
public interface CreatorOwningMapper {
    Creator toEntity(CreatorOwningDto creatorOwningDto);

    CreatorOwningDto toDto(Creator creator);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Creator partialUpdate(CreatorOwningDto creatorOwningDto, @MappingTarget Creator creator);
}