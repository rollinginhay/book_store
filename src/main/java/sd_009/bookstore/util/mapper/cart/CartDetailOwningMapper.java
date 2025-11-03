package sd_009.bookstore.util.mapper.cart;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.cart.CartDetailOwningDto;
import sd_009.bookstore.entity.cart.CartDetail;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                sd_009.bookstore.util.mapper.book.BookDetailMapper.class,
                sd_009.bookstore.util.mapper.user.UserMapper.class
        }
)
public interface CartDetailOwningMapper {

    CartDetail toEntity(CartDetailOwningDto dto);

    CartDetailOwningDto toDto(CartDetail entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CartDetail partialUpdate(CartDetailOwningDto dto, @MappingTarget CartDetail entity);
}
