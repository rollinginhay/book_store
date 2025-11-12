package sd_009.bookstore.dto.jsonApiResource.campaign;

import lombok.*;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.campaign.Campaign;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link .entity.CampaignDetail}
 */
@Getter
@AllArgsConstructor
public class CampaignDetailDto implements Serializable {
    private final String id;
    private final Campaign campaign;
    private final BookDetail bookDetail;
    private final Double value;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Boolean enabled;
    private final String note;
}

