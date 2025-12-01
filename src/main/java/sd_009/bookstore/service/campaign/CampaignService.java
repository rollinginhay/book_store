package sd_009.bookstore.service.campaign;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.exceptionHanding.exception.DuplicateElementException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.campaign.CampaignDto;
import sd_009.bookstore.entity.campaign.Campaign;
import sd_009.bookstore.repository.CampaignRepository;
import sd_009.bookstore.util.mapper.campaign.CampaignMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final JsonApiValidator validator;
    private final JsonApiAdapterProvider adapterProvider;

    // 🔹 Lấy tất cả campaign
    @Transactional(readOnly = true)
    public String findAll() {
        List<Campaign> list = campaignRepository.findAllByEnabled(true, Sort.by("updatedAt").descending());
        List<Campaign> filterList = list.stream().filter(e -> e.getEndDate().isAfter(LocalDateTime.now())).toList();
        List<CampaignDto> dtos = filterList.stream().map(campaignMapper::toDto).toList();

        Document<List<CampaignDto>> doc = Document.with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CAMPAIGNS))
                        .build().toMap()))
                .build();

        return getListAdapter().toJson(doc);
    }

    // 🔹 Lấy campaign theo ID
    @Transactional(readOnly = true)
    public String findById(Long id) {
        Campaign found = campaignRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Campaign not found"));
        CampaignDto dto = campaignMapper.toDto(found);

        Document<CampaignDto> doc = Document.with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CAMPAIGN_BY_ID, id))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // 🔹 Tạo mới campaign
    @Transactional
    public String save(String json) {
        CampaignDto dto = validator.readAndValidate(json, CampaignDto.class);
        if (campaignRepository.findByName(dto.getName()).isPresent()) {
            throw new DuplicateElementException("Campaign name already exists");
        }

        Campaign entity = campaignMapper.toEntity(dto);
        Campaign saved = campaignRepository.save(entity);

        Document<CampaignDto> doc = Document.with(campaignMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CAMPAIGN_BY_ID, saved.getId()))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // 🔹 Cập nhật campaign
    @Transactional
    public String update(String json) {
        CampaignDto dto = validator.readAndValidate(json, CampaignDto.class);
        if (dto.getId() == null)
            throw new BadRequestException("No identifier found");

        Campaign existing = campaignRepository.findById(Long.valueOf(dto.getId()))
                .orElseThrow(() -> new BadRequestException("Campaign not found"));

        Campaign updated = campaignRepository.save(campaignMapper.partialUpdate(dto, existing));

        Document<CampaignDto> doc = Document.with(campaignMapper.toDto(updated))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CAMPAIGN_BY_ID, updated.getId()))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // 🔹 Xóa mềm campaign
    @Transactional
    public void delete(Long id) {
        campaignRepository.findById(id).ifPresent(e -> {
            e.setEnabled(false);
            campaignRepository.save(e);
        });
    }

    private JsonAdapter<Document<CampaignDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(CampaignDto.class);
    }

    private JsonAdapter<Document<List<CampaignDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(CampaignDto.class);
    }
}
