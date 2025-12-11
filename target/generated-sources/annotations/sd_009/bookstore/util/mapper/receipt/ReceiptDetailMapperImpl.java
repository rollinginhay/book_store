package sd_009.bookstore.util.mapper.receipt;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDetailDto;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.receipt.ReceiptDetail;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class ReceiptDetailMapperImpl implements ReceiptDetailMapper {

    @Override
    public ReceiptDetail toEntity(ReceiptDetailDto receiptDetailDto) {
        if ( receiptDetailDto == null ) {
            return null;
        }

        ReceiptDetail.ReceiptDetailBuilder receiptDetail = ReceiptDetail.builder();

        if ( receiptDetailDto.getId() != null ) {
            receiptDetail.id( Long.parseLong( receiptDetailDto.getId() ) );
        }
        receiptDetail.bookCopy( bookDetailDtoToBookDetail( receiptDetailDto.getBookCopy() ) );
        receiptDetail.pricePerUnit( receiptDetailDto.getPricePerUnit() );
        if ( receiptDetailDto.getQuantity() != null ) {
            receiptDetail.quantity( receiptDetailDto.getQuantity().longValue() );
        }

        return receiptDetail.build();
    }

    @Override
    public ReceiptDetailDto toDto(ReceiptDetail receiptDetail) {
        if ( receiptDetail == null ) {
            return null;
        }

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;
        String id = null;
        BookDetailDto bookCopy = null;
        Double pricePerUnit = null;
        Double quantity = null;

        createdAt = receiptDetail.getCreatedAt();
        updatedAt = receiptDetail.getUpdatedAt();
        enabled = receiptDetail.getEnabled();
        note = receiptDetail.getNote();
        if ( receiptDetail.getId() != null ) {
            id = String.valueOf( receiptDetail.getId() );
        }
        bookCopy = bookDetailToBookDetailDto( receiptDetail.getBookCopy() );
        pricePerUnit = receiptDetail.getPricePerUnit();
        if ( receiptDetail.getQuantity() != null ) {
            quantity = receiptDetail.getQuantity().doubleValue();
        }

        ReceiptDetailDto receiptDetailDto = new ReceiptDetailDto( createdAt, updatedAt, enabled, note, id, bookCopy, pricePerUnit, quantity );

        return receiptDetailDto;
    }

    @Override
    public ReceiptDetail partialUpdate(ReceiptDetailDto receiptDetailDto, ReceiptDetail receiptDetail) {
        if ( receiptDetailDto == null ) {
            return receiptDetail;
        }

        if ( receiptDetailDto.getCreatedAt() != null ) {
            receiptDetail.setCreatedAt( receiptDetailDto.getCreatedAt() );
        }
        if ( receiptDetailDto.getUpdatedAt() != null ) {
            receiptDetail.setUpdatedAt( receiptDetailDto.getUpdatedAt() );
        }
        if ( receiptDetailDto.getEnabled() != null ) {
            receiptDetail.setEnabled( receiptDetailDto.getEnabled() );
        }
        if ( receiptDetailDto.getNote() != null ) {
            receiptDetail.setNote( receiptDetailDto.getNote() );
        }
        if ( receiptDetailDto.getId() != null ) {
            receiptDetail.setId( Long.parseLong( receiptDetailDto.getId() ) );
        }
        if ( receiptDetailDto.getBookCopy() != null ) {
            if ( receiptDetail.getBookCopy() == null ) {
                receiptDetail.setBookCopy( BookDetail.builder().build() );
            }
            bookDetailDtoToBookDetail1( receiptDetailDto.getBookCopy(), receiptDetail.getBookCopy() );
        }
        if ( receiptDetailDto.getPricePerUnit() != null ) {
            receiptDetail.setPricePerUnit( receiptDetailDto.getPricePerUnit() );
        }
        if ( receiptDetailDto.getQuantity() != null ) {
            receiptDetail.setQuantity( receiptDetailDto.getQuantity().longValue() );
        }

        return receiptDetail;
    }

    protected BookDetail bookDetailDtoToBookDetail(BookDetailDto bookDetailDto) {
        if ( bookDetailDto == null ) {
            return null;
        }

        BookDetail.BookDetailBuilder bookDetail = BookDetail.builder();

        if ( bookDetailDto.getId() != null ) {
            bookDetail.id( Long.parseLong( bookDetailDto.getId() ) );
        }
        bookDetail.isbn( bookDetailDto.getIsbn() );
        bookDetail.bookFormat( bookDetailDto.getBookFormat() );
        bookDetail.dimensions( bookDetailDto.getDimensions() );
        bookDetail.printLength( bookDetailDto.getPrintLength() );
        bookDetail.stock( bookDetailDto.getStock() );
        bookDetail.supplyPrice( bookDetailDto.getSupplyPrice() );
        bookDetail.salePrice( bookDetailDto.getSalePrice() );
        bookDetail.bookCondition( bookDetailDto.getBookCondition() );

        return bookDetail.build();
    }

    protected BookDetailDto bookDetailToBookDetailDto(BookDetail bookDetail) {
        if ( bookDetail == null ) {
            return null;
        }

        BookDetailDto.BookDetailDtoBuilder bookDetailDto = BookDetailDto.builder();

        bookDetailDto.createdAt( bookDetail.getCreatedAt() );
        bookDetailDto.updatedAt( bookDetail.getUpdatedAt() );
        bookDetailDto.enabled( bookDetail.getEnabled() );
        bookDetailDto.note( bookDetail.getNote() );
        if ( bookDetail.getId() != null ) {
            bookDetailDto.id( String.valueOf( bookDetail.getId() ) );
        }
        bookDetailDto.isbn( bookDetail.getIsbn() );
        bookDetailDto.bookFormat( bookDetail.getBookFormat() );
        bookDetailDto.dimensions( bookDetail.getDimensions() );
        bookDetailDto.printLength( bookDetail.getPrintLength() );
        bookDetailDto.stock( bookDetail.getStock() );
        bookDetailDto.supplyPrice( bookDetail.getSupplyPrice() );
        bookDetailDto.salePrice( bookDetail.getSalePrice() );
        bookDetailDto.bookCondition( bookDetail.getBookCondition() );

        return bookDetailDto.build();
    }

    protected void bookDetailDtoToBookDetail1(BookDetailDto bookDetailDto, BookDetail mappingTarget) {
        if ( bookDetailDto == null ) {
            return;
        }

        if ( bookDetailDto.getCreatedAt() != null ) {
            mappingTarget.setCreatedAt( bookDetailDto.getCreatedAt() );
        }
        if ( bookDetailDto.getUpdatedAt() != null ) {
            mappingTarget.setUpdatedAt( bookDetailDto.getUpdatedAt() );
        }
        if ( bookDetailDto.getEnabled() != null ) {
            mappingTarget.setEnabled( bookDetailDto.getEnabled() );
        }
        if ( bookDetailDto.getNote() != null ) {
            mappingTarget.setNote( bookDetailDto.getNote() );
        }
        if ( bookDetailDto.getId() != null ) {
            mappingTarget.setId( Long.parseLong( bookDetailDto.getId() ) );
        }
        if ( bookDetailDto.getIsbn() != null ) {
            mappingTarget.setIsbn( bookDetailDto.getIsbn() );
        }
        if ( bookDetailDto.getBookFormat() != null ) {
            mappingTarget.setBookFormat( bookDetailDto.getBookFormat() );
        }
        if ( bookDetailDto.getDimensions() != null ) {
            mappingTarget.setDimensions( bookDetailDto.getDimensions() );
        }
        if ( bookDetailDto.getPrintLength() != null ) {
            mappingTarget.setPrintLength( bookDetailDto.getPrintLength() );
        }
        if ( bookDetailDto.getStock() != null ) {
            mappingTarget.setStock( bookDetailDto.getStock() );
        }
        if ( bookDetailDto.getSupplyPrice() != null ) {
            mappingTarget.setSupplyPrice( bookDetailDto.getSupplyPrice() );
        }
        if ( bookDetailDto.getSalePrice() != null ) {
            mappingTarget.setSalePrice( bookDetailDto.getSalePrice() );
        }
        if ( bookDetailDto.getBookCondition() != null ) {
            mappingTarget.setBookCondition( bookDetailDto.getBookCondition() );
        }
    }
}
