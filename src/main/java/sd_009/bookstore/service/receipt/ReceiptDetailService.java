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
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDetailDto;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDto;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.receipt.Receipt;
import sd_009.bookstore.entity.receipt.ReceiptDetail;
import sd_009.bookstore.repository.BookDetailRepository;
import sd_009.bookstore.repository.ReceiptDetailRepository;
import sd_009.bookstore.repository.ReceiptRepository;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.receipt.ReceiptDetailMapper;
import sd_009.bookstore.util.mapper.receipt.ReceiptMapper;
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
    private final BookDetailRepository bookDetailRepository;
    private final ReceiptMapper receiptMapper;

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
        ReceiptDto receiptDto = jsonApiValidator.readAndValidate(json, ReceiptDto.class);
        Receipt receipt = receiptRepository.findById(Long.valueOf(receiptDto.getId())).orElseThrow();

        List<ReceiptDetailDto> receiptDetailDtos = receiptDto.getReceiptDetails();

        List<ReceiptDetail> receiptDetails = receiptDetailDtos.stream().map(receiptDetailMapper::toEntity).toList();
        receiptDetails.forEach(e -> {
            if (e.getId() == 0) e.setId(null);
            BookDetail bookDetail = bookDetailRepository.findById(e.getBookCopy().getId()).orElseThrow();
            e.setPricePerUnit(Long.valueOf(bookDetail.getSalePrice()));
            e.setBookCopy(bookDetail);
            e.setReceipt(receipt);
            receiptDetailRepository.save(e);
        });

        Receipt updatedReceipt = receiptRepository.findById(Long.valueOf(receiptDto.getId())).orElseThrow();

        return adapterProvider.singleResourceAdapter(ReceiptDto.class).toJson(Document
                .with(receiptMapper.toDto(updatedReceipt))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_RECEIPT_BY_ID, updatedReceipt.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public String update(String json) {
        ReceiptDto receiptDto = jsonApiValidator.readAndValidate(json, ReceiptDto.class);
        Receipt receipt = receiptRepository.findById(Long.valueOf(receiptDto.getId())).orElseThrow();

        List<ReceiptDetailDto> receiptDetailDtos = receiptDto.getReceiptDetails();

        List<ReceiptDetail> receiptDetails = receiptDetailDtos.stream().map(receiptDetailMapper::toEntity).toList();
        receiptDetails.forEach(e -> {
            if (e.getId() == 0) e.setId(null);
            BookDetail bookDetail = bookDetailRepository.findById(e.getBookCopy().getId()).orElseThrow();
            e.setPricePerUnit(Long.valueOf(bookDetail.getSalePrice()));
            e.setBookCopy(bookDetail);
            e.setReceipt(receipt);
            receiptDetailRepository.save(e);
        });

        Receipt updatedReceipt = receiptRepository.findById(Long.valueOf(receiptDto.getId())).orElseThrow();

        return adapterProvider.singleResourceAdapter(ReceiptDto.class).toJson(Document
                .with(receiptMapper.toDto(updatedReceipt))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_RECEIPT_BY_ID, updatedReceipt.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public void delete(Long id) {
        //need to be deleted from shopping carts
        //receipts query any bookdetail status
        //need to disable from campaign detail
        receiptDetailRepository.findById(id).ifPresent(receiptDetailRepository::delete);
    }

    @Transactional
    public String attachOrReplaceRelationship(Long receiptDetailId, String json, String relationship) {
        ReceiptDetail receiptDetail = receiptDetailRepository.findById(receiptDetailId).orElseThrow();

        Class<?> dependentType;

        if (relationship.equals("bookDetail")) {
            dependentType = BookDetailDto.class;
        } else {
            throw new BadRequestException("Invalid relationship type");
        }

        var dto = jsonApiValidator.readAndValidate(json, dependentType);

        switch (dto) {
            case BookDetailDto bookDetailDto -> {
                BookDetail bookDetail = bookDetailRepository.findById(Long.valueOf(bookDetailDto.getId())).orElseThrow();
                receiptDetail.setBookCopy(bookDetail);
            }
            case null, default ->
                    throw new BadRequestException("Unsupported relationship type");
        }
        ReceiptDetail saved = receiptDetailRepository.save(receiptDetail);
        return getSingleAdapter().toJson(Document
                .with(receiptDetailMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_RECEIPT_DETAIL_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    //detach not needed, attach and replace only, otherwise delete
//    @Transactional
//    public <T> String detachRelationShip(Long receiptDetailId, String json, String relationship) {
//        ReceiptDetail receiptDetail = receiptDetailRepository.findById(receiptDetailId).orElseThrow();
//
//        Class<?> dependentType;
//
//        if (relationship.equals("bookDetail")) {
//            dependentType = BookDetailDto.class;
//        } else {
//            throw new BadRequestException("Invalid relationship type");
//        }
//
//        var dto = jsonApiValidator.readAndValidate(json, dependentType);
//
//        switch (dto) {
//            case BookDetailDto bookDetailDto -> {
//                BookDetail bookDetail = bookDetailRepository.findById(Long.valueOf(bookDetailDto.getId())).orElseThrow();
//
//                receiptDetail.setBookCopy(null);
//            }
//            case null, default ->
//                    throw new BadRequestException("Unsupported relationship type");
//        }
//        return getSingleAdapter().toJson(Document.with(receiptDetailMapper.toDto(receiptDetailRepository.save(receiptDetail))).build());
//    }

    private JsonAdapter<Document<ReceiptDetailDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(ReceiptDetailDto.class);
    }

    private JsonAdapter<Document<List<ReceiptDetailDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(ReceiptDetailDto.class);
    }
}
