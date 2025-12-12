package sd_009.bookstore.util.mapper.receipt;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDto;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.util.mapper.user.UserMapper;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                UserMapper.class,
                PaymentDetailMapper.class,
                sd_009.bookstore.util.mapper.book.BookDetailMapper.class
        }
)
public interface ReceiptMapper {

    Receipt toEntity(ReceiptDto dto);

    @AfterMapping
    default void linkPaymentDetail(ReceiptDto dto, @MappingTarget Receipt receipt) {
        if (receipt.getPaymentDetail() != null) {
            receipt.getPaymentDetail().setReceipt(receipt);
        }
    }

    @AfterMapping
    default void linkReceiptDetails(ReceiptDto dto, @MappingTarget Receipt receipt) {
        if (dto.getReceiptDetails() != null) {
            receipt.getReceiptDetails().forEach(rd -> rd.setReceipt(receipt));
        }
    }

    // 🔥 CẮT VÒNG LẶP BOOK ↔ BOOKDETAIL
    @Mapping(target = "receiptDetails[].bookCopy.book.bookCopies", ignore = true)
    ReceiptDto toDto(Receipt receipt);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Receipt partialUpdate(ReceiptDto dto, @MappingTarget Receipt receipt);
}
