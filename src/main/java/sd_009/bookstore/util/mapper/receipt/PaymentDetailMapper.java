package sd_009.bookstore.util.mapper.receipt;

import org.mapstruct.*;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.dto.jsonApiResource.receipt.PaymentDetailDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentDetailMapper {
    PaymentDetail toEntity(PaymentDetailDto paymentDetailDto);

    PaymentDetailDto toDto(PaymentDetail paymentDetail);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PaymentDetail partialUpdate(PaymentDetailDto paymentDetailDto, @MappingTarget PaymentDetail paymentDetail);
}