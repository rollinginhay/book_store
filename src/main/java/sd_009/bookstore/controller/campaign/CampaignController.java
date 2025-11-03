package sd_009.bookstore.controller.campaign;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.service.campaign.CampaignService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Campaign CRUD", description = "Quản lý chiến dịch khuyến mãi")
public class CampaignController {

    @Value("${config.jsonapi.contentType}")
    private String contentType;

    private final CampaignService campaignService;

    @Operation(summary = "Get all campaigns")
    @GetMapping(Routes.GET_CAMPAIGNS)
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(campaignService.findAll());
    }

    @Operation(summary = "Get campaign by ID")
    @GetMapping(Routes.GET_CAMPAIGN_BY_ID)
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(campaignService.findById(id));
    }

    @Operation(summary = "Create campaign")
    @PostMapping(Routes.POST_CAMPAIGN_CREATE)
    public ResponseEntity<Object> create(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.valueOf(contentType))
                .body(campaignService.save(json));
    }

    @Operation(summary = "Update campaign")
    @PutMapping(Routes.PUT_CAMPAIGN_UPDATE)
    public ResponseEntity<Object> update(@RequestBody String json) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(campaignService.update(json));
    }

    @Operation(summary = "Soft delete campaign")
    @DeleteMapping(Routes.DELETE_CAMPAIGN_DELETE)
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        campaignService.delete(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(null);
    }
}
