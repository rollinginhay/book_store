package sd_009.bookstore.controller.book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.dto.jsonApiResource.book.PublisherDto;
import sd_009.bookstore.dto.jsonApiResource.book.PublisherOwningDto;
import sd_009.bookstore.service.book.PublisherService;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Publisher CRUD")
public class PublisherController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final PublisherService publisherService;


    @GetMapping("/publishers")
    @Operation(description = "Get publishers by query")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PublisherDto.class)))
    public ResponseEntity<Object> getPublishers(@RequestParam(required = false, name = "q") String keyword,
                                                @RequestParam(name = "e") Boolean enabled,
                                                @RequestParam int page,
                                                @RequestParam int limit,
                                                @RequestParam(required = false) List<String> sort) {
        if (keyword == null) {
            keyword = "";
        }

        Sort sortInstance = Sort.unsorted();

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
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(publisherService.find(enabled, keyword, PageRequest.of(page, limit).withSort(sortInstance)));
    }

    @Operation(description = "Get publisher by id, with attached relationship")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PublisherOwningDto.class)))
    @GetMapping("/publisher/{id}")
    public ResponseEntity<Object> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(publisherService.findById(id));
    }

    @Operation(description = "Create a new publisher")
    @ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = PublisherDto.class)))
    @PostMapping("/publisher/create")
    public ResponseEntity<Object> createPublisher(@Valid @RequestBody PublisherDto publisherDto) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(publisherService.save(publisherDto));
    }

    @Operation(description = "Update a publisher")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = PublisherDto.class)))
    @PutMapping("/publisher/update")
    public ResponseEntity<Object> updatePublisher(@Valid @RequestBody PublisherDto publisherDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(publisherService.update(publisherDto));
    }

    @Operation(description = "Delete a publisher")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping("/publisher/delete/{id}")
    public ResponseEntity<Object> deletePublisher(@PathVariable Long id) {
        publisherService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }
}

