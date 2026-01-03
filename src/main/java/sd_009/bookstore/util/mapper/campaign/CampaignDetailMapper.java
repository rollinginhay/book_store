package sd_009.bookstore.util.mapper.campaign;

import org.mapstruct.*;
import sd_009.bookstore.dto.jsonApiResource.campaign.CampaignDetailDto;
import sd_009.bookstore.entity.campaign.CampaignDetail;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CampaignDetailMapper {

    CampaignDetail toEntity(CampaignDetailDto dto);

    @Mapping(
            target = "bookDetailId",
            expression = "java(entity.getBookDetail() != null ? String.valueOf(entity.getBookDetail().getId()) : null)"
    )
    CampaignDetailDto toDto(CampaignDetail entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CampaignDetail partialUpdate(CampaignDetailDto dto, @MappingTarget CampaignDetail entity);
}

