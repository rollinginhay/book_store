package sd_009.bookstore.service.receipt;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDetailDto;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.entity.receipt.ReceiptDetail;
import sd_009.bookstore.repository.ReceiptDetailRepository;
import sd_009.bookstore.repository.ReceiptRepository;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.receipt.ReceiptDetailMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptDetailService {
    private final JsonApiAdapterProvider adapterProvider;
    private final JsonApiValidator jsonApiValidator;

    private final ReceiptDetailMapper receiptDetailMapper;
    private final ReceiptDetailRepository receiptDetailRepository;
    private final ReceiptRepository receiptRepository;

    @Transactional
    public String findByReceiptId(Boolean enabled, Long bookId) {
        Receipt book = receiptRepository.findById(bookId).orElseThrow();

        List<ReceiptDetail> receiptDetails = receiptDetailRepository.findByReceipt(book);

        List<ReceiptDetailDto> dtos = receiptDetails.stream().map(receiptDetailMapper::toDto).toList();

        Document<List<ReceiptDetailDto>> doc = Document.with(dtos).links(
                Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.MULTI_RECEIPT_RELATIONSHIP_RECEIPT_DETAIL, book.getId()))
                        .build().toMap())
        ).build();

        return getListAdapter().toJson(doc);
    }

    @Transactional
    public String findById(Long id) {

        ReceiptDetail found = receiptDetailRepository.findById(id).orElseThrow();

        ReceiptDetailDto dto = receiptDetailMapper.toDto(found);

        Document<ReceiptDetailDto> doc = Document
                .with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_RECEIPT_DETAIL_BY_ID, id))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    @Transactional
    public String save(String json) {
        ReceiptDetailDto dto = jsonApiValidator.readAndValidate(json, ReceiptDetailDto.class);

        ReceiptDetail saved = receiptDetailRepository.save(receiptDetailMapper.toEntity(dto));

        return getSingleAdapter().toJson(Document
                .with(receiptDetailMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_RECEIPT_DETAIL_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public String update(String json) {
        ReceiptDetailDto dto = jsonApiValidator.readAndValidate(json, ReceiptDetailDto.class);
        if (dto.getId() == null) {
            throw new BadRequestException("No identifier found");
        }
        ReceiptDetail existing = receiptDetailRepository.findById(Long.valueOf(dto.getId())).orElseThrow();
        ReceiptDetail saved = receiptDetailRepository.save(receiptDetailMapper.partialUpdate(dto, existing));

        return getSingleAdapter().toJson(Document
                .with(receiptDetailMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_RECEIPT_DETAIL_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public void delete(Long id) {
        receiptDetailRepository.findById(id).ifPresent(e -> {
            //need to be deleted from shopping carts
            //receipts query any bookdetail status
            //need to disable from campaign detail
            e.setEnabled(false);
            receiptDetailRepository.save(e);
        });
    }

    private JsonAdapter<Document<ReceiptDetailDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(ReceiptDetailDto.class);
    }

    private JsonAdapter<Document<List<ReceiptDetailDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(ReceiptDetailDto.class);
    }
}
