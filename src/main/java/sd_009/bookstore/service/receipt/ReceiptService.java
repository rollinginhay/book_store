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
import sd_009.bookstore.entity.receipt.ReceiptHistory;
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
import sd_009.bookstore.util.security.SecurityUtils;
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
    private final CartDetailRepository cartDetailRepository;
    private final ReceiptHistoryRepository receiptHistoryRepository;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final SecurityUtils securityUtils;

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
        
        // ✅ GHI LỊCH SỬ: Tạo đơn POS → PAID (từ null → PAID)
        if (saved.getOrderStatus() != null) {
            changeStatus(saved, saved.getOrderStatus(), null, "System");
        }
        
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
        // LẤY CUSTOMER_ID TỪ TOKEN (KHÔNG TỪ JSON)
        //------------------------------------------
        // Lấy userId từ token thay vì từ JSON để đảm bảo security
        Long userId = securityUtils.getCurrentUserId();
        User customer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        receipt.setCustomer(customer);

        //------------------------------------------
        // LẤY EMPLOYEE_ID TỪ JSON (nếu có)
        //------------------------------------------
        JsonNode root;
        try {
            root = objectMapper.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JSON");
        }

        JsonNode relationships = root.path("data").path("relationships");

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
        // PARSE RECEIPT DETAILS TỪ JSON RELATIONSHIPS (MỚI THÊM)
        //------------------------------------------
        List<ReceiptDetail> receiptDetailsFromJson = new java.util.ArrayList<>();
        if (relationships.has("receiptDetails")) {
            JsonNode receiptDetailsNode = relationships.path("receiptDetails").path("data");
            if (receiptDetailsNode.isArray()) {
                for (JsonNode detailNode : receiptDetailsNode) {
                    try {
                        JsonNode attributes = detailNode.path("attributes");
                        JsonNode bookDetailRel = detailNode.path("relationships").path("bookDetail").path("data");

                        if (!bookDetailRel.isMissingNode() && bookDetailRel.has("id")) {
                            String bookDetailId = bookDetailRel.path("id").asText();
                            BookDetail bookDetail = bookDetailRepository.findById(Long.valueOf(bookDetailId))
                                    .orElseThrow(() -> new RuntimeException("BookDetail not found: " + bookDetailId));

                            Long pricePerUnit = attributes.path("pricePerUnit").asLong(0);
                            Long quantity = attributes.path("quantity").asLong(1);

                            ReceiptDetail receiptDetail = new ReceiptDetail();
                            receiptDetail.setBookCopy(bookDetail);
                            receiptDetail.setPricePerUnit(pricePerUnit);
                            receiptDetail.setQuantity(quantity);
                            receiptDetail.setId(null); // Để DB tự sinh ID

                            receiptDetailsFromJson.add(receiptDetail);
                        }
                    } catch (Exception e) {
                        log.error("Lỗi parse receiptDetail từ JSON: {}", e.getMessage(), e);
                    }
                }
            }
        }

        //------------------------------------------
        // SAVE RECEIPT
        //------------------------------------------
        Receipt savedReceipt = receiptRepository.save(receipt);

        //------------------------------------------
        // SAVE RECEIPT DETAILS (TỪ buildEntityWithRelationships HOẶC TỪ JSON)
        //------------------------------------------
        List<ReceiptDetail> allReceiptDetails = new java.util.ArrayList<>();

        // Lấy từ buildEntityWithRelationships (nếu có)
        if (receipt.getReceiptDetails() != null && !receipt.getReceiptDetails().isEmpty()) {
            allReceiptDetails.addAll(receipt.getReceiptDetails());
        }

        // Thêm từ JSON relationships (nếu có)
        if (!receiptDetailsFromJson.isEmpty()) {
            allReceiptDetails.addAll(receiptDetailsFromJson);
        }

        // Lưu tất cả receiptDetails
        if (!allReceiptDetails.isEmpty()) {

            //------------------------------------------
            // ✅ CHECK TỒN KHO TRƯỚC KHI TRỪ STOCK
            // - Đảm bảo case: 2 người cùng mua, người thanh toán sau sẽ bị báo hết hàng
            //------------------------------------------
            for (ReceiptDetail rd : allReceiptDetails) {
                if (rd.getBookCopy() != null && rd.getQuantity() != null && rd.getQuantity() > 0) {
                    // Luôn đọc stock mới nhất từ DB
                    BookDetail freshBookDetail = bookDetailRepository
                            .findById(rd.getBookCopy().getId())
                            .orElseThrow(() -> new BadRequestException("BookDetail not found: " + rd.getBookCopy().getId()));
                    Long currentStock = freshBookDetail.getStock();
                    if (currentStock == null || currentStock < rd.getQuantity()) {
                        throw new BadRequestException("Sản phẩm đã hết hàng do có người khác vừa mua trước bạn.");
                    }
                }
            }

            // Nếu qua được vòng check trên thì mới set receipt & lưu chi tiết
            allReceiptDetails.forEach(rd -> rd.setReceipt(savedReceipt));
            receiptDetailRepository.saveAll(allReceiptDetails);
            
            //------------------------------------------
            // ✅ TRỪ STOCK NGAY NẾU ĐƠN ONLINE CHUYỂN KHOẢN (AUTHORIZED)
            // - ONLINE COD (PENDING): KHÔNG trừ stock, chờ xác nhận
            // - ONLINE Chuyển khoản (AUTHORIZED): TRỪ stock ngay
            //------------------------------------------
            if (receipt.getOrderStatus() == OrderStatus.AUTHORIZED) {
                // Đơn chuyển khoản: trừ stock ngay khi tạo
                for (ReceiptDetail rd : allReceiptDetails) {
                    if (rd.getBookCopy() != null && rd.getQuantity() != null && rd.getQuantity() > 0) {
                        // Luôn đọc stock mới nhất từ DB
                        BookDetail freshBookDetail = bookDetailRepository
                                .findById(rd.getBookCopy().getId())
                                .orElseThrow(() -> new BadRequestException("BookDetail not found: " + rd.getBookCopy().getId()));
                        Long currentStock = freshBookDetail.getStock();
                        if (currentStock == null || currentStock < rd.getQuantity()) {
                            throw new BadRequestException("Sản phẩm đã hết hàng do có người khác vừa mua trước bạn.");
                        }
                        // Trừ stock
                        freshBookDetail.setStock(currentStock - rd.getQuantity());
                        bookDetailRepository.save(freshBookDetail);
                        System.out.println("✅ [ReceiptService] Đã trừ stock khi tạo đơn chuyển khoản (AUTHORIZED): BookDetail " + freshBookDetail.getId() + 
                            " - Stock cũ: " + currentStock + ", Số lượng trừ: " + rd.getQuantity() + 
                            ", Stock mới: " + freshBookDetail.getStock());
                    }
                }
            }
            // ONLINE COD (PENDING): KHÔNG trừ stock ở đây, sẽ trừ khi chuyển sang AUTHORIZED
        }

        //------------------------------------------
        // SAVE PAYMENT DETAIL
        //------------------------------------------
        if (receipt.getPaymentDetail() != null) {
            receipt.getPaymentDetail().setReceipt(savedReceipt);
            paymentDetailRepository.save(receipt.getPaymentDetail());
        }

        // ✅ GHI LỊCH SỬ: Tạo đơn ONLINE → PENDING hoặc AUTHORIZED (từ null → status)
        if (savedReceipt.getOrderStatus() != null) {
            changeStatus(savedReceipt, savedReceipt.getOrderStatus(), null, "System");
        }

        //------------------------------------------
        // XÓA CÁC SẢN PHẨM TRONG GIỎ HÀNG SAU KHI ĐẶT HÀNG THÀNH CÔNG
        //------------------------------------------
        if (receipt.getCustomer() != null && !allReceiptDetails.isEmpty()) {
            try {
                // Lấy danh sách bookDetailIds từ receiptDetails
                List<Long> bookDetailIds = allReceiptDetails.stream()
                        .map(rd -> rd.getBookCopy().getId())
                        .distinct()
                        .toList();

                // Xóa các cart items của user có bookDetailId trùng với các sản phẩm đã đặt hàng
                for (Long bookDetailId : bookDetailIds) {
                    BookDetail bookDetail = bookDetailRepository.findById(bookDetailId).orElse(null);
                    if (bookDetail != null) {
                        cartDetailRepository.findByUserAndBookDetail(receipt.getCustomer(), bookDetail)
                                .ifPresent(cartDetail -> {
                                    cartDetail.setEnabled(false);
                                    cartDetailRepository.save(cartDetail);
                                    log.info("✅ Đã xóa cart item: cartDetailId={}, bookDetailId={}, userId={}",
                                            cartDetail.getId(), bookDetailId, receipt.getCustomer().getId());
                                });
                    }
                }
                log.info("✅ Đã xóa giỏ hàng sau khi đặt hàng thành công - receiptId={}, userId={}",
                        savedReceipt.getId(), receipt.getCustomer().getId());
            } catch (Exception e) {
                log.error("❌ Lỗi khi xóa giỏ hàng sau khi đặt hàng - receiptId={}, userId={}",
                        savedReceipt.getId(), receipt.getCustomer() != null ? receipt.getCustomer().getId() : null, e);
                // Không throw exception để không ảnh hưởng đến việc tạo đơn hàng
            }
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
        ReceiptDto dto = validator.readAndValidate(json, ReceiptDto.class);
        Receipt receipt;
        
        // Nếu có id, load receipt hiện tại từ DB và chỉ update field note (partial update)
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            Long receiptId = Long.valueOf(dto.getId());
            Receipt existingReceipt = receiptRepository.findWithDetailsById(receiptId)
                    .orElseThrow(() -> new BadRequestException("Receipt not found: " + receiptId));
            
            // Chỉ update field note nếu có trong DTO (giữ nguyên tất cả các field khác)
            if (dto.getNote() != null) {
                existingReceipt.setNote(dto.getNote());
            }
            
            receipt = existingReceipt;
        } else {
            // Nếu không có id, tạo mới (giữ nguyên logic cũ)
            receipt = buildEntityWithRelationships(json);
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
    public Receipt buildEntityWithRelationships(String json) {
        ReceiptDto dto = validator.readAndValidate(json, ReceiptDto.class);

        List<ReceiptDetail> receiptDetails = dto.getReceiptDetails() == null ? List.of() :
                dto.getReceiptDetails()
                        .stream()
                        .map(e -> receiptDetailMapper.toEntity(e))
                        .toList();

        // ✅ Check tồn kho nhưng CHƯA trừ stock ở đây
        // Stock sẽ được trừ sau khi set status:
        // - POS: Trừ ngay khi tạo (vì status = PAID)
        // - ONLINE: Không trừ ở đây, sẽ trừ sau
        receiptDetails.forEach(e -> {
            ReceiptDetailDto receiptDetailDto = dto.getReceiptDetails().stream().filter(rdDto -> e.getId().toString().equals(rdDto.getId())).findFirst().get();
            BookDetail bookDetail = bookDetailRepository.findById(Long.valueOf(receiptDetailDto.getBookCopy().getId())).orElseThrow();
            if (bookDetail.getStock() < e.getQuantity()) {
                throw new BadRequestException("Đơn hàng đặt sách quá số lượng tồn");
            }
            // Chỉ set bookCopy, chưa trừ stock
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

            // Bỏ VAT - không dùng nữa
            Double taxRate = 0D;
            Double serviceCost = 0D;

            if (receipt.getHasShipping()) serviceCost += 30000;

            // Công thức mới: grandTotal = subtotal - discount + serviceCost (không có VAT)
            Double grandTotal = subtotal - dto.getDiscount() + serviceCost;

            receipt.setTax(taxRate);
            receipt.setSubTotal(subtotal);
            receipt.setDiscount(dto.getDiscount());
            receipt.setServiceCost(serviceCost);
            receipt.setGrandTotal(grandTotal);

        }
        PaymentDetail paymentDetail = null;
        // ✅ Set status cho POS: Chỉ còn bán tại quầy (không ship), luôn PAID
        if (dto.getOrderType() == OrderType.DIRECT) {
            // POS: Luôn không ship, mua xong hoàn thành ngay
            paymentDetail = PaymentDetail.builder()
                    .amount(receipt.getGrandTotal())
                    .paymentType(dto.getPaymentDetail() != null 
                        ? dto.getPaymentDetail().getPaymentType() 
                        : PaymentType.CASH)
                    .receipt(receipt)
                    .build();
            // ✅ POS: Luôn PAID (mua xong hoàn thành ngay, không qua trạng thái khác)
            receipt.setOrderStatus(OrderStatus.PAID);
            // ✅ POS: Trừ số lượng ngay khi tạo (vì đã hoàn thành)
            if (receiptDetails != null && !receiptDetails.isEmpty()) {
                for (ReceiptDetail rd : receiptDetails) {
                    if (rd.getBookCopy() != null && rd.getQuantity() != null && rd.getQuantity() > 0) {
                        BookDetail bookDetail = rd.getBookCopy();
                        Long currentStock = bookDetail.getStock();
                        if (currentStock == null || currentStock < rd.getQuantity()) {
                            throw new BadRequestException("Sản phẩm đã hết hàng.");
                        }
                        bookDetail.setStock(currentStock - rd.getQuantity());
                        bookDetailRepository.save(bookDetail);
                    }
                }
            }
        } else {
            // ONLINE: Tạo paymentDetail nhưng không set status ở đây (sẽ set trong buildEntityWithRelationships1)
            paymentDetail = PaymentDetail.builder()
                    .amount(receipt.getGrandTotal())
                    .paymentType(dto.getPaymentDetail() != null 
                        ? dto.getPaymentDetail().getPaymentType() 
                        : PaymentType.CASH)
                    .receipt(receipt)
                    .build();
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
        if (receipt.getId() != null && receipt.getId() == 0)
            receipt.setId(null);

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

                // Kiểm tra bookCopy từ entity (đã được map từ bookDetailId) hoặc từ DTO
                if (rd.getBookCopy() != null && rd.getBookCopy().getId() != null) {
                    // Entity đã có bookCopy từ mapper, chỉ cần query lại để có đầy đủ thông tin
                    BookDetail bookDetail = bookDetailRepository.findById(rd.getBookCopy().getId())
                            .orElseThrow(() -> new RuntimeException("BookDetail not found id: " + rd.getBookCopy().getId()));
                    rd.setBookCopy(bookDetail);
                } else if (rdDto.getBookCopy() != null && rdDto.getBookCopy().getId() != null) {
                    // Fallback: nếu entity chưa có, lấy từ DTO relationships
                    BookDetail bookDetail = bookDetailRepository.findById(Long.valueOf(rdDto.getBookCopy().getId()))
                            .orElseThrow(() -> new RuntimeException("BookDetail not found id: " + rdDto.getBookCopy().getId()));
                    rd.setBookCopy(bookDetail);
                } else if (rdDto.getBookDetailId() != null) {
                    // Fallback: lấy từ bookDetailId trong attributes
                    BookDetail bookDetail = bookDetailRepository.findById(rdDto.getBookDetailId())
                            .orElseThrow(() -> new RuntimeException("BookDetail not found id: " + rdDto.getBookDetailId()));
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
        // Bỏ VAT - không dùng nữa
        double taxRate = 0D;
        double serviceCost = receipt.getHasShipping() ? 30000 : 0;
        // Công thức mới: grandTotal = subtotal - discount + serviceCost (không có VAT)
        double grandTotal = subtotal - dto.getDiscount() + serviceCost;

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

        // ✅ 7. Set status theo luồng mới:
        // - POS (DIRECT, không ship): PAID luôn (mua xong hoàn thành ngay)
        // - ONLINE COD (CASH): PENDING (chưa trừ số lượng, chờ xác nhận)
        // - ONLINE Chuyển khoản (TRANSFER): AUTHORIZED (trừ số lượng luôn)
        if (dto.getOrderType() == OrderType.DIRECT && !dto.getHasShipping()) {
            // POS: Hoàn thành luôn
            receipt.setOrderStatus(OrderStatus.PAID);
        } else if (dto.getOrderType() == OrderType.ONLINE) {
            // ONLINE: Phân biệt COD và Chuyển khoản
            PaymentType paymentType = dto.getPaymentDetail() != null 
                ? dto.getPaymentDetail().getPaymentType() 
                : PaymentType.CASH;
            
            if (paymentType == PaymentType.CASH) {
                // COD: PENDING (chưa trừ số lượng)
                receipt.setOrderStatus(OrderStatus.PENDING);
            } else if (paymentType == PaymentType.TRANSFER) {
                // Chuyển khoản: AUTHORIZED (trừ số lượng luôn)
                receipt.setOrderStatus(OrderStatus.AUTHORIZED);
            } else {
                // Fallback: PENDING
                receipt.setOrderStatus(OrderStatus.PENDING);
            }
        } else {
            // Fallback: PENDING
            receipt.setOrderStatus(OrderStatus.PENDING);
        }

        // 8. Log cuối
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

        // ✅ CẬP NHẬT TỒN KHO KHI THAY ĐỔI TRẠNG THÁI
        List<ReceiptDetail> receiptDetails = receiptDetailRepository.findByReceipt(receipt);
        
        //------------------------------------------
        // ✅ TRỪ STOCK KHI CHUYỂN SANG AUTHORIZED (ĐÃ XÁC NHẬN)
        //------------------------------------------
        if (newStatus == OrderStatus.AUTHORIZED && oldStatusEnum == OrderStatus.PENDING) {
            // Chỉ trừ stock khi chuyển từ PENDING sang AUTHORIZED
            for (ReceiptDetail rd : receiptDetails) {
                if (rd.getBookCopy() != null && rd.getQuantity() != null && rd.getQuantity() > 0) {
                    // Luôn đọc stock mới nhất từ DB
                    BookDetail freshBookDetail = bookDetailRepository
                            .findById(rd.getBookCopy().getId())
                            .orElseThrow(() -> new BadRequestException("BookDetail not found: " + rd.getBookCopy().getId()));
                    Long currentStock = freshBookDetail.getStock();
                    if (currentStock == null || currentStock < rd.getQuantity()) {
                        throw new BadRequestException("Sản phẩm đã hết hàng. Không thể xác nhận đơn hàng.");
                    }
                    freshBookDetail.setStock(currentStock - rd.getQuantity());
                    bookDetailRepository.save(freshBookDetail);
                    System.out.println("✅ [ReceiptService] Đã trừ stock khi xác nhận đơn (AUTHORIZED): BookDetail " + freshBookDetail.getId() + 
                        " - Stock cũ: " + currentStock + ", Số lượng trừ: " + rd.getQuantity() + 
                        ", Stock mới: " + freshBookDetail.getStock());
                }
            }
        }
        
        // ✅ Nếu chuyển sang CANCELLED: restore tồn kho (cộng lại số lượng đã trừ)
        // Restore nếu đã từng trừ stock:
        // - POS: Đã trừ khi tạo (status = PAID)
        // - ONLINE Chuyển khoản: Đã trừ khi tạo (status = AUTHORIZED)
        // - ONLINE COD: Đã trừ khi chuyển PENDING → AUTHORIZED
        if (newStatus == OrderStatus.CANCELLED && oldStatusEnum != OrderStatus.CANCELLED) {
            // Restore nếu đã từng trừ stock (PAID, AUTHORIZED, IN_TRANSIT, FAILED)
            if (oldStatusEnum == OrderStatus.PAID || 
                oldStatusEnum == OrderStatus.AUTHORIZED || 
                oldStatusEnum == OrderStatus.IN_TRANSIT ||
                oldStatusEnum == OrderStatus.FAILED) {
                for (ReceiptDetail rd : receiptDetails) {
                    if (rd.getBookCopy() != null && rd.getQuantity() != null && rd.getQuantity() > 0) {
                        BookDetail bookDetail = bookDetailRepository
                                .findById(rd.getBookCopy().getId())
                                .orElseThrow(() -> new BadRequestException("BookDetail not found: " + rd.getBookCopy().getId()));
                        Long currentStock = bookDetail.getStock();
                        bookDetail.setStock(currentStock + rd.getQuantity());
                        bookDetailRepository.save(bookDetail);
                        System.out.println("✅ [ReceiptService] Đã restore stock khi hủy đơn: BookDetail " + bookDetail.getId() + 
                            " - Stock cũ: " + currentStock + ", Số lượng restore: " + rd.getQuantity() + 
                            ", Stock mới: " + bookDetail.getStock());
                    }
                }
            }
        }
        // ✅ Nếu chuyển TỪ CANCELLED sang AUTHORIZED: trừ lại tồn kho
        else if (oldStatusEnum == OrderStatus.CANCELLED && newStatus == OrderStatus.AUTHORIZED) {
            // Khôi phục đơn từ CANCELLED → AUTHORIZED: cần trừ stock lại
            for (ReceiptDetail rd : receiptDetails) {
                if (rd.getBookCopy() != null && rd.getQuantity() != null && rd.getQuantity() > 0) {
                    // Luôn đọc stock mới nhất từ DB
                    BookDetail freshBookDetail = bookDetailRepository
                            .findById(rd.getBookCopy().getId())
                            .orElseThrow(() -> new BadRequestException("BookDetail not found: " + rd.getBookCopy().getId()));
                    Long currentStock = freshBookDetail.getStock();
                    if (currentStock == null || currentStock < rd.getQuantity()) {
                        throw new BadRequestException("Sản phẩm đã hết hàng. Không thể khôi phục đơn hàng.");
                    }
                    freshBookDetail.setStock(currentStock - rd.getQuantity());
                    bookDetailRepository.save(freshBookDetail);
                    System.out.println("✅ [ReceiptService] Đã trừ lại stock khi khôi phục đơn (CANCELLED → AUTHORIZED): BookDetail " + freshBookDetail.getId() + 
                        " - Stock cũ: " + currentStock + ", Số lượng trừ: " + rd.getQuantity() + 
                        ", Stock mới: " + freshBookDetail.getStock());
                }
            }
        }
        
        // ✅ Nếu chuyển sang REFUNDED (hoàn tiền): restore tồn kho (hàng đã trả lại)
        // REFUNDED có thể từ: PAID (trả hàng), FAILED (giao thất bại), CANCELLED (hủy sau khi đã thanh toán)
        if (newStatus == OrderStatus.REFUNDED && oldStatusEnum != OrderStatus.REFUNDED) {
            // Restore stock nếu đã từng trừ (PAID, AUTHORIZED, IN_TRANSIT, FAILED)
            if (oldStatusEnum == OrderStatus.PAID || 
                oldStatusEnum == OrderStatus.AUTHORIZED || 
                oldStatusEnum == OrderStatus.IN_TRANSIT ||
                oldStatusEnum == OrderStatus.FAILED) {
                for (ReceiptDetail rd : receiptDetails) {
                    if (rd.getBookCopy() != null && rd.getQuantity() != null && rd.getQuantity() > 0) {
                        BookDetail bookDetail = bookDetailRepository
                                .findById(rd.getBookCopy().getId())
                                .orElseThrow(() -> new BadRequestException("BookDetail not found: " + rd.getBookCopy().getId()));
                        Long currentStock = bookDetail.getStock();
                        bookDetail.setStock(currentStock + rd.getQuantity());
                        bookDetailRepository.save(bookDetail);
                        System.out.println("✅ [ReceiptService] Đã restore stock khi hoàn tiền (REFUNDED): BookDetail " + bookDetail.getId() + 
                            " - Stock cũ: " + currentStock + ", Số lượng restore: " + rd.getQuantity() + 
                            ", Stock mới: " + bookDetail.getStock());
                    }
                }
            }
        }

        // set trạng thái mới
        receipt.setOrderStatus(newStatus);
        Receipt saved = receiptRepository.save(receipt);

        // ✅ LƯU LỊCH SỬ THAY ĐỔI TRẠNG THÁI VÀO RECEIPT_HISTORY
        changeStatus(saved, newStatus, oldStatusEnum, "Admin");

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

    // ✅ HELPER: GHI LỊCH SỬ THAY ĐỔI TRẠNG THÁI
    private void changeStatus(Receipt receipt, OrderStatus newStatus, OrderStatus oldStatus, String actorName) {
        try {
            ReceiptHistory history = ReceiptHistory.builder()
                    .receipt(receipt)
                    .actorName(actorName != null ? actorName : "System")
                    .oldStatus(oldStatus)
                    .newStatus(newStatus)
                    .build();
            receiptHistoryRepository.save(history);
            System.out.println("✅ [ReceiptService] Đã lưu lịch sử: " + oldStatus + " → " + newStatus);
        } catch (Exception e) {
            System.err.println("⚠️ [ReceiptService] Lỗi khi lưu lịch sử: " + e.getMessage());
        }
    }

    // ✅ LẤY LỊCH SỬ THAY ĐỔI TRẠNG THÁI CỦA RECEIPT
    public List<ReceiptHistory> getReceiptHistory(Long receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));
        return receiptHistoryRepository.findByReceiptOrderByCreatedAtDesc(receipt);
    }

    public List<sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptHistoryDto> getReceiptHistoryDto(Long receiptId) {
        List<ReceiptHistory> historyList = getReceiptHistory(receiptId);
        return historyList.stream().map(h -> {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            return sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptHistoryDto.builder()
                    .oldStatus(h.getOldStatus())
                    .newStatus(h.getNewStatus())
                    .actorName(h.getActorName())
                    .createdAt(h.getCreatedAt() != null ? h.getCreatedAt().format(formatter) : null)
                    .updatedAt(h.getUpdatedAt() != null ? h.getUpdatedAt().format(formatter) : null)
                    .build();
        }).toList();
    }

    private JsonAdapter<Document<ReceiptDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(ReceiptDto.class);
    }

    private JsonAdapter<Document<List<ReceiptDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(ReceiptDto.class);
    }
}






