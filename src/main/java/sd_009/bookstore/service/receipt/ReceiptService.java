package sd_009.bookstore.service.receipt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptResponseDto;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.receipt.*;
import sd_009.bookstore.entity.user.User;
import sd_009.bookstore.repository.*;
import sd_009.bookstore.service.mail.EmailBuilder;
import sd_009.bookstore.service.mail.EmailService;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.link.LinkParamMapper;
import sd_009.bookstore.util.mapper.receipt.PaymentDetailMapper;
import sd_009.bookstore.util.mapper.receipt.ReceiptDetailMapper;
import sd_009.bookstore.util.mapper.receipt.ReceiptMapper;
import sd_009.bookstore.util.mapper.receipt.ReceiptResponseMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class ReceiptService {
    private final JsonApiAdapterProvider adapterProvider;
    private final JsonApiValidator validator;
    private final ReceiptMapper receiptMapper;
    private final ReceiptResponseMapper receiptResponseMapper;
    private final ReceiptDetailMapper receiptDetailMapper;
    private final ReceiptRepository receiptRepository;
    private final ReceiptDetailRepository receiptDetailRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final PaymentDetailMapper paymentDetailMapper;
    private final UserRepository userRepository;
    private final BookDetailRepository bookDetailRepository;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;

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

    //Hiển thị receipt ở admin
    @Transactional(readOnly = true)
    public String findForList(Boolean enabled, Pageable pageable) {

        Page<Receipt> page = receiptRepository.findByEnabled(enabled, pageable);

        List<ReceiptResponseDto> dtos = page.getContent()
                .stream()
                .map(receiptResponseMapper::toDto) // mapper CŨ, KHÔNG SỬA
                .toList();

        LinkParamMapper<?> paramMapper = LinkParamMapper.<Receipt>builder()
                .enabled(enabled)
                .page(page)
                .build();

        Document<List<ReceiptResponseDto>> doc = Document
                .with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLinkWithQuery(
                                Routes.GET_RECEIPTS,
                                paramMapper.getSelfParams()
                        ))
                        .first(LinkMapper.toLinkWithQuery(Routes.GET_RECEIPTS, paramMapper.getFirstParams()))
                        .last(LinkMapper.toLinkWithQuery(Routes.GET_RECEIPTS, paramMapper.getLastParams()))
                        .next(paramMapper.getNextParams() == null ? null :
                                LinkMapper.toLinkWithQuery(Routes.GET_RECEIPTS, paramMapper.getNextParams()))
                        .prev(paramMapper.getPrevParams() == null ? null :
                                LinkMapper.toLinkWithQuery(Routes.GET_RECEIPTS, paramMapper.getPrevParams()))
                        .build().toMap()))
                .build();

        return adapterProvider
                .listResourceAdapter(ReceiptResponseDto.class)
                .toJson(doc);
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
        Receipt receipt = buildEntityWithRelationships(json);
        receiptDetailRepository.saveAll(receipt.getReceiptDetails());
        paymentDetailRepository.save(receipt.getPaymentDetail());
        receipt.setPaymentDate(LocalDateTime.now());
        Receipt saved = receiptRepository.save(receipt);
        return getSingleAdapter().toJson(Document
                .with(receiptMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_RECEIPT_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public String saveOneline(String json) {

        // Build receipt from JSON (attributes + basic fields)
        Receipt receipt = buildEntityWithRelationships1(json);

        //------------------------------------------
        // LẤY CUSTOMER_ID & EMPLOYEE_ID TỪ JSON
        //------------------------------------------
        JsonNode root;
        try {
            root = objectMapper.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JSON");
        }

        JsonNode relationships = root.path("data").path("relationships");

        // CUSTOMER
        if (relationships.has("customer")) {
            String customerId = relationships
                    .path("customer").path("data").path("id").asText();

            User customer = userRepository.findById(Long.valueOf(customerId))
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            receipt.setCustomer(customer);
        }

        // EMPLOYEE
        if (relationships.has("employee") && !relationships.path("employee").path("data").isMissingNode()) {
            JsonNode employeeNode = relationships.path("employee").path("data");
            if (!employeeNode.isNull() && employeeNode.has("id")) {
                String employeeId = employeeNode.path("id").asText();
                if (!employeeId.isEmpty()) {
                    User employee = userRepository.findById(Long.valueOf(employeeId))
                            .orElseThrow(() -> new RuntimeException("Employee not found"));
                    receipt.setEmployee(employee);
                }
            }
        }


        //------------------------------------------
        // SAVE RECEIPT
        //------------------------------------------
        Receipt savedReceipt = receiptRepository.save(receipt);

        //------------------------------------------
        // SAVE RECEIPT DETAILS
        //------------------------------------------
        if (receipt.getReceiptDetails() != null && !receipt.getReceiptDetails().isEmpty()) {
            receipt.getReceiptDetails().forEach(rd -> rd.setReceipt(savedReceipt));
            receiptDetailRepository.saveAll(receipt.getReceiptDetails());
        }

        //------------------------------------------
        // SAVE PAYMENT DETAIL
        //------------------------------------------
        if (receipt.getPaymentDetail() != null) {
            receipt.getPaymentDetail().setReceipt(savedReceipt);
            paymentDetailRepository.save(receipt.getPaymentDetail());
        }

        //------------------------------------------
        // TRẢ VỀ DTO JSON:API
        //------------------------------------------
        Receipt finalReceipt =
                receiptRepository.findWithDetailsById(savedReceipt.getId()).orElseThrow();

//------------------------------------------
// 📧 GỬI MAIL XÁC NHẬN ĐƠN HÀNG (LẦN 1)
//------------------------------------------
        boolean shouldSendMail =
                finalReceipt.getCustomer() != null
                        && finalReceipt.getCustomer().getEmail() != null
                        && (
                        finalReceipt.getOrderType() == OrderType.ONLINE
                                || (
                                finalReceipt.getOrderType() == OrderType.DIRECT
                                        && Boolean.TRUE.equals(finalReceipt.getHasShipping())
                        )
                );

        if (shouldSendMail) {
            try {
                emailService.sendOrderEmail(
                        finalReceipt.getCustomer().getEmail(),
                        "Xác nhận đơn hàng #" + finalReceipt.getId(),
                        EmailBuilder.buildOrderEmail(
                                finalReceipt,
                                finalReceipt.getPaymentDetail()
                        )
                );
            } catch (Exception e) {
                log.error(
                        "❌ Gửi mail xác nhận đơn hàng thất bại - receiptId={}",
                        finalReceipt.getId(),
                        e
                );
                throw new RuntimeException("Không gửi được email xác nhận đơn hàng");
            }
//            catch (Exception e) {
//                log.error("Mail lỗi nhưng vẫn cho tạo đơn", e);
//            }

        }



        return getSingleAdapter().toJson(
                Document.with(receiptMapper.toDto(finalReceipt))
                        .links(Links.from(JsonApiLinksObject.builder()
                                .self(LinkMapper.toLink(Routes.GET_RECEIPT_BY_ID, finalReceipt.getId()))
                                .build().toMap()))
                        .build()
        );
    }


    @Transactional
    public String update(String json) {
        Receipt receipt = buildEntityWithRelationships(json);

        Receipt saved = receiptRepository.save(receipt);
        return getSingleAdapter().toJson(Document
                .with(receiptMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_RECEIPT_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public Receipt buildEntityWithRelationships(String json) {
        ReceiptDto dto = validator.readAndValidate(json, ReceiptDto.class);

        List<ReceiptDetail> receiptDetails = dto.getReceiptDetails() == null ? List.of() :
                dto.getReceiptDetails()
                        .stream()
                        .map(e -> receiptDetailMapper.toEntity(e))
                        .toList();

        receiptDetails.forEach(e -> {
            ReceiptDetailDto receiptDetailDto = dto.getReceiptDetails().stream().filter(rdDto -> e.getId().toString().equals(rdDto.getId())).findFirst().get();
            BookDetail bookDetail = bookDetailRepository.findById(Long.valueOf(receiptDetailDto.getBookCopy().getId())).orElseThrow();
            e.setBookCopy(bookDetail);
        });
        receiptDetails.forEach(e -> e.setId(null));

        User employee = dto.getEmployee() == null ? null : userRepository.findById(Long.valueOf(dto.getEmployee().getId())).orElse(null);
        User customer = dto.getCustomer() == null ? null : userRepository.findById(Long.valueOf(dto.getCustomer().getId())).orElse(null);

        Receipt receipt = receiptMapper.toEntity(dto);
        if (receipt.getId() == 0) receipt.setId(null);

        //calculate fields
        if (receipt.getReceiptDetails() != null && !receipt.getReceiptDetails().isEmpty()) {
            Double subtotal = receiptDetails.stream()
                    .mapToDouble(e -> e.getPricePerUnit() * e.getQuantity())
                    .sum();

            Double taxRate = 8D;
            Double serviceCost = 0D;

            if (receipt.getHasShipping()) serviceCost += 30000;

            Double grandTotal = (subtotal - dto.getDiscount()) * (100 + taxRate) / 100 + serviceCost;

            receipt.setTax(taxRate);
            receipt.setSubTotal(subtotal);
            receipt.setDiscount(dto.getDiscount());
            receipt.setServiceCost(serviceCost);
            receipt.setGrandTotal(grandTotal);

        }
        PaymentDetail paymentDetail = null;
        if (dto.getOrderType() == OrderType.DIRECT && !dto.getHasShipping()) {
            paymentDetail = PaymentDetail.builder()
                    .amount(receipt.getGrandTotal())
                    .paymentType(dto.getPaymentDetail().getPaymentType())
                    .receipt(receipt)
                    .build();
            receipt.setOrderStatus(OrderStatus.PAID);
        }

        if (dto.getOrderType() == OrderType.DIRECT && dto.getHasShipping()) {
            paymentDetail = PaymentDetail.builder()
                    .amount(receipt.getGrandTotal())
                    .paymentType(dto.getPaymentDetail().getPaymentType())
                    .receipt(receipt)
                    .build();
            receipt.setOrderStatus(OrderStatus.IN_TRANSIT);
        }
        receipt.setPaymentDetail(paymentDetail);
        receipt.setReceiptDetails(receiptDetails);
        receiptDetails.forEach(e -> e.setReceipt(receipt));
        receipt.setCustomer(customer);
        receipt.setEmployee(employee);
        return receipt;
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
    public Receipt buildEntityWithRelationships1(String json) {
        System.out.println("===== START buildEntityWithRelationships1 =====");

        // 1. Parse JSON -> DTO
        ReceiptDto dto = validator.readAndValidate(json, ReceiptDto.class);

        System.out.println("DTO receiptDetails size: " + (dto.getReceiptDetails() == null ? 0 : dto.getReceiptDetails().size()));

        // 2. Tạo Receipt entity từ DTO
        Receipt receipt = receiptMapper.toEntity(dto);
        if (receipt.getId() != null && receipt.getId() == 0) receipt.setId(null);

        // 3. Xử lý customer & employee
        User customer = null;
        if (dto.getCustomer() != null && dto.getCustomer().getId() != null) {
            customer = userRepository.findById(Long.valueOf(dto.getCustomer().getId()))
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            receipt.setCustomer(customer);
        }

        User employee = null;
        if (dto.getEmployee() != null && dto.getEmployee().getId() != null) {
            employee = userRepository.findById(Long.valueOf(dto.getEmployee().getId()))
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            receipt.setEmployee(employee);
        }

        // 4. Xử lý ReceiptDetails + BookCopy
        List<ReceiptDetail> receiptDetails = new ArrayList<>();
        if (dto.getReceiptDetails() != null) {
            for (ReceiptDetailDto rdDto : dto.getReceiptDetails()) {
                ReceiptDetail rd = receiptDetailMapper.toEntity(rdDto);

                if (rdDto.getBookCopy() != null && rdDto.getBookCopy().getId() != null) {
                    BookDetail bookDetail = bookDetailRepository.findById(Long.valueOf(rdDto.getBookCopy().getId()))
                            .orElseThrow(() -> new RuntimeException("BookDetail not found id: " + rdDto.getBookCopy().getId()));
                    rd.setBookCopy(bookDetail);
                }

                rd.setId(null); // để JPA tạo mới
                rd.setReceipt(receipt);
                receiptDetails.add(rd);
            }
        }
        receipt.setReceiptDetails(receiptDetails);

        System.out.println("Mapped receiptDetails size: " + receiptDetails.size());

        // 5. Tính toán subtotal, grandTotal
        double subtotal = receiptDetails.stream()
                .mapToDouble(e -> e.getPricePerUnit() * e.getQuantity())
                .sum();
        double taxRate = 8D;
        double serviceCost = receipt.getHasShipping() ? 30000 : 0;
        double grandTotal = (subtotal - dto.getDiscount()) * (100 + taxRate) / 100 + serviceCost;

        receipt.setSubTotal(subtotal);
        receipt.setTax(taxRate);
        receipt.setServiceCost(serviceCost);
        receipt.setDiscount(dto.getDiscount());
        receipt.setGrandTotal(grandTotal);

        System.out.println("Subtotal: " + subtotal + ", GrandTotal: " + grandTotal);

        // 6. Xử lý PaymentDetail
        PaymentDetail paymentDetail = PaymentDetail.builder()
                .amount(grandTotal)
                .paymentType(dto.getPaymentDetail() != null
                        ? dto.getPaymentDetail().getPaymentType()
                        : PaymentType.CASH) // mặc định COD nếu null
                .receipt(receipt)
                .build();
        receipt.setPaymentDetail(paymentDetail);

        // 7. Log cuối
        System.out.println("Final receipt entity created. Receipt id: " + receipt.getId());
        receiptDetails.forEach(rd -> {
            System.out.println("ReceiptDetail bookCopyId: " + (rd.getBookCopy() == null ? "null" : rd.getBookCopy().getId()));
            System.out.println("Quantity: " + rd.getQuantity() + ", Price: " + rd.getPricePerUnit());
        });

        System.out.println("===== END buildEntityWithRelationships1 =====");
        return receipt;
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
    @Transactional
    public Receipt updateOrderStatus(
            Long receiptId,
            OrderStatus newStatus
    ) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));

        // ✅ lấy tên HIỂN THỊ trạng thái cũ
        OrderStatus oldStatusEnum = receipt.getOrderStatus();
        String oldStatus = oldStatusEnum != null
                ? oldStatusEnum.getDisplayName()
                : "-";

        // set trạng thái mới
        receipt.setOrderStatus(newStatus);
        Receipt saved = receiptRepository.save(receipt);

        // 👉 GỬI MAIL SAU KHI SAVE
        if (saved.getCustomer() != null && saved.getCustomer().getEmail() != null) {
            emailService.sendOrderStatusEmail(
                    saved.getCustomer().getEmail(),      // mail khách
                    saved,
                    oldStatus,                           // 👈 tiếng Việt
                    newStatus.getDisplayName()           // 👈 tiếng Việt
            );
        }

        return saved;
    }

    private JsonAdapter<Document<ReceiptDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(ReceiptDto.class);
    }

    private JsonAdapter<Document<List<ReceiptDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(ReceiptDto.class);
    }
}






