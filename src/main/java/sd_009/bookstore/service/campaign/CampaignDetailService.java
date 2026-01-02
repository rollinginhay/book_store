package sd_009.bookstore.service.campaign;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.campaign.CampaignDetailDto;
import sd_009.bookstore.entity.campaign.Campaign;
import sd_009.bookstore.entity.campaign.CampaignDetail;
import sd_009.bookstore.repository.BookDetailRepository;
import sd_009.bookstore.repository.CampaignDetailRepository;
import sd_009.bookstore.repository.CampaignRepository;
import sd_009.bookstore.util.mapper.campaign.CampaignDetailMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignDetailService {

    private final CampaignDetailRepository campaignDetailRepository;
    private final CampaignRepository campaignRepository;
    private final BookDetailRepository bookDetailRepository;
    private final CampaignDetailMapper campaignDetailMapper;
    private final JsonApiValidator validator;
    private final JsonApiAdapterProvider adapterProvider;

    // 🔹 Lấy toàn bộ campaign detail theo campaignId
    @Transactional(readOnly = true)
    public String findByCampaignId(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new BadRequestException("Campaign not found"));
        List<CampaignDetail> details = campaign.getCampaignDetails();

        Document<List<CampaignDetailDto>> doc = Document.with(
                details.stream().map(campaignDetailMapper::toDto).toList())
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.MULTI_CAMPAIGN_RELATIONSHIP_CAMPAIGN_DETAIL, campaignId))
                        .build().toMap()))
                .build();

        return getListAdapter().toJson(doc);
    }

    // 🔹 Lấy 1 campaign detail theo ID
    @Transactional(readOnly = true)
    public String findById(Long id) {
        CampaignDetail found = campaignDetailRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("CampaignDetail not found"));

        Document<CampaignDetailDto> doc = Document.with(campaignDetailMapper.toDto(found))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CAMPAIGN_DETAIL_BY_ID, id))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

//    @Transactional
//    public String save(String json) {
//        CampaignDetailDto dto = validator.readAndValidate(json, CampaignDetailDto.class);
//
//        Campaign campaign = campaignRepository.findById(dto.getCampaign().getId())
//                .orElseThrow(() -> new BadRequestException("Invalid campaign ID"));
//        BookDetail bookDetail = bookDetailRepository.findById(dto.getBookDetail().getId())
//                .orElseThrow(() -> new BadRequestException("Invalid book detail ID"));
//
//        boolean exists = campaignDetailRepository.findAll().stream()
//                .anyMatch(cd -> cd.getCampaign().getId().equals(dto.getCampaign().getId())
//                        && cd.getBookDetail().getId().equals(dto.getBookDetail().getId()));
//        if (exists) throw new DuplicateElementException("CampaignDetail already exists");
//
//        CampaignDetail entity = campaignDetailMapper.toEntity(dto);
//        entity.setCampaign(campaign);
//        entity.setBookDetail(bookDetail);
//
//        CampaignDetail saved = campaignDetailRepository.save(entity);
//
//        Document<CampaignDetailDto> doc = Document.with(campaignDetailMapper.toDto(saved))
//                .links(Links.from(JsonApiLinksObject.builder()
//                        .self(LinkMapper.toLink(Routes.GET_CAMPAIGN_DETAIL_BY_ID, saved.getId()))
//                        .build().toMap()))
//                .build();
//
//        return getSingleAdapter().toJson(doc);
//    }

    // 🔹 Cập nhật campaign detail
    @Transactional
    public String update(String json) {
        CampaignDetailDto dto = validator.readAndValidate(json, CampaignDetailDto.class);
        if (dto.getId() == null) throw new BadRequestException("No identifier found");

        CampaignDetail existing = campaignDetailRepository.findById(Long.valueOf(dto.getId()))
                .orElseThrow(() -> new BadRequestException("CampaignDetail not found"));

        CampaignDetail updated = campaignDetailRepository.save(campaignDetailMapper.partialUpdate(dto, existing));

        Document<CampaignDetailDto> doc = Document.with(campaignDetailMapper.toDto(updated))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CAMPAIGN_DETAIL_BY_ID, updated.getId()))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // 🔹 Xóa mềm campaign detail
    @Transactional
    public void delete(Long id) {
        campaignDetailRepository.findById(id).ifPresent(e -> {
            e.setEnabled(false);
            campaignDetailRepository.save(e);
        });
    }

    private JsonAdapter<Document<CampaignDetailDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(CampaignDetailDto.class);
    }

    private JsonAdapter<Document<List<CampaignDetailDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(CampaignDetailDto.class);
    }
}
