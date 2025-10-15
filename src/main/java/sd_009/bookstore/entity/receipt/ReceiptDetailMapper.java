package sd_009.bookstore.entity.receipt;

import org.mapstruct.*;
import sd_009.bookstore.util.mapper.book.BookDetailMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BookDetailMapper.class})
public interface ReceiptDetailMapper {
    ReceiptDetail toEntity(ReceiptDetailDto receiptDetailDto);

    ReceiptDetailDto toDto(ReceiptDetail receiptDetail);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ReceiptDetail partialUpdate(ReceiptDetailDto receiptDetailDto, @MappingTarget ReceiptDetail receiptDetail);
}