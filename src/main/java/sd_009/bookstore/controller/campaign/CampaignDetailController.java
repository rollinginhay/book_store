package sd_009.bookstore.controller.campaign;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.service.campaign.CampaignDetailService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Campaign Detail CRUD", description = "Quản lý chi tiết chiến dịch khuyến mãi")
public class CampaignDetailController {

    @Value("${config.jsonapi.contentType}")
    private String contentType;

    private final CampaignDetailService campaignDetailService;

    @Operation(
            summary = "Get campaign details by campaign ID",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(examples = @ExampleObject(
                            name = "Get campaignDetails of campaign",
                            externalValue = "/jsonExample/campaign/get_campaignDetails.json"
                    ))
            )
    )
    @GetMapping("/v1/campaign/{campaignId}/relationships/campaignDetail")
    public ResponseEntity<Object> getByCampaignId(@PathVariable Long campaignId) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(campaignDetailService.findByCampaignId(campaignId));
    }

    @Operation(
            summary = "Get campaign detail by ID",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(examples = @ExampleObject(
                            name = "Get campaignDetail by id resp",
                            externalValue = "/jsonExample/campaign/get_campaignDetail.json"
                    ))
            )
    )
    @GetMapping(Routes.GET_CAMPAIGN_DETAIL_BY_ID)
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(campaignDetailService.findById(id));
    }

    @Operation(
            summary = "Create new campaign detail",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(
                            name = "Create campaignDetail req",
                            externalValue = "/jsonExample/campaign/post_campaignDetail.json"
                    ))
            ),
            responses = @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(examples = @ExampleObject(
                            name = "Create campaignDetail resp",
                            externalValue = "/jsonExample/campaign/get_campaignDetail.json"
                    ))
            )
    )
    @PostMapping(Routes.POST_CAMPAIGN_DETAIL_CREATE)
    public ResponseEntity<Object> create(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.valueOf(contentType))
                .body(campaignDetailService.save(json));
    }

    @Operation(
            summary = "Update campaign detail",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(
                            name = "Update campaignDetail req",
                            externalValue = "/jsonExample/campaign/put_campaignDetail.json"
                    ))
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Updated",
                    content = @Content(examples = @ExampleObject(
                            name = "Update campaignDetail resp",
                            externalValue = "/jsonExample/campaign/get_campaignDetail.json"
                    ))
            )
    )
    @PutMapping(Routes.PUT_CAMPAIGN_DETAIL_UPDATE)
    public ResponseEntity<Object> update(@RequestBody String json) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(campaignDetailService.update(json));
    }

    @Operation(summary = "Soft delete campaign detail")
    @DeleteMapping(Routes.DELETE_CAMPAIGN_DETAIL_DELETE)
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        campaignDetailService.delete(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(null);
    }
}
