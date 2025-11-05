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
import sd_009.bookstore.service.campaign.CampaignService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Campaign CRUD", description = "Quản lý chiến dịch khuyến mãi")
public class CampaignController {

    @Value("${config.jsonapi.contentType}")
    private String contentType;

    private final CampaignService campaignService;

    // 🔹 Lấy tất cả campaign
    @Operation(
            summary = "Get all campaigns",
            description = "Lấy danh sách tất cả các chiến dịch khuyến mãi.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(examples = @ExampleObject(
                            name = "Get campaigns resp",
                            externalValue = "/jsonExample/campaign/get_campaigns.json"
                    ))
            )
    )
    @GetMapping(Routes.GET_CAMPAIGNS)
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(campaignService.findAll());
    }

    // 🔹 Lấy campaign theo ID
    @Operation(
            summary = "Get campaign by ID",
            description = "Lấy thông tin chi tiết 1 chiến dịch khuyến mãi bằng ID.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(examples = {
                            @ExampleObject(
                                    name = "Get campaign by id resp",
                                    externalValue = "/jsonExample/campaign/get_campaign.json"
                            ),
                            @ExampleObject(
                                    name = "Get campaign owning resp",
                                    externalValue = "/jsonExample/campaign/get_campaign_owning.json"
                            )
                    })
            )
    )
    @GetMapping(Routes.GET_CAMPAIGN_BY_ID)
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(campaignService.findById(id));
    }

    // 🔹 Tạo mới campaign
    @Operation(
            summary = "Create campaign",
            description = "Tạo mới 1 chiến dịch khuyến mãi.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(
                            name = "Create campaign req",
                            externalValue = "/jsonExample/campaign/post_campaign.json"
                    ))
            ),
            responses = @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(examples = @ExampleObject(
                            name = "Create campaign resp",
                            externalValue = "/jsonExample/campaign/get_campaign.json"
                    ))
            )
    )
    @PostMapping(Routes.POST_CAMPAIGN_CREATE)
    public ResponseEntity<Object> create(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.valueOf(contentType))
                .body(campaignService.save(json));
    }

    // 🔹 Cập nhật campaign
    @Operation(
            summary = "Update campaign",
            description = "Cập nhật thông tin chiến dịch khuyến mãi.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(
                            name = "Update campaign req",
                            externalValue = "/jsonExample/campaign/put_campaign.json"
                    ))
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Updated",
                    content = @Content(examples = @ExampleObject(
                            name = "Update campaign resp",
                            externalValue = "/jsonExample/campaign/get_campaign.json"
                    ))
            )
    )
    @PutMapping(Routes.PUT_CAMPAIGN_UPDATE)
    public ResponseEntity<Object> update(@RequestBody String json) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(campaignService.update(json));
    }

    // 🔹 Xóa mềm campaign
    @Operation(
            summary = "Soft delete campaign",
            description = "Xóa mềm 1 chiến dịch khuyến mãi (enabled=false)."
    )
    @DeleteMapping(Routes.DELETE_CAMPAIGN_DELETE)
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        campaignService.delete(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(null);
    }
}
