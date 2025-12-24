package sd_009.bookstore.util.mapper.receipt;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptResponseDto;
import sd_009.bookstore.entity.receipt.Receipt;

import org.mapstruct.Named;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {PaymentDetailMapper.class} // nếu ReceiptResponseDto có paymentDetail
)
public interface ReceiptResponseMapper {

    @Mapping(target = "customerName", source = "customerName", qualifiedByName = "normalizeName")
    @Mapping(target = "customerPhone", source = "customerPhone", qualifiedByName = "normalizePhone")
    ReceiptResponseDto toDto(Receipt receipt);

    @Named("normalizeName")
    default String normalizeName(String name) {
        return (name == null || name.isBlank()) ? null : name;
    }

    @Named("normalizePhone")
    default String normalizePhone(String phone) {
        return (phone == null || phone.isBlank()) ? null : phone;
    }
}
