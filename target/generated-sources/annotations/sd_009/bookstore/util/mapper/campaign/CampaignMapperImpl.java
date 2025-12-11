package sd_009.bookstore.util.mapper.campaign;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.campaign.CampaignDto;
import sd_009.bookstore.entity.campaign.Campaign;
import sd_009.bookstore.entity.campaign.CampaignType;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class CampaignMapperImpl implements CampaignMapper {

    @Override
    public Campaign toEntity(CampaignDto dto) {
        if ( dto == null ) {
            return null;
        }

        Campaign.CampaignBuilder campaign = Campaign.builder();

        if ( dto.getId() != null ) {
            campaign.id( Long.parseLong( dto.getId() ) );
        }
        campaign.name( dto.getName() );
        campaign.campaignType( dto.getCampaignType() );
        campaign.startDate( dto.getStartDate() );
        campaign.endDate( dto.getEndDate() );
        campaign.minTotal( dto.getMinTotal() );
        campaign.percentage( dto.getPercentage() );
        campaign.maxDiscount( dto.getMaxDiscount() );

        return campaign.build();
    }

    @Override
    public CampaignDto toDto(Campaign entity) {
        if ( entity == null ) {
            return null;
        }

        String id = null;
        String name = null;
        CampaignType campaignType = null;
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;
        Double minTotal = null;
        Double percentage = null;
        Double maxDiscount = null;

        if ( entity.getId() != null ) {
            id = String.valueOf( entity.getId() );
        }
        name = entity.getName();
        campaignType = entity.getCampaignType();
        startDate = entity.getStartDate();
        endDate = entity.getEndDate();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
        enabled = entity.getEnabled();
        note = entity.getNote();
        minTotal = entity.getMinTotal();
        percentage = entity.getPercentage();
        maxDiscount = entity.getMaxDiscount();

        CampaignDto campaignDto = new CampaignDto( id, name, campaignType, startDate, endDate, createdAt, updatedAt, enabled, note, minTotal, percentage, maxDiscount );

        return campaignDto;
    }

    @Override
    public Campaign partialUpdate(CampaignDto dto, Campaign entity) {
        if ( dto == null ) {
            return entity;
        }

        if ( dto.getCreatedAt() != null ) {
            entity.setCreatedAt( dto.getCreatedAt() );
        }
        if ( dto.getUpdatedAt() != null ) {
            entity.setUpdatedAt( dto.getUpdatedAt() );
        }
        if ( dto.getEnabled() != null ) {
            entity.setEnabled( dto.getEnabled() );
        }
        if ( dto.getNote() != null ) {
            entity.setNote( dto.getNote() );
        }
        if ( dto.getId() != null ) {
            entity.setId( Long.parseLong( dto.getId() ) );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getCampaignType() != null ) {
            entity.setCampaignType( dto.getCampaignType() );
        }
        if ( dto.getStartDate() != null ) {
            entity.setStartDate( dto.getStartDate() );
        }
        if ( dto.getEndDate() != null ) {
            entity.setEndDate( dto.getEndDate() );
        }
        if ( dto.getMinTotal() != null ) {
            entity.setMinTotal( dto.getMinTotal() );
        }
        if ( dto.getPercentage() != null ) {
            entity.setPercentage( dto.getPercentage() );
        }
        if ( dto.getMaxDiscount() != null ) {
            entity.setMaxDiscount( dto.getMaxDiscount() );
        }

        return entity;
    }
}
