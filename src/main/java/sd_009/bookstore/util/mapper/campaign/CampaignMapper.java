package sd_009.bookstore.util.mapper.campaign;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.campaign.CampaignDto;
import sd_009.bookstore.entity.campaign.Campaign;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CampaignMapper {

    Campaign toEntity(CampaignDto dto);

    CampaignDto toDto(Campaign entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Campaign partialUpdate(CampaignDto dto, @MappingTarget Campaign entity);
}

