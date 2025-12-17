package sd_009.bookstore.dto.jsonApiResource.campaign;

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
    private final String name;
    private final CampaignType campaignType;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    @ToMany(name = "campaignDetails")
    private final List<CampaignDetailDto> campaignDetails;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    private Double minTotal;
    private Double percentage;
    private Double maxDiscount;
}

