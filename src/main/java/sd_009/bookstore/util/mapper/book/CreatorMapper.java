package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.CreatorDto;
import sd_009.bookstore.entity.book.Creator;
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CreatorMapper {
    Creator toEntity(CreatorDto creatorDto);

    CreatorDto toDto(Creator creator);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Creator partialUpdate(CreatorDto creatorDto, @MappingTarget Creator creator);
}