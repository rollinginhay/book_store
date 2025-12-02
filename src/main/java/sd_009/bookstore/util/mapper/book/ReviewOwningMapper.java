package sd_009.bookstore.util.mapper.book;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.book.ReviewOwningDto;
import sd_009.bookstore.entity.book.Review;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BookMapper.class})
public interface ReviewOwningMapper {
    Review toEntity(ReviewOwningDto reviewOwningDto);

    ReviewOwningDto toDto(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Review partialUpdate(ReviewOwningDto reviewOwningDto, @MappingTarget Review review);
}