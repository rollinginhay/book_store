package sd_009.bookstore.controller.book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.service.book.CreatorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Creator CRUD")
public class CreatorController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final CreatorService creatorService;

    @Operation(
            summary = "Get creators by query",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get creator by query req", externalValue = "/jsonExample/creator/get_creator.json"))))
    @GetMapping(Routes.GET_CREATORS)
    public ResponseEntity<Object> getCreators(@RequestParam(required = false, name = "q") String keyword,
                                              @RequestParam(name = "e") Boolean enabled,
                                              @RequestParam int page,
                                              @RequestParam int limit,
                                              @RequestParam(required = false) List<String> sort) {
        if (keyword == null) {
            keyword = "";
        }
        if (enabled == null) {
            enabled = true;
        }

        Sort sortInstance = Sort.unsorted();

        if (sort != null && !sort.isEmpty()) {
            for (String query : sort) {
                String[] queries = query.split(";");
                String field = queries[0];
                String order = queries[1];

                if (order.equals("asc")) {
                    sortInstance = sortInstance.and(Sort.by(field));
                } else {
                    sortInstance = sortInstance.and(Sort.by(field).descending());
                }

            }
        } else {
            sortInstance = Sort.by("updatedAt").descending();
        }


        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(creatorService.find(enabled, keyword, PageRequest.of(page, limit).withSort(sortInstance)));
    }

    @Operation(
            summary = "Get creator by id, with attached relationship",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get creator by id resp", externalValue = "/jsonExample/creator/get_creator.json"))))
    @GetMapping(Routes.GET_CREATOR_BY_ID)
    public ResponseEntity<Object> getCreatorById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(creatorService.findById(id));
    }

    @Operation(
            summary = "Create a new creator",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create creator req", externalValue = "/jsonExample/creator/post_creator.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create creator resp", externalValue = "/jsonExample/creator/get_creator.json"))))
    @PostMapping(Routes.POST_CREATOR_CREATE)
    public ResponseEntity<Object> createCreator(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(creatorService.save(json));
    }

    @Operation(
            summary = "Update a creator",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create creator req", externalValue = "/jsonExample/creator/put_creator.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create creator resp", externalValue = "/jsonExample/creator/get_creator.json"))))
    @PutMapping(Routes.PUT_CREATOR_UPDATE)
    public ResponseEntity<Object> updateCreator(@RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(creatorService.update(json));
    }

    @Operation(description = "Delete a creator")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping(Routes.DELETE_CREATOR_DELETE)
    public ResponseEntity<Object> deleteCreator(@PathVariable Long id) {
        creatorService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }
}
