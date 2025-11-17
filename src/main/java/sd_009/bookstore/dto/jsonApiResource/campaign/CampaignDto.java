package sd_009.bookstore.dto.jsonApiResource.campaign;

import lombok.*;
import sd_009.bookstore.entity.campaign.CampaignDetail;
import sd_009.bookstore.entity.campaign.CampaignType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link .entity.Campaign}
 */
@Getter
@AllArgsConstructor
public class CampaignDto implements Serializable {
    private final String id;
    private final String name;
    private final CampaignType campaignType;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<CampaignDetail> campaignDetails;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
}

