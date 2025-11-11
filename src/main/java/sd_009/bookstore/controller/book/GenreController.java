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
import sd_009.bookstore.service.book.GenreService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Genre CRUD")
public class GenreController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final GenreService genreService;


    @Operation(
            summary = "Get genres by query",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get genres resp", externalValue = "/jsonExample/genre/get_genres.json"))))
    @GetMapping("/v1/genres/demo")
    public ResponseEntity<Object> getDemoGenres() {
        var genres = List.of(
                Map.of("id", 1, "name", "Kỹ năng sống"),
                Map.of("id", 2, "name", "Thiếu nhi"),
                Map.of("id", 3, "name", "Kinh doanh"),
                Map.of("id", 4, "name", "Sách trong nước"),
                Map.of("id", 5, "name", "Sách nước ngoài")
        );
        return ResponseEntity.ok().body(genres);
    }



    @GetMapping(Routes.GET_GENRES)
    public ResponseEntity<Object> getGenres(@RequestParam(required = false, name = "q") String keyword,
                                            @RequestParam(required = false, name = "e") Boolean enabled,
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
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(genreService.find(enabled, keyword, PageRequest.of(page, limit).withSort(sortInstance)));
    }

    @Operation(
            summary = "Get genre by id, with attached relationship",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get genre by id resp", externalValue = "/jsonExample/genre/get_genre.json"))))
    @GetMapping(Routes.GET_GENRE_BY_ID)
    public ResponseEntity<Object> getGenreById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(genreService.findById(id));
    }

    @Operation(
            summary = "Create a new genre",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create genre req", externalValue = "/jsonExample/genre/post_genre.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create genre resp", externalValue = "/jsonExample/genre/get_genre.json"))))
    @PostMapping(Routes.POST_GENRE_CREATE)
    public ResponseEntity<Object> createGenre(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(genreService.save(json));
    }

    @Operation(
            summary = "Update a genre",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create genre req", externalValue = "/jsonExample/genre/put_genre.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create genre resp", externalValue = "/jsonExample/genre/get_genre.json"))))
    @PutMapping(Routes.PUT_GENRE_UPDATE)
    public ResponseEntity<Object> updateGenre(@RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(genreService.update(json));
    }

    @Operation(description = "Delete a genre")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping(Routes.DELETE_GENRE_DELETE)
    public ResponseEntity<Object> deleteGenre(@PathVariable Long id) {
        genreService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }
}

