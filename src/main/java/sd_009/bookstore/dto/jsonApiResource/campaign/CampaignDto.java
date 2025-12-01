package sd_009.bookstore.dto.jsonApiResource.campaign;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sd_009.bookstore.entity.campaign.CampaignType;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    //    private final List<CampaignDetail> campaignDetails;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
    private Double minTotal;
    private Double percentage;
    private Double maxDiscount;
}

