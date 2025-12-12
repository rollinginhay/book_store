package sd_009.bookstore.util.mapper.book;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.entity.book.BookDetail;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class BookDetailMapperImpl implements BookDetailMapper {

    @Override
    public BookDetail toEntity(BookDetailDto bookDetailDto) {
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

    @Override
    public BookDetailDto toDto(BookDetail bookDetail) {
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

    @Override
    public BookDetail partialUpdate(BookDetailDto bookDetailDto, BookDetail bookDetail) {
        if ( bookDetailDto == null ) {
            return bookDetail;
        }

        if ( bookDetailDto.getCreatedAt() != null ) {
            bookDetail.setCreatedAt( bookDetailDto.getCreatedAt() );
        }
        if ( bookDetailDto.getUpdatedAt() != null ) {
            bookDetail.setUpdatedAt( bookDetailDto.getUpdatedAt() );
        }
        if ( bookDetailDto.getEnabled() != null ) {
            bookDetail.setEnabled( bookDetailDto.getEnabled() );
        }
        if ( bookDetailDto.getNote() != null ) {
            bookDetail.setNote( bookDetailDto.getNote() );
        }
        if ( bookDetailDto.getId() != null ) {
            bookDetail.setId( Long.parseLong( bookDetailDto.getId() ) );
        }
        if ( bookDetailDto.getIsbn() != null ) {
            bookDetail.setIsbn( bookDetailDto.getIsbn() );
        }
        if ( bookDetailDto.getBookFormat() != null ) {
            bookDetail.setBookFormat( bookDetailDto.getBookFormat() );
        }
        if ( bookDetailDto.getDimensions() != null ) {
            bookDetail.setDimensions( bookDetailDto.getDimensions() );
        }
        if ( bookDetailDto.getPrintLength() != null ) {
            bookDetail.setPrintLength( bookDetailDto.getPrintLength() );
        }
        if ( bookDetailDto.getStock() != null ) {
            bookDetail.setStock( bookDetailDto.getStock() );
        }
        if ( bookDetailDto.getSupplyPrice() != null ) {
            bookDetail.setSupplyPrice( bookDetailDto.getSupplyPrice() );
        }
        if ( bookDetailDto.getSalePrice() != null ) {
            bookDetail.setSalePrice( bookDetailDto.getSalePrice() );
        }
        if ( bookDetailDto.getBookCondition() != null ) {
            bookDetail.setBookCondition( bookDetailDto.getBookCondition() );
        }

        return bookDetail;
    }
}
