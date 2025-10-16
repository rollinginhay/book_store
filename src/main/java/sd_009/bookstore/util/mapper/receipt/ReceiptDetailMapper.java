package sd_009.bookstore.util.mapper.receipt;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDetailDto;
import sd_009.bookstore.entity.receipt.ReceiptDetail;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReceiptDetailMapper {
    ReceiptDetail toEntity(ReceiptDetailDto receiptDetailDto);

    ReceiptDetailDto toDto(ReceiptDetail receiptDetail);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ReceiptDetail partialUpdate(ReceiptDetailDto receiptDetailDto, @MappingTarget ReceiptDetail receiptDetail);
}