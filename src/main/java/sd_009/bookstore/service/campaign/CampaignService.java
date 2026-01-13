package sd_009.bookstore.service.campaign;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.exceptionHanding.exception.DuplicateElementException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.campaign.CampaignDto;
import sd_009.bookstore.entity.campaign.Campaign;
import sd_009.bookstore.entity.campaign.CampaignDetail;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.repository.CampaignRepository;
import sd_009.bookstore.repository.CampaignDetailRepository;
import sd_009.bookstore.repository.BookDetailRepository;
import sd_009.bookstore.util.mapper.campaign.CampaignMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignDetailRepository campaignDetailRepository;
    private final BookDetailRepository bookDetailRepository;
    private final CampaignMapper campaignMapper;
    private final JsonApiValidator validator;
    private final JsonApiAdapterProvider adapterProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 🔹 Lấy tất cả campaign
    @Transactional(readOnly = true)
    public String findAll() {
        // Chỉ filter theo enabled, không filter theo endDate để hiển thị tất cả
        List<Campaign> list = campaignRepository.findAllByEnabled(true, Sort.by("updatedAt").descending());
        List<CampaignDto> dtos = list.stream().map(campaignMapper::toDto).toList();

        Document<List<CampaignDto>> doc = Document.with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CAMPAIGNS))
                        .build().toMap()))
                .build();

        return getListAdapter().toJson(doc);
    }

    @Transactional
    public String findActive() {
        List<Campaign> list = campaignRepository.findAllByEnabled(true, Sort.by("updatedAt").descending());

        LocalDateTime now = LocalDateTime.now();
        System.out.println("🔍 [CampaignService.findActive] Current time: " + now);
        System.out.println("🔍 [CampaignService.findActive] Total enabled campaigns: " + list.size());
        
        List<Campaign> filterList = list.stream()
                .filter(e -> {
                    // Campaign phải có endDate và endDate > now
                    // Hoặc nếu là PERCENTAGE_PRODUCT (combo) thì không cần endDate
                    if (e.getCampaignType() != null && 
                        e.getCampaignType().name().equals("PERCENTAGE_PRODUCT")) {
                        System.out.println("✅ [CampaignService.findActive] PERCENTAGE_PRODUCT campaign (no endDate check): " + e.getName());
                        return true; // Combo không có endDate
                    }
                    if (e.getEndDate() == null) {
                        System.out.println("⚠️ [CampaignService.findActive] Campaign has no endDate: " + e.getName());
                        return false;
                    }
                    boolean isActive = e.getEndDate().isAfter(now) || e.getEndDate().isEqual(now);
                    System.out.println("🔍 [CampaignService.findActive] Campaign: " + e.getName() + 
                        " | endDate: " + e.getEndDate() + 
                        " | now: " + now +
                        " | isActive (endDate >= now): " + isActive);
                    return isActive;
                })
                .filter(e -> {
                    // Campaign phải có startDate và startDate <= now (đã bắt đầu)
                    // Hoặc nếu là PERCENTAGE_PRODUCT thì không cần startDate
                    if (e.getCampaignType() != null && 
                        e.getCampaignType().name().equals("PERCENTAGE_PRODUCT")) {
                        return true; // Combo không có startDate
                    }
                    if (e.getStartDate() == null) {
                        System.out.println("⚠️ [CampaignService.findActive] Campaign has no startDate: " + e.getName());
                        return false;
                    }
                    boolean hasStarted = !e.getStartDate().isAfter(now);
                    System.out.println("🔍 [CampaignService.findActive] Campaign: " + e.getName() + 
                        " | startDate: " + e.getStartDate() + 
                        " | now: " + now +
                        " | hasStarted (startDate <= now): " + hasStarted);
                    return hasStarted;
                })
                .toList();
        
        System.out.println("✅ [CampaignService.findActive] Active campaigns count: " + filterList.size());
        filterList.forEach(c -> System.out.println("  - " + c.getName() + " (type: " + c.getCampaignType() + ", percentage: " + c.getPercentage() + "%)"));
        
        List<CampaignDto> dtos = filterList.stream().map(campaignMapper::toDto).toList();

        Document<List<CampaignDto>> doc = Document.with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CAMPAIGNS))
                        .build().toMap()))
                .build();

        return getListAdapter().toJson(doc);
    }

    // 🔹 Lấy campaign theo ID
    @Transactional
    public String findById(Long id) {
        Campaign found = campaignRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Campaign not found"));
        CampaignDto dto = campaignMapper.toDto(found);
        
        // ✅ Filter campaignDetails enabled = true sau khi map (vì CampaignDto có final fields nên không thể modify)
        // Filter sẽ được thực hiện ở CampaignDetailService.findByCampaignId() khi FE fetch relationships

        Document<CampaignDto> doc = Document.with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CAMPAIGN_BY_ID, id))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // 🔹 Tạo mới campaign
    @Transactional
    public String save(String json) {
        // ✅ Parse JSON để lấy relationships.campaignDetails TRƯỚC khi validate
        // (vì validator sẽ báo lỗi nếu relationships không có id/lid)
        JsonNode campaignDetailsData = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            JsonNode relationships = jsonNode.path("data").path("relationships");
            JsonNode campaignDetailsRel = relationships.path("campaignDetails");
            campaignDetailsData = campaignDetailsRel.path("data");
            
            // ✅ Loại bỏ relationships.campaignDetails khỏi JSON trước khi validate
            if (campaignDetailsData.isArray() && campaignDetailsData.size() > 0) {
                ((ObjectNode) jsonNode.path("data").path("relationships")).remove("campaignDetails");
                json = objectMapper.writeValueAsString(jsonNode);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not parse relationships before validation: " + e.getMessage());
        }
        
        CampaignDto dto = validator.readAndValidate(json, CampaignDto.class);
        if (campaignRepository.findByName(dto.getName()).isPresent()) {
            throw new DuplicateElementException("Campaign name already exists");
        }

        // ✅ Validate: Không cho phép tạo đợt sale trùng khoảng thời gian
        validateNoOverlappingSaleCampaigns(dto, null, campaignDetailsData);

        Campaign entity = campaignMapper.toEntity(dto);
        
        // ✅ Set endDate time = 23:59:59 để campaign hết hạn vào cuối ngày
        if (entity.getEndDate() != null) {
            entity.setEndDate(entity.getEndDate().withHour(23).withMinute(59).withSecond(59).withNano(999000000));
        }
        
        Campaign saved = campaignRepository.save(entity);

        // ✅ Lưu campaignDetails từ relationships đã parse ở trên
        if (campaignDetailsData != null && campaignDetailsData.isArray() && campaignDetailsData.size() > 0) {
            try {
                List<CampaignDetail> campaignDetails = new ArrayList<>();
                for (JsonNode detailNode : campaignDetailsData) {
                    JsonNode attributes = detailNode.path("attributes");
                    String bookDetailId = attributes.path("bookDetailId").asText();
                    Double value = attributes.path("value").isNull() ? null : attributes.path("value").asDouble();
                    
                    if (bookDetailId != null && !bookDetailId.isEmpty()) {
                        BookDetail bookDetail = bookDetailRepository.findById(Long.valueOf(bookDetailId))
                                .orElseThrow(() -> new BadRequestException("BookDetail not found: " + bookDetailId));
                        
                        CampaignDetail campaignDetail = CampaignDetail.builder()
                                .campaign(saved)
                                .bookDetail(bookDetail)
                                .value(value != null ? value : dto.getPercentage())
                                .build();
                        campaignDetail.setEnabled(true);
                        campaignDetails.add(campaignDetail);
                    }
                }
                if (!campaignDetails.isEmpty()) {
                    campaignDetailRepository.saveAll(campaignDetails);
                }
            } catch (Exception e) {
                // Không throw exception để không làm hỏng luồng khác, chỉ log
                System.err.println("Warning: Could not save campaignDetails relationships: " + e.getMessage());
            }
        }

        Document<CampaignDto> doc = Document.with(campaignMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CAMPAIGN_BY_ID, saved.getId()))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // 🔹 Cập nhật campaign
    @Transactional
    public String update(String json) {
        // ✅ Parse JSON để lấy relationships.campaignDetails TRƯỚC khi validate
        // (vì validator sẽ báo lỗi nếu relationships không có id/lid)
        JsonNode campaignDetailsData = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            JsonNode relationships = jsonNode.path("data").path("relationships");
            JsonNode campaignDetailsRel = relationships.path("campaignDetails");
            campaignDetailsData = campaignDetailsRel.path("data");
            
            // ✅ Loại bỏ relationships.campaignDetails khỏi JSON trước khi validate
            if (campaignDetailsData.isArray() && campaignDetailsData.size() > 0) {
                ((ObjectNode) jsonNode.path("data").path("relationships")).remove("campaignDetails");
                json = objectMapper.writeValueAsString(jsonNode);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not parse relationships before validation: " + e.getMessage());
        }
        
        CampaignDto dto = validator.readAndValidate(json, CampaignDto.class);
        if (dto.getId() == null)
            throw new BadRequestException("No identifier found");

        Campaign existing = campaignRepository.findById(Long.valueOf(dto.getId()))
                .orElseThrow(() -> new BadRequestException("Campaign not found"));

        // ✅ Validate: Không cho phép cập nhật đợt sale trùng khoảng thời gian
        validateNoOverlappingSaleCampaigns(dto, Long.valueOf(dto.getId()), campaignDetailsData);

        Campaign updated = campaignMapper.partialUpdate(dto, existing);
        
        // ✅ Set endDate time = 23:59:59 để campaign hết hạn vào cuối ngày (nếu có endDate trong dto)
        if (dto.getEndDate() != null && updated.getEndDate() != null) {
            updated.setEndDate(updated.getEndDate().withHour(23).withMinute(59).withSecond(59).withNano(999000000));
        }
        
        Campaign saved = campaignRepository.save(updated);

        // ✅ Cập nhật campaignDetails từ relationships đã parse ở trên
        // ⚠️ QUAN TRỌNG: Chỉ xử lý campaignDetails cho combo (PERCENTAGE_PRODUCT)
        if (dto.getCampaignType() != null && dto.getCampaignType().name().equals("PERCENTAGE_PRODUCT")) {
            try {
                System.out.println("🔍 [CampaignService.update] Processing campaignDetails for combo campaign " + updated.getId());
                System.out.println("🔍 [CampaignService.update] campaignDetailsData: " + (campaignDetailsData != null ? campaignDetailsData.toString() : "null"));
                
                // ✅ BƯỚC 1: Soft-delete TẤT CẢ CampaignDetail cũ của campaign này (LUÔN LUÔN chạy khi update combo)
                List<CampaignDetail> oldDetails = campaignDetailRepository.findAll().stream()
                        .filter(cd -> cd.getCampaign() != null && 
                                cd.getCampaign().getId() != null &&
                                cd.getCampaign().getId().equals(saved.getId()) && 
                                cd.getEnabled() != null && 
                                cd.getEnabled())
                        .toList();
                
                System.out.println("🔍 [CampaignService.update] Found " + oldDetails.size() + " old CampaignDetails to soft-delete for campaign " + saved.getId());
                
                // ✅ Soft-delete tất cả cũ TRƯỚC
                for (CampaignDetail oldDetail : oldDetails) {
                    oldDetail.setEnabled(false);
                    campaignDetailRepository.save(oldDetail);
                    System.out.println("✅ [CampaignService.update] Soft-deleted CampaignDetail ID: " + oldDetail.getId());
                }
                
                // ✅ BƯỚC 2: Tạo mới CampaignDetail từ relationships (chỉ khi có dữ liệu)
                if (campaignDetailsData != null && campaignDetailsData.isArray() && campaignDetailsData.size() > 0) {
                    List<CampaignDetail> campaignDetails = new ArrayList<>();
                    for (JsonNode detailNode : campaignDetailsData) {
                        System.out.println("🔍 [CampaignService.update] Processing detailNode: " + detailNode.toString());
                        JsonNode attributes = detailNode.path("attributes");
                        String bookDetailId = attributes.path("bookDetailId").asText();
                        Double value = attributes.path("value").isNull() ? null : attributes.path("value").asDouble();
                        
                        System.out.println("🔍 [CampaignService.update] Extracted bookDetailId: " + bookDetailId + ", value: " + value);
                        
                        if (bookDetailId != null && !bookDetailId.isEmpty() && !bookDetailId.equals("null")) {
                            try {
                                BookDetail bookDetail = bookDetailRepository.findById(Long.valueOf(bookDetailId))
                                        .orElseThrow(() -> new BadRequestException("BookDetail not found: " + bookDetailId));
                                
                                CampaignDetail campaignDetail = CampaignDetail.builder()
                                        .campaign(updated)
                                        .bookDetail(bookDetail)
                                        .value(value != null ? value : dto.getPercentage())
                                        .build();
                                campaignDetail.setEnabled(true);
                                campaignDetails.add(campaignDetail);
                                System.out.println("✅ [CampaignService.update] Created new CampaignDetail for bookDetailId: " + bookDetailId);
                            } catch (NumberFormatException e) {
                                System.err.println("⚠️ [CampaignService.update] Invalid bookDetailId: " + bookDetailId);
                            }
                        } else {
                            System.err.println("⚠️ [CampaignService.update] Empty or null bookDetailId in detailNode: " + detailNode.toString());
                        }
                    }
                    if (!campaignDetails.isEmpty()) {
                        campaignDetailRepository.saveAll(campaignDetails);
                        System.out.println("✅ [CampaignService.update] Created " + campaignDetails.size() + " new CampaignDetails for campaign " + updated.getId());
                    } else {
                        System.out.println("⚠️ [CampaignService.update] No valid CampaignDetails to create after parsing");
                    }
                } else {
                    System.out.println("⚠️ [CampaignService.update] No campaignDetailsData found in relationships for campaign " + updated.getId() + " - old details were soft-deleted");
                }
            } catch (Exception e) {
                // Không throw exception để không làm hỏng luồng khác, chỉ log
                System.err.println("❌ [CampaignService.update] Error updating campaignDetails relationships: " + e.getMessage());
                e.printStackTrace();
            }
        }

        Document<CampaignDto> doc = Document.with(campaignMapper.toDto(updated))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CAMPAIGN_BY_ID, updated.getId()))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // 🔹 Xóa mềm campaign
    @Transactional
    public void delete(Long id) {
        campaignRepository.findById(id).ifPresent(e -> {
            e.setEnabled(false);
            campaignRepository.save(e);
        });
    }

    /**
     * ✅ Validate: Không cho phép tạo/cập nhật đợt sale trùng khoảng thời gian
     * Chỉ validate cho PERCENTAGE_DISCOUNT và FLAT_DISCOUNT (đợt sale có ngày)
     * Combo (PERCENTAGE_PRODUCT) không cần validate vì không có ngày
     */
    private void validateNoOverlappingSaleCampaigns(CampaignDto dto, Long excludeId, JsonNode campaignDetailsData) {
        // Chỉ validate cho đợt sale có ngày (PERCENTAGE_DISCOUNT, FLAT_DISCOUNT)
        if (dto.getMinTotal() != null && dto.getMinTotal() < 0) {
            throw new BadRequestException("Giá trị đơn hàng tối thiểu không được âm");
        }

        // 2️⃣ Validate percentage
        if (dto.getPercentage() != null) {
            if (dto.getPercentage() <= 0 || dto.getPercentage() > 100) {
                throw new BadRequestException("Phần trăm giảm phải nằm trong khoảng 1–100%");
            }
        }

        // 3️⃣ Validate maxDiscount
        if (dto.getMaxDiscount() != null && dto.getMaxDiscount() < 0) {
            throw new BadRequestException("Giá trị giảm tối đa không được âm");
        }

        // ✅ Xử lý riêng cho PERCENTAGE_PRODUCT: validate sản phẩm trùng
        if (dto.getCampaignType() != null && dto.getCampaignType().name().equals("PERCENTAGE_PRODUCT")) {
            // Lấy danh sách bookDetailId từ campaignDetailsData
            List<Long> newBookDetailIds = new ArrayList<>();
            if (campaignDetailsData != null && campaignDetailsData.isArray()) {
                for (JsonNode detailNode : campaignDetailsData) {
                    JsonNode attributes = detailNode.path("attributes");
                    String bookDetailIdStr = attributes.path("bookDetailId").asText();
                    if (bookDetailIdStr != null && !bookDetailIdStr.isEmpty() && !bookDetailIdStr.equals("null")) {
                        try {
                            newBookDetailIds.add(Long.valueOf(bookDetailIdStr));
                        } catch (NumberFormatException e) {
                            // Bỏ qua nếu không parse được
                        }
                    }
                }
            }

            // ✅ BƯỚC 1: Lấy tất cả các campaign PERCENTAGE_PRODUCT đang hoạt động và trùng thời gian
            List<Campaign> campaignsWithOverlappingTime =
                    campaignRepository.findAllByEnabled(true, Sort.by("updatedAt").descending())
                            .stream()
                            .filter(c -> excludeId == null || !c.getId().equals(excludeId))
                            .filter(c -> c.getCampaignType() != null && 
                                    c.getCampaignType().name().equals("PERCENTAGE_PRODUCT"))
                            .filter(c ->
                                    c.getStartDate() != null &&
                                            c.getEndDate() != null &&
                                            dto.getStartDate() != null &&
                                            dto.getEndDate() != null &&
                                            isDateRangeOverlapping(
                                                    dto.getStartDate(), dto.getEndDate(),
                                                    c.getStartDate(), c.getEndDate()
                                            )
                            )
                            .toList();

            // ✅ BƯỚC 2: Kiểm tra sản phẩm trùng (chỉ trong các campaign trùng thời gian)
            // Tách thông báo lỗi: lỗi sản phẩm trùng → thông báo về sản phẩm
            for (Campaign existing : campaignsWithOverlappingTime) {
                // Lấy danh sách bookDetailId từ campaign hiện có (chỉ lấy enabled = true)
                List<CampaignDetail> existingDetails = campaignDetailRepository.findAll()
                        .stream()
                        .filter(cd -> cd.getCampaign() != null && 
                                cd.getCampaign().getId() != null &&
                                cd.getCampaign().getId().equals(existing.getId()) &&
                                cd.getEnabled() != null && 
                                cd.getEnabled() &&
                                cd.getBookDetail() != null)
                        .toList();
                
                List<Long> existingBookDetailIds = existingDetails.stream()
                        .map(cd -> cd.getBookDetail().getId())
                        .toList();

                // ✅ Kiểm tra xem có sản phẩm nào trùng không
                // Thông báo lỗi chỉ về sản phẩm trùng (không mention thời gian vì đó chỉ là điều kiện check)
                for (Long newBookDetailId : newBookDetailIds) {
                    if (existingBookDetailIds.contains(newBookDetailId)) {
                        // Tìm tên sản phẩm để thông báo rõ ràng hơn
                        String productName = "sản phẩm này";
                        try {
                            BookDetail bookDetail = bookDetailRepository.findById(newBookDetailId).orElse(null);
                            if (bookDetail != null && bookDetail.getBook() != null) {
                                productName = bookDetail.getBook().getTitle() + 
                                        (bookDetail.getBookFormat() != null ? " - " + bookDetail.getBookFormat() : "");
                            }
                        } catch (Exception e) {
                            // Nếu không lấy được tên, dùng mặc định
                        }
                        
                        throw new BadRequestException(
                                "Sản phẩm '" + productName + "' đã được áp dụng giảm giá trong campaign '" + 
                                existing.getName() + "'. Vui lòng chọn sản phẩm khác."
                        );
                    }
                }
            }
            return; // Kết thúc validation cho PERCENTAGE_PRODUCT
        }

        // ✅ Logic validation cho PERCENTAGE_DISCOUNT và FLAT_DISCOUNT (giữ nguyên)
        // Lấy tất cả các đợt sale đang hoạt động cùng loại
        LocalDateTime now = LocalDateTime.now();
        List<Campaign> existingCampaigns =
                campaignRepository.findAllByEnabled(true, Sort.by("updatedAt").descending())
                        .stream()
                        .filter(c -> excludeId == null || !c.getId().equals(excludeId))
                        .filter(c -> c.getCampaignType() == dto.getCampaignType())
                        // ✅ CHỈ kiểm tra campaigns đang ACTIVE (endDate > now) - không kiểm tra campaigns đã hết hạn
                        .filter(c -> {
                            if (c.getEndDate() == null) {
                                return false; // Campaign không có endDate thì không kiểm tra
                            }
                            boolean isActive = c.getEndDate().isAfter(now) || c.getEndDate().isEqual(now);
                            return isActive;
                        })
                        .filter(c ->
                                c.getStartDate() != null &&
                                        c.getEndDate() != null &&
                                        dto.getStartDate() != null &&
                                        dto.getEndDate() != null &&
                                        isDateRangeOverlapping(
                                                dto.getStartDate(), dto.getEndDate(),
                                                c.getStartDate(), c.getEndDate()
                                        )
                        )
                        .toList();

        for (Campaign existing : existingCampaigns) {
            boolean sameMinTotal =
                    (existing.getMinTotal() == null && dto.getMinTotal() == null) ||
                            (existing.getMinTotal() != null && existing.getMinTotal().equals(dto.getMinTotal()));

            boolean samePercentage =
                    (existing.getPercentage() == null && dto.getPercentage() == null) ||
                            (existing.getPercentage() != null && existing.getPercentage().equals(dto.getPercentage()));

            boolean sameMaxDiscount =
                    (existing.getMaxDiscount() == null && dto.getMaxDiscount() == null) ||
                            (existing.getMaxDiscount() != null && existing.getMaxDiscount().equals(dto.getMaxDiscount()));

            if (sameMinTotal && samePercentage && sameMaxDiscount) {
                throw new BadRequestException(
                        "Đã tồn tại campaign cùng thời gian và cùng điều kiện giảm giá. " +
                                "Vui lòng thay đổi minTotal, phần trăm hoặc mức giảm tối đa."
                );
            }
        }

//         Kiểm tra overlap với từng campaign
//        for (Campaign existing : existingCampaigns) {
//            if (isDateRangeOverlapping(
//                    dto.getStartDate(), dto.getEndDate(),
//                    existing.getStartDate(), existing.getEndDate())) {
//                throw new BadRequestException(
//                        String.format("Đợt sale '%s' (từ %s đến %s) đang trùng khoảng thời gian với đợt sale '%s' (từ %s đến %s). Vui lòng chọn khoảng thời gian khác.",
//                                dto.getName(),
//                                dto.getStartDate().toLocalDate(),
//                                dto.getEndDate().toLocalDate(),
//                                existing.getName(),
//                                existing.getStartDate().toLocalDate(),
//                                existing.getEndDate().toLocalDate()));
//            }
//        }
    }

    /**
     * Kiểm tra 2 khoảng thời gian có overlap không
     * Overlap xảy ra khi: start1 <= end2 AND end1 >= start2
     */
    private boolean isDateRangeOverlapping(LocalDateTime start1, LocalDateTime end1,
                                           LocalDateTime start2, LocalDateTime end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }

    // 🔹 Tìm combo campaigns chứa bookDetailId (để hiển thị trên web)
    @Transactional(readOnly = true)
    public String findCombosByBookDetailId(Long bookDetailId) {
        // Tìm tất cả CampaignDetail có bookDetail này và enabled = true
        List<CampaignDetail> campaignDetails = campaignDetailRepository.findAll()
                .stream()
                .filter(cd -> cd.getEnabled() != null && cd.getEnabled() &&
                        cd.getBookDetail() != null &&
                        cd.getBookDetail().getId().equals(bookDetailId))
                .toList();

        // Lấy các campaign từ campaignDetails
        List<Campaign> comboCampaigns = campaignDetails.stream()
                .map(CampaignDetail::getCampaign)
                .filter(c -> c != null && c.getEnabled() != null && c.getEnabled())
                .filter(c -> c.getCampaignType() != null && 
                        c.getCampaignType().name().equals("PERCENTAGE_PRODUCT"))
                .filter(c -> {
                    // Chỉ lấy campaign đang active (không có endDate hoặc endDate > now)
                    if (c.getEndDate() == null) return true;
                    return c.getEndDate().isAfter(LocalDateTime.now());
                })
                .distinct()
                .toList();

        // Filter: Chỉ lấy combo mà TẤT CẢ sách trong combo đều có stock > 0
        List<Campaign> validCombos = comboCampaigns.stream()
                .filter(campaign -> {
                    List<CampaignDetail> details = campaign.getCampaignDetails().stream()
                            .filter(cd -> cd.getEnabled() != null && cd.getEnabled())
                            .toList();
                    
                    // Kiểm tra tất cả sách trong combo đều có stock > 0
                    return details.stream()
                            .allMatch(cd -> cd.getBookDetail() != null &&
                                    cd.getBookDetail().getStock() != null &&
                                    cd.getBookDetail().getStock() > 0);
                })
                .toList();

        List<CampaignDto> dtos = validCombos.stream().map(campaignMapper::toDto).toList();

        Document<List<CampaignDto>> doc = Document.with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_COMBO_BY_BOOK_DETAIL_ID))
                        .build().toMap()))
                .build();

        return getListAdapter().toJson(doc);
    }

    private JsonAdapter<Document<CampaignDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(CampaignDto.class);
    }

    private JsonAdapter<Document<List<CampaignDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(CampaignDto.class);
    }
}
