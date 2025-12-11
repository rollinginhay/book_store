package sd_009.bookstore.util.mapper.receipt;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.receipt.PaymentDetailDto;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.PaymentType;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class PaymentDetailMapperImpl implements PaymentDetailMapper {

    @Override
    public PaymentDetail toEntity(PaymentDetailDto paymentDetailDto) {
        if ( paymentDetailDto == null ) {
            return null;
        }

        PaymentDetail.PaymentDetailBuilder paymentDetail = PaymentDetail.builder();

        if ( paymentDetailDto.getId() != null ) {
            paymentDetail.id( Long.parseLong( paymentDetailDto.getId() ) );
        }
        paymentDetail.paymentType( paymentDetailDto.getPaymentType() );
        paymentDetail.provider( paymentDetailDto.getProvider() );
        paymentDetail.providerId( paymentDetailDto.getProviderId() );
        paymentDetail.amount( paymentDetailDto.getAmount() );

        return paymentDetail.build();
    }

    @Override
    public PaymentDetailDto toDto(PaymentDetail paymentDetail) {
        if ( paymentDetail == null ) {
            return null;
        }

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;
        String id = null;
        PaymentType paymentType = null;
        String provider = null;
        String providerId = null;
        Double amount = null;

        createdAt = paymentDetail.getCreatedAt();
        updatedAt = paymentDetail.getUpdatedAt();
        enabled = paymentDetail.getEnabled();
        note = paymentDetail.getNote();
        if ( paymentDetail.getId() != null ) {
            id = String.valueOf( paymentDetail.getId() );
        }
        paymentType = paymentDetail.getPaymentType();
        provider = paymentDetail.getProvider();
        providerId = paymentDetail.getProviderId();
        amount = paymentDetail.getAmount();

        PaymentDetailDto paymentDetailDto = new PaymentDetailDto( createdAt, updatedAt, enabled, note, id, paymentType, provider, providerId, amount );

        return paymentDetailDto;
    }

    @Override
    public PaymentDetail partialUpdate(PaymentDetailDto paymentDetailDto, PaymentDetail paymentDetail) {
        if ( paymentDetailDto == null ) {
            return paymentDetail;
        }

        if ( paymentDetailDto.getCreatedAt() != null ) {
            paymentDetail.setCreatedAt( paymentDetailDto.getCreatedAt() );
        }
        if ( paymentDetailDto.getUpdatedAt() != null ) {
            paymentDetail.setUpdatedAt( paymentDetailDto.getUpdatedAt() );
        }
        if ( paymentDetailDto.getEnabled() != null ) {
            paymentDetail.setEnabled( paymentDetailDto.getEnabled() );
        }
        if ( paymentDetailDto.getNote() != null ) {
            paymentDetail.setNote( paymentDetailDto.getNote() );
        }
        if ( paymentDetailDto.getId() != null ) {
            paymentDetail.setId( Long.parseLong( paymentDetailDto.getId() ) );
        }
        if ( paymentDetailDto.getPaymentType() != null ) {
            paymentDetail.setPaymentType( paymentDetailDto.getPaymentType() );
        }
        if ( paymentDetailDto.getProvider() != null ) {
            paymentDetail.setProvider( paymentDetailDto.getProvider() );
        }
        if ( paymentDetailDto.getProviderId() != null ) {
            paymentDetail.setProviderId( paymentDetailDto.getProviderId() );
        }
        if ( paymentDetailDto.getAmount() != null ) {
            paymentDetail.setAmount( paymentDetailDto.getAmount() );
        }

        return paymentDetail;
    }
}
