package sd_009.bookstore.util.mapper.receipt;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDto;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.util.mapper.user.UserMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, UserMapper.class, PaymentDetailMapper.class})
public interface ReceiptMapper {
    Receipt toEntity(ReceiptDto receiptDto);

    @AfterMapping
    default void linkPaymentDetail(@MappingTarget Receipt receipt) {
        PaymentDetail paymentDetail = receipt.getPaymentDetail();
        if (paymentDetail != null) {
            paymentDetail.setReceipt(receipt);
        }
    }

    @AfterMapping
    default void linkReceiptDetails(@MappingTarget Receipt receipt) {
        receipt.getReceiptDetails().forEach(receiptDetail -> receiptDetail.setReceipt(receipt));
    }

    ReceiptDto toDto(Receipt receipt);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Receipt partialUpdate(ReceiptDto receiptDto, @MappingTarget Receipt receipt);
}