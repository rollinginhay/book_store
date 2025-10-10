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
import sd_009.bookstore.dto.jsonApiResource.book.SeriesDto;
import sd_009.bookstore.dto.jsonApiResource.book.SeriesOwningDto;
import sd_009.bookstore.service.book.SeriesService;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Series CRUD")
public class SeriesController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final SeriesService seriesService;


    @GetMapping("/seriess")
    @Operation(description = "Get seriess by query")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = SeriesDto.class)))
    public ResponseEntity<Object> getSeriess(@RequestParam(required = false, name = "q") String keyword,
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
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(seriesService.find(enabled, keyword, PageRequest.of(page, limit).withSort(sortInstance)));
    }

    @Operation(description = "Get series by id, with attached relationship")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = SeriesOwningDto.class)))
    @GetMapping("/series/{id}")
    public ResponseEntity<Object> getSeriesById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(seriesService.findById(id));
    }

    @Operation(description = "Create a new series")
    @ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = SeriesDto.class)))
    @PostMapping("/series/create")
    public ResponseEntity<Object> createSeries(@Valid @RequestBody SeriesDto seriesDto) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(seriesService.save(seriesDto));
    }

    @Operation(description = "Update a series")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = SeriesDto.class)))
    @PutMapping("/series/update")
    public ResponseEntity<Object> updateSeries(@Valid @RequestBody SeriesDto seriesDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(seriesService.update(seriesDto));
    }

    @Operation(description = "Delete a series")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping("/series/delete/{id}")
    public ResponseEntity<Object> deleteSeries(@PathVariable Long id) {
        seriesService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }
}

