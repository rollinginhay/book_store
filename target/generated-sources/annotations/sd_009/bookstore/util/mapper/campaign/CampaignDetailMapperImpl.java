package sd_009.bookstore.util.mapper.campaign;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.campaign.CampaignDetailDto;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.campaign.Campaign;
import sd_009.bookstore.entity.campaign.CampaignDetail;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class CampaignDetailMapperImpl implements CampaignDetailMapper {

    @Override
    public CampaignDetail toEntity(CampaignDetailDto dto) {
        if ( dto == null ) {
            return null;
        }

        CampaignDetail.CampaignDetailBuilder campaignDetail = CampaignDetail.builder();

        if ( dto.getId() != null ) {
            campaignDetail.id( Long.parseLong( dto.getId() ) );
        }
        campaignDetail.campaign( dto.getCampaign() );
        campaignDetail.bookDetail( dto.getBookDetail() );
        campaignDetail.value( dto.getValue() );

        return campaignDetail.build();
    }

    @Override
    public CampaignDetailDto toDto(CampaignDetail entity) {
        if ( entity == null ) {
            return null;
        }

        String id = null;
        Campaign campaign = null;
        BookDetail bookDetail = null;
        Double value = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;

        if ( entity.getId() != null ) {
            id = String.valueOf( entity.getId() );
        }
        campaign = entity.getCampaign();
        bookDetail = entity.getBookDetail();
        value = entity.getValue();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
        enabled = entity.getEnabled();
        note = entity.getNote();

        CampaignDetailDto campaignDetailDto = new CampaignDetailDto( id, campaign, bookDetail, value, createdAt, updatedAt, enabled, note );

        return campaignDetailDto;
    }

    @Override
    public CampaignDetail partialUpdate(CampaignDetailDto dto, CampaignDetail entity) {
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
        if ( dto.getCampaign() != null ) {
            entity.setCampaign( dto.getCampaign() );
        }
        if ( dto.getBookDetail() != null ) {
            entity.setBookDetail( dto.getBookDetail() );
        }
        if ( dto.getValue() != null ) {
            entity.setValue( dto.getValue() );
        }

        return entity;
    }
}
