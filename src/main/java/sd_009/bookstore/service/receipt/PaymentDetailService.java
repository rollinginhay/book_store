package sd_009.bookstore.service.receipt;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.receipt.PaymentDetailDto;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.repository.PaymentDetailRepository;
import sd_009.bookstore.repository.ReceiptRepository;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.receipt.PaymentDetailMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentDetailService {
    private final JsonApiAdapterProvider adapterProvider;
    private final JsonApiValidator jsonApiValidator;

    private final PaymentDetailMapper paymentDetailMapper;
    private final PaymentDetailRepository paymentDetailRepository;
    private final ReceiptRepository receiptRepository;

    @Transactional
    public String findByReceiptId(Boolean enabled, Long bookId) {
        Receipt book = receiptRepository.findById(bookId).orElseThrow();

        List<PaymentDetail> paymentDetails = paymentDetailRepository.findByReceipt(book);

        List<PaymentDetailDto> dtos = paymentDetails.stream().map(paymentDetailMapper::toDto).toList();

        Document<List<PaymentDetailDto>> doc = Document.with(dtos).links(
                Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.MULTI_RECEIPT_RELATIONSHIP_PAYMENT_DETAIL, book.getId()))
                        .build().toMap())
        ).build();

        return getListAdapter().toJson(doc);
    }

    @Transactional
    public String findById(Long id) {

        PaymentDetail found = paymentDetailRepository.findById(id).orElseThrow();

        PaymentDetailDto dto = paymentDetailMapper.toDto(found);

        Document<PaymentDetailDto> doc = Document
                .with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_PAYMENT_DETAIL_BY_ID, id))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    @Transactional
    public String save(String json) {
        PaymentDetailDto dto = jsonApiValidator.readAndValidate(json, PaymentDetailDto.class);
        PaymentDetail saved = paymentDetailRepository.save(paymentDetailMapper.toEntity(dto));
        return getSingleAdapter().toJson(Document
                .with(paymentDetailMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_PAYMENT_DETAIL_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public String update(String json) {
        PaymentDetailDto dto = jsonApiValidator.readAndValidate(json, PaymentDetailDto.class);
        if (dto.getId() == null) {
            throw new BadRequestException("No identifier found");
        }
        PaymentDetail existing = paymentDetailRepository.findById(Long.valueOf(dto.getId())).orElseThrow();
        PaymentDetail saved = paymentDetailRepository.save(paymentDetailMapper.partialUpdate(dto, existing));

        return getSingleAdapter().toJson(Document
                .with(paymentDetailMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_PAYMENT_DETAIL_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public void delete(Long id) {
        paymentDetailRepository.findById(id).ifPresent(e -> {
            //need to be deleted from shopping carts
            //receipts query any bookdetail status
            //need to disable from campaign detail
            e.setEnabled(false);
            paymentDetailRepository.save(e);
        });
    }

    private JsonAdapter<Document<PaymentDetailDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(PaymentDetailDto.class);
    }

    private JsonAdapter<Document<List<PaymentDetailDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(PaymentDetailDto.class);
    }

}
