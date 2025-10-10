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
import sd_009.bookstore.dto.jsonApiResource.book.CreatorDto;
import sd_009.bookstore.dto.jsonApiResource.book.CreatorOwningDto;
import sd_009.bookstore.service.book.CreatorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Tag(name = "Creator CRUD")
public class CreatorController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final CreatorService creatorService;


    @GetMapping("/creators")
    @Operation(description = "Get creators by query")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = CreatorDto.class)))
    public ResponseEntity<Object> getCreators(@RequestParam(required = false, name = "q") String keyword,
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
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(creatorService.find(enabled, keyword, PageRequest.of(page, limit).withSort(sortInstance)));
    }

    @Operation(description = "Get creator by id, with attached relationship")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = CreatorOwningDto.class)))
    @GetMapping("/creator/{id}")
    public ResponseEntity<Object> getCreatorById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(creatorService.findById(id));
    }

    @Operation(description = "Create a new creator")
    @ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = CreatorDto.class)))
    @PostMapping("/creator/create")
    public ResponseEntity<Object> createCreator(@Valid @RequestBody CreatorDto creatorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(creatorService.save(creatorDto));
    }

    @Operation(description = "Update a creator")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = CreatorDto.class)))
    @PutMapping("/creator/update")
    public ResponseEntity<Object> updateCreator(@Valid @RequestBody CreatorDto creatorDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(creatorService.update(creatorDto));
    }

    @Operation(description = "Delete a creator")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping("/creator/delete/{id}")
    public ResponseEntity<Object> deleteCreator(@PathVariable Long id) {
        creatorService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }

}
