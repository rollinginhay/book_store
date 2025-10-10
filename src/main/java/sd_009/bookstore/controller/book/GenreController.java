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
import sd_009.bookstore.dto.jsonApiResource.book.GenreDto;
import sd_009.bookstore.dto.jsonApiResource.book.GenreOwningDto;
import sd_009.bookstore.service.book.GenreService;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Genre CRUD")
public class GenreController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final GenreService genreService;


    @GetMapping("/genres")
    @Operation(description = "Get genres by query")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = GenreDto.class)))
    public ResponseEntity<Object> getGenres(@RequestParam(required = false, name = "q") String keyword,
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
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(genreService.find(enabled, keyword, PageRequest.of(page, limit).withSort(sortInstance)));
    }

    @Operation(description = "Get genre by id, with attached relationship")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = GenreOwningDto.class)))
    @GetMapping("/genre/{id}")
    public ResponseEntity<Object> getGenreById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(genreService.findById(id));
    }

    @Operation(description = "Create a new genre")
    @ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = GenreDto.class)))
    @PostMapping("/genre/create")
    public ResponseEntity<Object> createGenre(@Valid @RequestBody GenreDto genreDto) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(genreService.save(genreDto));
    }

    @Operation(description = "Update a genre")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = GenreDto.class)))
    @PutMapping("/genre/update")
    public ResponseEntity<Object> updateGenre(@Valid @RequestBody GenreDto genreDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(genreService.update(genreDto));
    }

    @Operation(description = "Delete a genre")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping("/genre/delete/{id}")
    public ResponseEntity<Object> deleteGenre(@PathVariable Long id) {
        genreService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }
}

