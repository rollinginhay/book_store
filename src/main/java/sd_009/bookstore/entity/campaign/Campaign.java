package sd_009.bookstore.entity.campaign;

import jakarta.persistence.*;
import lombok.*;
import sd_009.bookstore.entity.AuditableEntity;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity

public class Campaign extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CampaignType campaignType;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.PERSIST)
    private List<CampaignDetail> campaignDetails;

}
