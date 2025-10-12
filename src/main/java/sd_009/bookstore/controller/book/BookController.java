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
import sd_009.bookstore.dto.jsonApiResource.book.*;
import sd_009.bookstore.service.book.BookService;
import sd_009.bookstore.util.spec.Routes;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Tag(name = "Book CRUD")
public class BookController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final BookService bookService;


    @GetMapping("/books")
    @Operation(description = "Get books by query")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = BookDto.class)))
    public ResponseEntity<Object> getBooks(@RequestParam(required = false, name = "q") String keyword,
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
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.find(enabled, keyword, PageRequest.of(page, limit).withSort(sortInstance)));
    }

    @Operation(description = "Get book by id, with attached relationship")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = BookDto.class)))
    @GetMapping("/book/{id}")
    public ResponseEntity<Object> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.findById(id));
    }

    @Operation(description = "Create a new book")
    @ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = BookDto.class)))
    @PostMapping("/book/create")
    public ResponseEntity<Object> createBook(@Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(bookService.save(bookDto));
    }

    @Operation(description = "Update a book")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = BookDto.class)))
    @PutMapping("/book/update")
    public ResponseEntity<Object> updateBook(@Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.update(bookDto));
    }

    @Operation(description = "Delete a book")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping("/book/delete/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }

    @PostMapping(Routes.BOOK_RELATIONSHIP_BOOK_DETAIL_PATH)
    public ResponseEntity<Object> attachBookDetail(@PathVariable Long id, @Valid @RequestBody BookDetailDto bookDetailDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.attachOrReplaceRelationship(id, bookDetailDto));
    }

    @DeleteMapping(Routes.BOOK_RELATIONSHIP_BOOK_DETAIL_PATH)
    public ResponseEntity<Object> detachBookDetail(@PathVariable Long id, @Valid @RequestBody BookDetailDto bookDetailDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.detachRelationShip(id, bookDetailDto));
    }

    @PostMapping(Routes.BOOK_RELATIONSHIP_BOOK_REVIEW_PATH)
    public ResponseEntity<Object> attachReview(@PathVariable Long id, @Valid @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.attachOrReplaceRelationship(id, reviewDto));
    }

    @PostMapping(Routes.BOOK_RELATIONSHIP_BOOK_REVIEW_PATH)
    public ResponseEntity<Object> detachReview(@PathVariable Long id, @Valid @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.detachRelationShip(id, reviewDto));
    }

    @PostMapping(Routes.BOOK_RELATIONSHIP_GENRE_PATH)
    public ResponseEntity<Object> attachGenre(@PathVariable Long id, @Valid @RequestBody GenreDto genreDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.attachOrReplaceRelationship(id, genreDto));
    }

    @PostMapping(Routes.BOOK_RELATIONSHIP_GENRE_PATH)
    public ResponseEntity<Object> detachGenre(@PathVariable Long id, @Valid @RequestBody GenreDto genreDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.detachRelationShip(id, genreDto));
    }

    @PostMapping(Routes.BOOK_RELATIONSHIP_CREATOR_PATH)
    public ResponseEntity<Object> attachCreator(@PathVariable Long id, @Valid @RequestBody CreatorDto creatorDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.attachOrReplaceRelationship(id, creatorDto));
    }

    @PostMapping(Routes.BOOK_RELATIONSHIP_CREATOR_PATH)
    public ResponseEntity<Object> detachCreator(@PathVariable Long id, @Valid @RequestBody CreatorDto creatorDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.detachRelationShip(id, creatorDto));
    }

    @PostMapping(Routes.BOOK_RELATIONSHIP_PUBLISHER_PATH)
    public ResponseEntity<Object> attachPublisher(@PathVariable Long id, @Valid @RequestBody PublisherDto publisherDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.attachOrReplaceRelationship(id, publisherDto));
    }

    @PostMapping(Routes.BOOK_RELATIONSHIP_PUBLISHER_PATH)
    public ResponseEntity<Object> detachPublisher(@PathVariable Long id, @Valid @RequestBody PublisherDto publisherDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.detachRelationShip(id, publisherDto));
    }

    @PostMapping(Routes.BOOK_RELATIONSHIP_SERIES_PATH)
    public ResponseEntity<Object> attachSeries(@PathVariable Long id, @Valid @RequestBody SeriesDto SeriesDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.attachOrReplaceRelationship(id, SeriesDto));
    }

    @PostMapping(Routes.BOOK_RELATIONSHIP_SERIES_PATH)
    public ResponseEntity<Object> detachSeries(@PathVariable Long id, @Valid @RequestBody SeriesDto SeriesDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.detachRelationShip(id, SeriesDto));
    }
}
