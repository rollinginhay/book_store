package hn_152.bookstore.model.entity.campaign;

import hn_152.bookstore.model.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
//@Entity

public class Campaign extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String campaignType;

    private LocalDate startDate;

    private LocalDate endDate;
}
