package sd_009.bookstore.util.mapper.voucher;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.voucher.VoucherDto;
import sd_009.bookstore.entity.voucher.Voucher;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface VoucherMapper {

    Voucher toEntity(VoucherDto dto);

    VoucherDto toDto(Voucher entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Voucher partialUpdate(VoucherDto dto, @MappingTarget Voucher entity);
}

