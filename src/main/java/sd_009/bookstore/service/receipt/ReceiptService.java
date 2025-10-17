package sd_009.bookstore.service.receipt;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.receipt.PaymentDetailDto;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDetailDto;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDto;
import sd_009.bookstore.entity.receipt.PaymentDetail;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.entity.receipt.ReceiptDetail;
import sd_009.bookstore.repository.PaymentDetailRepository;
import sd_009.bookstore.repository.ReceiptDetailRepository;
import sd_009.bookstore.repository.ReceiptRepository;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.link.LinkParamMapper;
import sd_009.bookstore.util.mapper.receipt.PaymentDetailMapper;
import sd_009.bookstore.util.mapper.receipt.ReceiptDetailMapper;
import sd_009.bookstore.util.mapper.receipt.ReceiptMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ReceiptService {
    private final JsonApiAdapterProvider adapterProvider;
    private final JsonApiValidator validator;
    private final ReceiptMapper receiptMapper;
    private final ReceiptDetailMapper receiptDetailMapper;
    private final ReceiptRepository receiptRepository;
    private final ReceiptDetailRepository receiptDetailRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final PaymentDetailMapper paymentDetailMapper;

    @Transactional
    public String find(Boolean enabled, String titleQuery, Pageable pageable) {
        Page<Receipt> page;
        if (titleQuery == null || titleQuery.isEmpty()) {
            page = receiptRepository.findByEnabled(enabled, pageable);
        } else {
            throw new BadRequestException("Query not supported");
        }
        List<ReceiptDto> dtos = page.getContent().stream().map(receiptMapper::toDto).toList();

        LinkParamMapper<?> paramMapper = LinkParamMapper.<Receipt>builder()
                .keyword(titleQuery)
                .enabled(enabled)
                .page(page)
                .build();

        Document<List<ReceiptDto>> doc = Document
                .with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLinkWithQuery(Routes.GET_RECEIPTS, paramMapper.getSelfParams()))
                        .first(LinkMapper.toLinkWithQuery(Routes.GET_RECEIPTS, paramMapper.getFirstParams()))
                        .last(LinkMapper.toLinkWithQuery(Routes.GET_RECEIPTS, paramMapper.getLastParams()))
                        //has to manually check for null in case of invalid pages
                        .next(paramMapper.getNextParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_RECEIPTS, paramMapper.getNextParams()))
                        .prev(paramMapper.getPrevParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_RECEIPTS, paramMapper.getPrevParams()))
                        .build().toMap()))
                .build();

        return getListAdapter().toJson(doc);
    }

    public String findById(Long id) {
        Receipt found = receiptRepository.findById(id).orElseThrow();

        ReceiptDto dto = receiptMapper.toDto(found);

        Document<ReceiptDto> doc = Document
                .with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_RECEIPT_BY_ID, id))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    @Transactional
    public String save(String json) {
        ReceiptDto dto = validator.readAndValidate(json, ReceiptDto.class);

        Receipt saved = receiptRepository.save(receiptMapper.toEntity(dto));
        return getSingleAdapter().toJson(Document
                .with(receiptMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_RECEIPT_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public String update(String json) {
        ReceiptDto dto = validator.readAndValidate(json, ReceiptDto.class);

        Receipt existing = receiptRepository.findById(Long.valueOf(dto.getId())).orElseThrow();

        Receipt saved = receiptRepository.save(receiptMapper.partialUpdate(dto, existing));
        return getSingleAdapter().toJson(Document
                .with(receiptMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_RECEIPT_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public void delete(Long id) {
        receiptRepository.findById(id).ifPresent(e -> {
            List<ReceiptDetail> associated = receiptDetailRepository.findByReceipt(e);

            associated.stream().peek(detail -> detail.setEnabled(false)).forEach(receiptDetailRepository::save);

            e.setEnabled(false);
            receiptRepository.save(e);
        });
    }

    @Transactional
    public String attachOrReplaceRelationship(Long receiptId, String json, String relationship) {
        Receipt receipt = receiptRepository.findById(receiptId).orElseThrow();

        Class<?> dependentType;

        switch (relationship) {
            case "receiptDetail" -> {
                dependentType = ReceiptDetailDto.class;
            }
            case "paymentDetail" -> {
                dependentType = PaymentDetailDto.class;
            }
            default ->
                    throw new BadRequestException("Invalid relationship type");
        }

        var dto = validator.readAndValidate(json, dependentType);

        switch (dto) {
            case ReceiptDetailDto receiptDetailDto -> {
                ReceiptDetail receiptDetail = receiptDetailRepository.findById(Long.valueOf(receiptDetailDto.getId())).orElseThrow();
                receiptDetail.setReceipt(receipt);
                receipt.getReceiptDetails().add(receiptDetail);
            }
            case PaymentDetailDto paymentDetailDto -> {
                PaymentDetail paymentDetail = paymentDetailRepository.findById(Long.valueOf(paymentDetailDto.getId())).orElseThrow();
                paymentDetail.setReceipt(receipt);
                receipt.setPaymentDetail(paymentDetail);
            }
            case null, default ->
                    throw new BadRequestException("Unsupported relationship type");
        }
        Receipt saved = receiptRepository.save(receipt);
        return getSingleAdapter().toJson(Document
                .with(receiptMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_RECEIPT_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public <T> String detachRelationShip(Long receiptId, String json, String relationship) {
        Receipt receipt = receiptRepository.findById(receiptId).orElseThrow();

        Class<?> dependentType;

        switch (relationship) {
            case "receiptDetail" -> {
                dependentType = ReceiptDetailDto.class;
            }
            case "paymentDetail" -> {
                dependentType = PaymentDetailDto.class;
            }

            default ->
                    throw new BadRequestException("Invalid relationship type");
        }

        var dto = validator.readAndValidate(json, dependentType);

        switch (dto) {
            case ReceiptDetailDto receiptDetailDto -> {
                ReceiptDetail receiptDetail = receiptDetailRepository.findById(Long.valueOf(receiptDetailDto.getId())).orElseThrow();
                receiptDetail.setReceipt(null);
                receipt.getReceiptDetails().remove(receiptDetail);
            }
            case PaymentDetailDto paymentDetailDto -> {
                ReceiptDetail receiptDetail = receiptDetailRepository.findById(Long.valueOf(paymentDetailDto.getId())).orElseThrow();
                receiptDetail.setReceipt(null);
                receipt.setPaymentDetail(null);
            }
            case null, default ->
                    throw new BadRequestException("Unsupported relationship type");
        }
        return getSingleAdapter().toJson(Document.with(receiptMapper.toDto(receiptRepository.save(receipt))).build());
    }

    public String getDependents(Long receiptId, String type) {
        Receipt receipt = receiptRepository.findById(receiptId).orElseThrow();

        switch (type) {
            case "receiptDetail" -> {
                List<ReceiptDetail> dependents = receiptDetailRepository.findByReceipt(receipt);
                List<ReceiptDetailDto> dtos = dependents.stream().map(receiptDetailMapper::toDto).toList();
                return adapterProvider.listResourceAdapter(ReceiptDetailDto.class).toJson(Document
                        .with(dtos)
                        .links(Links.from(JsonApiLinksObject.builder()
                                .self(LinkMapper.toLink(Routes.MULTI_RECEIPT_RELATIONSHIP_RECEIPT_DETAIL, receiptId))
                                .build().toMap()))
                        .build());
            }
            case "paymentDetail" -> {
                List<PaymentDetail> dependents = paymentDetailRepository.findByReceipt(receipt);
                List<PaymentDetailDto> dtos = dependents.stream().map(paymentDetailMapper::toDto).toList();
                return adapterProvider.listResourceAdapter(PaymentDetailDto.class).toJson(Document
                        .with(dtos)
                        .links(Links.from(JsonApiLinksObject.builder()
                                .self(LinkMapper.toLink(Routes.MULTI_RECEIPT_RELATIONSHIP_PAYMENT_DETAIL, receiptId))
                                .build().toMap()))
                        .build());
            }
            default ->
                    throw new BadRequestException("Unsupported relationship type");
        }
    }

    private JsonAdapter<Document<ReceiptDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(ReceiptDto.class);
    }

    private JsonAdapter<Document<List<ReceiptDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(ReceiptDto.class);
    }
}






