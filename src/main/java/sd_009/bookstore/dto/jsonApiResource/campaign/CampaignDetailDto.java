package sd_009.bookstore.dto.jsonApiResource.campaign;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link .entity.CampaignDetail}
 */
@Getter
@AllArgsConstructor
@Resource(type = "campaignDetail")
public class CampaignDetailDto implements Serializable {
    @Id
    private final String id;
//    @ToOne(name = "bookDetail")
//    private final BookDetail bookDetail;
    private final Double value;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
}

