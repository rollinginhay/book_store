package sd_009.bookstore.service.voucher;

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
import sd_009.bookstore.dto.jsonApiResource.voucher.VoucherDto;
import sd_009.bookstore.entity.voucher.Voucher;
import sd_009.bookstore.repository.VoucherRepository;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.voucher.VoucherMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;
import sd_009.bookstore.entity.voucher.VoucherType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final JsonApiValidator validator;
    private final JsonApiAdapterProvider adapterProvider;

    // 🔹 Lấy tất cả voucher
    @Transactional(readOnly = true)
    public String findAll() {
        List<Voucher> list = voucherRepository.findAllByEnabled(true, Sort.by("updatedAt").descending());
        List<VoucherDto> dtos = list.stream().map(voucherMapper::toDto).toList();

        Document<List<VoucherDto>> doc = Document.with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_VOUCHERS))
                        .build().toMap()))
                .build();

        return getListAdapter().toJson(doc);
    }

    // 🔹 Lấy voucher theo ID
    @Transactional(readOnly = true)
    public String findById(Long id) {
        Voucher found = voucherRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Voucher not found"));
        VoucherDto dto = voucherMapper.toDto(found);

        Document<VoucherDto> doc = Document.with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_VOUCHER_BY_ID, id))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // 🔹 Tạo mới voucher
    @Transactional
    public String save(String json) {
        VoucherDto dto = validator.readAndValidate(json, VoucherDto.class);
        
        // Validate tên
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new BadRequestException("Voucher name is required");
        }
        if (voucherRepository.findByName(dto.getName()).isPresent()) {
            throw new DuplicateElementException("Voucher name already exists");
        }

        // Validate thời gian
        validateVoucherDates(dto.getStartDate(), dto.getEndDate(), null, true);

        // Validate loại voucher và giá trị giảm giá
        validateVoucherTypeAndDiscount(dto.getVoucherType(), dto.getPercentage(), dto.getMaxDiscount());

        // Validate minTotal
        if (dto.getMinTotal() != null && dto.getMinTotal() < 0) {
            throw new BadRequestException("Minimum total must be greater than or equal to 0");
        }

        Voucher entity = voucherMapper.toEntity(dto);
        
        // Tự động tạo code nếu chưa có
        if (entity.getCode() == null || entity.getCode().trim().isEmpty()) {
            entity.setCode(generateVoucherCode());
        } else {
            // Validate và kiểm tra code trùng
            String code = entity.getCode().trim().toUpperCase();
            if (code.length() < 3 || code.length() > 20) {
                throw new BadRequestException("Voucher code must be between 3 and 20 characters");
            }
            if (voucherRepository.findByCode(code).isPresent()) {
                throw new DuplicateElementException("Voucher code already exists");
            }
            entity.setCode(code);
        }
        
        // Set default values
        if (entity.getEnabled() == null) {
            entity.setEnabled(true);
        }
        if (entity.getUsed() == null) {
            entity.setUsed(false);
        }
        if (entity.getMinTotal() == null) {
            entity.setMinTotal(0.0);
        }
        
        Voucher saved = voucherRepository.save(entity);

        Document<VoucherDto> doc = Document.with(voucherMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_VOUCHER_BY_ID, saved.getId()))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // 🔹 Cập nhật voucher
    @Transactional
    public String update(String json) {
        VoucherDto dto = validator.readAndValidate(json, VoucherDto.class);
        if (dto.getId() == null)
            throw new BadRequestException("No identifier found");

        Voucher existing = voucherRepository.findById(Long.valueOf(dto.getId()))
                .orElseThrow(() -> new BadRequestException("Voucher not found"));

        // Validate: Không cho phép sửa voucher đã được sử dụng
        if (Boolean.TRUE.equals(existing.getUsed())) {
            throw new BadRequestException("Cannot update voucher that has been used");
        }

        // Validate tên nếu có thay đổi
        if (dto.getName() != null && !dto.getName().equals(existing.getName())) {
            if (dto.getName().trim().isEmpty()) {
                throw new BadRequestException("Voucher name cannot be empty");
            }
            if (voucherRepository.findByName(dto.getName()).isPresent()) {
                throw new DuplicateElementException("Voucher name already exists");
            }
        }

        // Validate code nếu có thay đổi
        if (dto.getCode() != null && !dto.getCode().equals(existing.getCode())) {
            String code = dto.getCode().trim().toUpperCase();
            if (code.length() < 3 || code.length() > 20) {
                throw new BadRequestException("Voucher code must be between 3 and 20 characters");
            }
            if (voucherRepository.findByCode(code).isPresent()) {
                throw new DuplicateElementException("Voucher code already exists");
            }
        }

        // Validate thời gian
        LocalDateTime newStartDate = dto.getStartDate() != null ? dto.getStartDate() : existing.getStartDate();
        LocalDateTime newEndDate = dto.getEndDate() != null ? dto.getEndDate() : existing.getEndDate();
        validateVoucherDates(newStartDate, newEndDate, existing.getId(), false);

        // Validate loại voucher và giá trị giảm giá nếu có thay đổi
        VoucherType newVoucherType = dto.getVoucherType() != null ? dto.getVoucherType() : existing.getVoucherType();
        Double newPercentage = dto.getPercentage() != null ? dto.getPercentage() : existing.getPercentage();
        Double newMaxDiscount = dto.getMaxDiscount() != null ? dto.getMaxDiscount() : existing.getMaxDiscount();
        validateVoucherTypeAndDiscount(newVoucherType, newPercentage, newMaxDiscount);

        // Validate minTotal
        if (dto.getMinTotal() != null && dto.getMinTotal() < 0) {
            throw new BadRequestException("Minimum total must be greater than or equal to 0");
        }
        
        // Validate: Không cho phép disable voucher đang active (nếu đang trong thời gian hiệu lực)
        if (dto.getEnabled() != null && !dto.getEnabled() && existing.getEnabled()) {
            LocalDateTime now = LocalDateTime.now();
            if (existing.getStartDate() != null && existing.getEndDate() != null) {
                if (now.isAfter(existing.getStartDate()) && now.isBefore(existing.getEndDate())) {
                    throw new BadRequestException("Cannot disable voucher that is currently active");
                }
            }
        }

        Voucher updated = voucherRepository.save(voucherMapper.partialUpdate(dto, existing));

        Document<VoucherDto> doc = Document.with(voucherMapper.toDto(updated))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_VOUCHER_BY_ID, updated.getId()))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // 🔹 Xóa mềm voucher
    @Transactional
    public void delete(Long id) {
        voucherRepository.findById(id).ifPresent(e -> {
            e.setEnabled(false);
            voucherRepository.save(e);
        });
    }

    // Tạo mã voucher tự động
    private String generateVoucherCode() {
        String code;
        do {
            code = "VCH" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (voucherRepository.findByCode(code).isPresent());
        return code;
    }

    /**
     * Validate thời gian của voucher
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @param voucherId ID của voucher (null nếu đang tạo mới)
     * @param isCreate true nếu đang tạo mới, false nếu đang cập nhật
     */
    private void validateVoucherDates(LocalDateTime startDate, LocalDateTime endDate, Long voucherId, boolean isCreate) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        if (startDate == null) {
            throw new BadRequestException("Start date is required");
        }

        if (endDate == null) {
            throw new BadRequestException("End date is required");
        }

        // Validate: endDate phải sau startDate
        if (!endDate.isAfter(startDate)) {
            throw new BadRequestException("End date must be after start date");
        }

        // Validate: Khi tạo mới, startDate không được trong quá khứ (chỉ so sánh ngày, không so sánh giờ)
        // Cho phép ngày hôm nay, chỉ không cho phép ngày trong quá khứ
        if (isCreate && startDate.toLocalDate().isBefore(today)) {
            throw new BadRequestException("Start date cannot be in the past when creating new voucher");
        }

        // Validate: Khi cập nhật, không cho phép thay đổi startDate nếu đã bắt đầu
        if (!isCreate && voucherId != null) {
            Voucher existing = voucherRepository.findById(voucherId)
                    .orElseThrow(() -> new BadRequestException("Voucher not found"));
            
            if (existing.getStartDate() != null && now.isAfter(existing.getStartDate())) {
                // Nếu đã bắt đầu, không cho phép thay đổi startDate
                if (startDate.isBefore(existing.getStartDate()) || startDate.isAfter(existing.getStartDate())) {
                    throw new BadRequestException("Cannot change start date after voucher has started");
                }
            }
        }
    }

    /**
     * Validate loại voucher và giá trị giảm giá
     * @param voucherType Loại voucher
     * @param percentage Phần trăm giảm giá (cho PERCENTAGE_DISCOUNT)
     * @param maxDiscount Số tiền giảm tối đa (cho FLAT_DISCOUNT)
     */
    private void validateVoucherTypeAndDiscount(VoucherType voucherType, Double percentage, Double maxDiscount) {
        if (voucherType == null) {
            throw new BadRequestException("Voucher type is required");
        }

        switch (voucherType) {
            case PERCENTAGE_DISCOUNT:
            case PERCENTAGE_RECEIPT:
            case PERCENTAGE_PRODUCT:
                if (percentage == null || percentage <= 0 || percentage > 100) {
                    throw new BadRequestException("Percentage must be between 1 and 100 for percentage discount type");
                }
                if (maxDiscount != null && maxDiscount < 0) {
                    throw new BadRequestException("Max discount cannot be negative");
                }
                break;
            case FLAT_DISCOUNT:
                if (maxDiscount == null || maxDiscount <= 0) {
                    throw new BadRequestException("Max discount is required and must be greater than 0 for flat discount type");
                }
                break;
        }
    }

    private JsonAdapter<Document<VoucherDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(VoucherDto.class);
    }

    private JsonAdapter<Document<List<VoucherDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(VoucherDto.class);
    }
}

