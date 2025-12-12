package sd_009.bookstore.util.mapper.cart;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.cart.CartDetailDto;
import sd_009.bookstore.entity.cart.CartDetail;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public interface CartDetailMapper {

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "bookDetailId", target = "bookDetail.id")
    CartDetail toEntity(CartDetailDto dto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "bookDetail.id", target = "bookDetailId")
    CartDetailDto toDto(CartDetail entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "bookDetailId", target = "bookDetail.id")
    CartDetail partialUpdate(CartDetailDto dto, @MappingTarget CartDetail entity);

}
