package sd_009.bookstore.util.mapper.receipt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.dto.jsonApiResource.receipt.PaymentDetailDto;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDetailDto;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDto;
import sd_009.bookstore.dto.jsonApiResource.user.UserDto;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.receipt.OrderStatus;
import sd_009.bookstore.entity.receipt.OrderType;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.entity.receipt.ReceiptDetail;
import sd_009.bookstore.entity.user.User;
import sd_009.bookstore.util.mapper.user.UserMapper;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class ReceiptMapperImpl implements ReceiptMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PaymentDetailMapper paymentDetailMapper;

    @Override
    public Receipt toEntity(ReceiptDto receiptDto) {
        if ( receiptDto == null ) {
            return null;
        }

        Receipt.ReceiptBuilder receipt = Receipt.builder();

        if ( receiptDto.getId() != null ) {
            receipt.id( Long.parseLong( receiptDto.getId() ) );
        }
        receipt.subTotal( receiptDto.getSubTotal() );
        receipt.discount( receiptDto.getDiscount() );
        receipt.tax( receiptDto.getTax() );
        receipt.serviceCost( receiptDto.getServiceCost() );
        receipt.hasShipping( receiptDto.getHasShipping() );
        receipt.shippingService( receiptDto.getShippingService() );
        receipt.shippingId( receiptDto.getShippingId() );
        receipt.grandTotal( receiptDto.getGrandTotal() );
        receipt.orderType( receiptDto.getOrderType() );
        if ( receiptDto.getOrderStatus() != null ) {
            receipt.orderStatus( Enum.valueOf( OrderStatus.class, receiptDto.getOrderStatus() ) );
        }
        receipt.customer( userMapper.toEntity( receiptDto.getCustomer() ) );
        receipt.employee( userMapper.toEntity( receiptDto.getEmployee() ) );
        receipt.customerName( receiptDto.getCustomerName() );
        receipt.customerPhone( receiptDto.getCustomerPhone() );
        receipt.customerAddress( receiptDto.getCustomerAddress() );
        receipt.paymentDate( receiptDto.getPaymentDate() );
        receipt.paymentDetail( paymentDetailMapper.toEntity( receiptDto.getPaymentDetail() ) );
        receipt.receiptDetails( receiptDetailDtoListToReceiptDetailList( receiptDto.getReceiptDetails() ) );

        Receipt receiptResult = receipt.build();

        linkPaymentDetail( receiptResult );
        linkReceiptDetails( receiptResult );

        return receiptResult;
    }

    @Override
    public ReceiptDto toDto(Receipt receipt) {
        if ( receipt == null ) {
            return null;
        }

        List<ReceiptDetailDto> receiptDetails = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;
        String id = null;
        Double subTotal = null;
        Double discount = null;
        Double tax = null;
        Double serviceCost = null;
        Boolean hasShipping = null;
        String shippingService = null;
        String shippingId = null;
        Double grandTotal = null;
        String orderStatus = null;
        OrderType orderType = null;
        UserDto customer = null;
        UserDto employee = null;
        String customerName = null;
        String customerPhone = null;
        String customerAddress = null;
        LocalDateTime paymentDate = null;
        PaymentDetailDto paymentDetail = null;

        receiptDetails = receiptDetailListToReceiptDetailDtoList( receipt.getReceiptDetails() );
        createdAt = receipt.getCreatedAt();
        updatedAt = receipt.getUpdatedAt();
        enabled = receipt.getEnabled();
        note = receipt.getNote();
        if ( receipt.getId() != null ) {
            id = String.valueOf( receipt.getId() );
        }
        subTotal = receipt.getSubTotal();
        discount = receipt.getDiscount();
        tax = receipt.getTax();
        serviceCost = receipt.getServiceCost();
        hasShipping = receipt.getHasShipping();
        shippingService = receipt.getShippingService();
        shippingId = receipt.getShippingId();
        grandTotal = receipt.getGrandTotal();
        if ( receipt.getOrderStatus() != null ) {
            orderStatus = receipt.getOrderStatus().name();
        }
        orderType = receipt.getOrderType();
        customer = userMapper.toDto( receipt.getCustomer() );
        employee = userMapper.toDto( receipt.getEmployee() );
        customerName = receipt.getCustomerName();
        customerPhone = receipt.getCustomerPhone();
        customerAddress = receipt.getCustomerAddress();
        paymentDate = receipt.getPaymentDate();
        paymentDetail = paymentDetailMapper.toDto( receipt.getPaymentDetail() );

        ReceiptDto receiptDto = new ReceiptDto( createdAt, updatedAt, enabled, note, id, subTotal, discount, tax, serviceCost, hasShipping, shippingService, shippingId, grandTotal, orderStatus, orderType, customer, employee, customerName, customerPhone, customerAddress, paymentDate, paymentDetail, receiptDetails );

        return receiptDto;
    }

    @Override
    public Receipt partialUpdate(ReceiptDto receiptDto, Receipt receipt) {
        if ( receiptDto == null ) {
            return receipt;
        }

        if ( receiptDto.getCreatedAt() != null ) {
            receipt.setCreatedAt( receiptDto.getCreatedAt() );
        }
        if ( receiptDto.getUpdatedAt() != null ) {
            receipt.setUpdatedAt( receiptDto.getUpdatedAt() );
        }
        if ( receiptDto.getEnabled() != null ) {
            receipt.setEnabled( receiptDto.getEnabled() );
        }
        if ( receiptDto.getNote() != null ) {
            receipt.setNote( receiptDto.getNote() );
        }
        if ( receiptDto.getId() != null ) {
            receipt.setId( Long.parseLong( receiptDto.getId() ) );
        }
        if ( receiptDto.getSubTotal() != null ) {
            receipt.setSubTotal( receiptDto.getSubTotal() );
        }
        if ( receiptDto.getDiscount() != null ) {
            receipt.setDiscount( receiptDto.getDiscount() );
        }
        if ( receiptDto.getTax() != null ) {
            receipt.setTax( receiptDto.getTax() );
        }
        if ( receiptDto.getServiceCost() != null ) {
            receipt.setServiceCost( receiptDto.getServiceCost() );
        }
        if ( receiptDto.getHasShipping() != null ) {
            receipt.setHasShipping( receiptDto.getHasShipping() );
        }
        if ( receiptDto.getShippingService() != null ) {
            receipt.setShippingService( receiptDto.getShippingService() );
        }
        if ( receiptDto.getShippingId() != null ) {
            receipt.setShippingId( receiptDto.getShippingId() );
        }
        if ( receiptDto.getGrandTotal() != null ) {
            receipt.setGrandTotal( receiptDto.getGrandTotal() );
        }
        if ( receiptDto.getOrderType() != null ) {
            receipt.setOrderType( receiptDto.getOrderType() );
        }
        if ( receiptDto.getOrderStatus() != null ) {
            receipt.setOrderStatus( Enum.valueOf( OrderStatus.class, receiptDto.getOrderStatus() ) );
        }
        if ( receiptDto.getCustomer() != null ) {
            if ( receipt.getCustomer() == null ) {
                receipt.setCustomer( User.builder().build() );
            }
            userMapper.partialUpdate( receiptDto.getCustomer(), receipt.getCustomer() );
        }
        if ( receiptDto.getEmployee() != null ) {
            if ( receipt.getEmployee() == null ) {
                receipt.setEmployee( User.builder().build() );
            }
            userMapper.partialUpdate( receiptDto.getEmployee(), receipt.getEmployee() );
        }
        if ( receiptDto.getCustomerName() != null ) {
            receipt.setCustomerName( receiptDto.getCustomerName() );
        }
        if ( receiptDto.getCustomerPhone() != null ) {
            receipt.setCustomerPhone( receiptDto.getCustomerPhone() );
        }
        if ( receiptDto.getCustomerAddress() != null ) {
            receipt.setCustomerAddress( receiptDto.getCustomerAddress() );
        }
        if ( receiptDto.getPaymentDate() != null ) {
            receipt.setPaymentDate( receiptDto.getPaymentDate() );
        }
        if ( receiptDto.getPaymentDetail() != null ) {
            if ( receipt.getPaymentDetail() == null ) {
                receipt.setPaymentDetail( PaymentDetail.builder().build() );
            }
            paymentDetailMapper.partialUpdate( receiptDto.getPaymentDetail(), receipt.getPaymentDetail() );
        }
        if ( receipt.getReceiptDetails() != null ) {
            List<ReceiptDetail> list = receiptDetailDtoListToReceiptDetailList( receiptDto.getReceiptDetails() );
            if ( list != null ) {
                receipt.getReceiptDetails().clear();
                receipt.getReceiptDetails().addAll( list );
            }
        }
        else {
            List<ReceiptDetail> list = receiptDetailDtoListToReceiptDetailList( receiptDto.getReceiptDetails() );
            if ( list != null ) {
                receipt.setReceiptDetails( list );
            }
        }

        linkPaymentDetail( receipt );
        linkReceiptDetails( receipt );

        return receipt;
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

    protected ReceiptDetail receiptDetailDtoToReceiptDetail(ReceiptDetailDto receiptDetailDto) {
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

    protected List<ReceiptDetail> receiptDetailDtoListToReceiptDetailList(List<ReceiptDetailDto> list) {
        if ( list == null ) {
            return null;
        }

        List<ReceiptDetail> list1 = new ArrayList<ReceiptDetail>( list.size() );
        for ( ReceiptDetailDto receiptDetailDto : list ) {
            list1.add( receiptDetailDtoToReceiptDetail( receiptDetailDto ) );
        }

        return list1;
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

    protected ReceiptDetailDto receiptDetailToReceiptDetailDto(ReceiptDetail receiptDetail) {
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

    protected List<ReceiptDetailDto> receiptDetailListToReceiptDetailDtoList(List<ReceiptDetail> list) {
        if ( list == null ) {
            return null;
        }

        List<ReceiptDetailDto> list1 = new ArrayList<ReceiptDetailDto>( list.size() );
        for ( ReceiptDetail receiptDetail : list ) {
            list1.add( receiptDetailToReceiptDetailDto( receiptDetail ) );
        }

        return list1;
    }
}
