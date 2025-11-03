package sd_009.bookstore.util.mapper.cart;

import org.mapstruct.*;
import sd_009.bookstore.entity.cart.CartDetail;
import sd_009.bookstore.dto.jsonApiResource.cart.CartDetailDto;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CartDetailMapper {

    CartDetail toEntity(CartDetailDto dto);

    CartDetailDto toDto(CartDetail entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CartDetail partialUpdate(CartDetailDto dto, @MappingTarget CartDetail entity);
}

