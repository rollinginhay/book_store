package sd_009.bookstore.dto.jsonApiResource.campaign;

import jakarta.validation.constraints.*;
import jsonapi.Id;
import jsonapi.Resource;
import jsonapi.ToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.entity.campaign.CampaignType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link .entity.Campaign}
 */
@Getter
@AllArgsConstructor
@Resource(type = "campaign")
public class CampaignDto implements Serializable {
    @Id
    private final String id;
    @NotBlank(message = "Phải có tên giảm giá")
    private final String name;
    @NotNull(message = "Phải có loại gim giá")
    private final CampaignType campaignType;
    @NotNull(message = "Ngày bắt đầu là bắt buộc")
    private final LocalDateTime startDate;
    @NotNull(message = "Ngày kết thúc là bắt buộc")
    private final LocalDateTime endDate;
    @ToMany(name = "campaignDetails")
    private final List<CampaignDetailDto> campaignDetails;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private final String note;
    @DecimalMin(value = "0.0", message = "Giá tối thiểu không được âm")
    private Double minTotal;
    @DecimalMin(value = "0.0", message = "Phần trăm không được âm")
    @DecimalMax(value = "100.0", message = "Phần trăm không được vượt quá 100")
    private Double percentage;
    @DecimalMin(value = "0.0", message = "Giảm giá tối đa không được âm")
    private Double maxDiscount;
}

