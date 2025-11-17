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
import sd_009.bookstore.service.book.BookService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Book CRUD")
public class BookController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final BookService bookService;

    @Operation(
            summary = "Get books by query",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get books resp", externalValue = "/jsonExample/book/get_books.json"))))

    @GetMapping(Routes.GET_BOOKS)
    public ResponseEntity<Object> getBooks(
            @RequestParam(required = false, name = "q") String keyword,
            @RequestParam(name = "e") Boolean enabled,
            @RequestParam int page,
            @RequestParam int limit,
            @RequestParam(required = false) List<String> sort,
            @RequestParam(required = false, name = "genre") String genreName // giữ filter của m
    ) {

        // 🟦 Giữ logic của master để tránh null
        if (keyword == null) {
            keyword = "";
        }
        if (enabled == null) {
            enabled = true;
        }

        // 🟦 Logic sort của m — an toàn hơn
        Sort sortInstance = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            for (String query : sort) {
                String[] queries = query.split(";");
                String field = queries[0];
                String order = queries.length > 1 ? queries[1] : "asc";

                sortInstance = order.equalsIgnoreCase("asc")
                        ? sortInstance.and(Sort.by(field))
                        : sortInstance.and(Sort.by(field).descending());
            }
        } else {
            // 🟦 Giữ default sort của master
            sortInstance = Sort.by("createdAt").descending();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(bookService.find(
                        enabled,
                        keyword,
                        PageRequest.of(page, limit).withSort(sortInstance),
                        genreName // giữ filter theo thể loại
                ));
    }





    @Operation(
            summary = "Get book by id, with attached relationship",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get book by id resp", externalValue = "/jsonExample/book/get_book.json"))))
    @GetMapping(Routes.GET_BOOK_BY_ID)
    public ResponseEntity<Object> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.findById(id));
    }

    @Operation(
            summary = "Create a new book",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create book req", externalValue = "/jsonExample/book/post_book.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create book resp", externalValue = "/jsonExample/book/get_book.json"))))
    @PostMapping(Routes.POST_BOOK_CREATE)
    public ResponseEntity<Object> createBook(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(bookService.save(json));
    }

    @Operation(
            summary = "Update a book",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create book req", externalValue = "/jsonExample/book/put_book.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create book resp", externalValue = "/jsonExample/book/get_book.json"))))
    @PutMapping(Routes.PUT_BOOK_UPDATE)
    public ResponseEntity<Object> updateBook(@RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.update(json));
    }

    @Operation(description = "Delete a book")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping(Routes.DELETE_BOOK_DELETE)
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }

    @Operation(
            summary = "Get bookDetails of book",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get bookDetails of book", externalValue = "/jsonExample/bookDetail/get_bookDetails.json"))))
    @GetMapping(Routes.MULTI_BOOK_RELATIONSHIP_BOOK_DETAIL)
    public ResponseEntity<Object> getBookDetailsByBook(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.getDependents(id, "bookDetail"));
    }

    @Operation(
            summary = "Get reviews of book",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get reviews of book", externalValue = "/jsonExample/review/get_reviews.json"))))
    @GetMapping(Routes.MULTI_BOOK_RELATIONSHIP_REVIEW)
    public ResponseEntity<Object> getReviewsByBook(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.getDependents(id, "review"));
    }

    @Operation(
            summary = "Attach relationship to book",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                    @ExampleObject(name = "Attach genre req", externalValue = "/jsonExample/genre/get_genre.json"),
                    @ExampleObject(name = "Attach bookDetail", externalValue = "/jsonExample/bookDetail/get_bookDetail.json")
            })),
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get book by id resp", externalValue = "/jsonExample/book/get_book.json"))))
    @PostMapping(Routes.MULTI_BOOK_RELATIONSHIP_GENERIC)
    public ResponseEntity<Object> attachRelationship(@PathVariable(name = "id") Long id, @PathVariable(name = "dependent") String dependent, @RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.attachOrReplaceRelationship(id, json, dependent));
    }

    @Operation(
            summary = "Detach relationship from book",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                    @ExampleObject(name = "Detach genre req", externalValue = "/jsonExample/genre/get_genre.json"),
                    @ExampleObject(name = "Detach bookDetail", externalValue = "/jsonExample/bookDetail/get_bookDetail.json")
            })),
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get book by id resp", externalValue = "/jsonExample/book/get_book.json"))))
    @DeleteMapping(Routes.MULTI_BOOK_RELATIONSHIP_GENERIC)
    public ResponseEntity<Object> detachRelationship(@PathVariable(name = "id") Long id, @PathVariable(name = "dependent") String dependent, @RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookService.detachRelationShip(id, json, dependent));
    }
}
