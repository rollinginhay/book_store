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
import sd_009.bookstore.service.book.SeriesService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Series CRUD")
public class SeriesController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final SeriesService seriesService;

    @Operation(
            summary = "Get seriess by query",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get seriess resp", externalValue = "/jsonExample/series/get_seriess.json"))))

    @GetMapping(Routes.GET_SERIES)
    public ResponseEntity<Object> getSeriess(@RequestParam(required = false, name = "q") String keyword,
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

        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(seriesService.find(enabled, keyword, PageRequest.of(page, limit).withSort(sortInstance)));
    }

    @Operation(
            summary = "Get series by id, with attached relationship",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get series by id resp", externalValue = "/jsonExample/series/get_series_owning.json"))))
    @GetMapping(Routes.GET_SERIES_BY_ID)
    public ResponseEntity<Object> getSeriesById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(seriesService.findById(id));
    }

    @Operation(
            summary = "Create a new series",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create series req", externalValue = "/jsonExample/series/post_series.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create series resp", externalValue = "/jsonExample/series/get_series.json"))))
    @PostMapping(Routes.POST_SERIES_CREATE)
    public ResponseEntity<Object> createSeries(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(seriesService.save(json));
    }

    @Operation(
            summary = "Update a series",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create series req", externalValue = "/jsonExample/series/put_series.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create series resp", externalValue = "/jsonExample/series/get_series.json"))))
    @PutMapping(Routes.PUT_SERIES_UPDATE)
    public ResponseEntity<Object> updateSeries(@RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(seriesService.update(json));
    }

    @Operation(description = "Delete a series")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping(Routes.DELETE_SERIES_DELETE)
    public ResponseEntity<Object> deleteSeries(@PathVariable Long id) {
        seriesService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }
}

