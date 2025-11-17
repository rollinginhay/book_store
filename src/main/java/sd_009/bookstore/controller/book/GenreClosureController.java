package sd_009.bookstore.controller.book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.service.book.GenreClosureService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Genre Closure (Cây thể loại)")
public class GenreClosureController {

    @Value("${config.jsonapi.contentType}")
    private String contentType;

    private final GenreClosureService genreClosureService;

    @Operation(summary = "Lấy toàn bộ bảng thể loại cha–con (genre_closure)")
    @GetMapping(Routes.GET_GENRE_CLOSURES)
    public ResponseEntity<Object> getAllGenreClosures(@RequestParam int page,
                                                      @RequestParam int limit) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(genreClosureService.findAll(PageRequest.of(page, limit, Sort.by("ancestor.id"))));
    }

    @Operation(summary = "Lấy danh sách thể loại con theo ID cha")
    @GetMapping(Routes.GET_GENRE_CLOSURE_DESCENDANTS)
    public ResponseEntity<Object> getDescendants(@PathVariable Long ancestorId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(genreClosureService.findDescendants(ancestorId));
    }

    @Operation(summary = "Lấy danh sách thể loại cha theo ID con")
    @GetMapping(Routes.GET_GENRE_CLOSURE_ANCESTORS)
    public ResponseEntity<Object> getAncestors(@PathVariable Long descendantId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(genreClosureService.findAncestors(descendantId));
    }
}
