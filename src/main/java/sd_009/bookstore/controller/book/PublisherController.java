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
import sd_009.bookstore.service.book.PublisherService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Publisher CRUD")
public class PublisherController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final PublisherService publisherService;

    @Operation(
            summary = "Get publishers by query",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get publishers resp", externalValue = "/jsonExample/publisher/get_publishers.json"))))
    @GetMapping(Routes.GET_PUBLISHERS)
    public ResponseEntity<Object> getPublishers(@RequestParam(required = false, name = "q") String keyword,
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
            sortInstance = Sort.by("createdAt").descending();
        }

        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(publisherService.find(enabled, keyword, PageRequest.of(page, limit).withSort(sortInstance)));
    }

    @Operation(
            summary = "Get publisher by id, with attached relationship",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get publisher by id resp", externalValue = "/jsonExample/publisher/get_publisher.json"))))
    @GetMapping(Routes.GET_PUBLISHER_BY_ID)
    public ResponseEntity<Object> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(publisherService.findById(id));
    }

    @Operation(
            summary = "Create a new publisher",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create publisher req", externalValue = "/jsonExample/publisher/post_publisher.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create publisher resp", externalValue = "/jsonExample/publisher/get_publisher.json"))))
    @PostMapping(Routes.POST_PUBLISHER_CREATE)
    public ResponseEntity<Object> createPublisher(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(publisherService.save(json));
    }

    @Operation(
            summary = "Update a publisher",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create publisher req", externalValue = "/jsonExample/publisher/put_publisher.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create publisher resp", externalValue = "/jsonExample/publisher/get_publisher.json"))))
    @PutMapping(Routes.PUT_PUBLISHER_UPDATE)
    public ResponseEntity<Object> updatePublisher(@RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(publisherService.update(json));
    }

    @Operation(description = "Delete a publisher")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping(Routes.DELETE_PUBLISHER_DELETE)
    public ResponseEntity<Object> deletePublisher(@PathVariable Long id) {
        publisherService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }
}

