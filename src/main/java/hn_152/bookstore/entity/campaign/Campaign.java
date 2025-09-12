package hn_152.bookstore.entity.campaign;

import hn_152.bookstore.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "campaign")
    private List<CampaignDetail> campaignDetails;

}
