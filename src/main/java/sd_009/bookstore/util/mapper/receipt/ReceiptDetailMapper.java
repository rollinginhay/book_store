package sd_009.bookstore.util.mapper.receipt;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDetailDto;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.receipt.ReceiptDetail;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ReceiptDetailMapper {

    // DTO -> Entity
    @Mapping(
            target = "bookCopy",
            expression = "java(mapBookCopy(receiptDetailDto.getBookDetailId()))"
    )
    ReceiptDetail toEntity(ReceiptDetailDto receiptDetailDto);

    // Entity -> DTO
    @Mapping(
            target = "bookDetailId",
            source = "bookCopy.id"
    )
    ReceiptDetailDto toDto(ReceiptDetail receiptDetail);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ReceiptDetail partialUpdate(ReceiptDetailDto receiptDetailDto, @MappingTarget ReceiptDetail receiptDetail);

    // helper: từ id → BookDetail entity (chỉ set id, không query DB)
    default BookDetail mapBookCopy(Long id) {
        if (id == null) return null;
        // Validate that id is a valid Long (not a string like "combo-...")
        // This should not happen if frontend sends correct data, but add defensive check
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid bookDetailId: " + id + ". Expected a positive Long value.");
        }
        BookDetail bd = new BookDetail();
        bd.setId(id);
        return bd;
    }
}
