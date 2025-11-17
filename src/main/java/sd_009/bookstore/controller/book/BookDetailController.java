package sd_009.bookstore.controller.book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.service.book.BookDetailService;

@RestController
@RequiredArgsConstructor
@Tag(name = "BookDetail CRUD")
public class BookDetailController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final BookDetailService bookDetailService;

    @Operation(
            summary = "Get bookDetail by id, with attached relationship",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get bookDetail by id resp", externalValue = "/jsonExample/bookDetail/get_bookDetails.json"))))
    @GetMapping(Routes.MULTI_BOOK_RELATIONSHIP_BOOK_DETAIL)
    public ResponseEntity<Object> getBookDetailsByBookId(@PathVariable Long id, @RequestParam(required = false) Boolean enabled) {
        if (enabled == null) {
            enabled = true;
        }
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookDetailService.findByBookId(enabled, id));
    }

    @Operation(
            summary = "Get by BookId",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get bookDetail by bookId", externalValue = "/jsonExample/bookDetail/get_bookDetail.json"))))

    @GetMapping(Routes.GET_BOOK_DETAIL_BY_ID)
    public ResponseEntity<Object> getBookDetailById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookDetailService.findById(id));
    }


    @Operation(
            summary = "Create a new bookDetail",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create bookDetail req", externalValue = "/jsonExample/bookDetail/post_bookDetail.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create bookDetail resp", externalValue = "/jsonExample/bookDetail/get_bookDetail.json"))))
    @PostMapping(Routes.POST_BOOK_DETAIL_CREATE)
    public ResponseEntity<Object> createBookDetail(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(bookDetailService.save(json));
    }

    @Operation(
            summary = "Update a bookDetail",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create bookDetail req", externalValue = "/jsonExample/bookDetail/put_bookDetail.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create bookDetail resp", externalValue = "/jsonExample/bookDetail/get_bookDetail.json"))))
    @PutMapping(Routes.PUT_BOOK_DETAIL_UPDATE)
    public ResponseEntity<Object> updateBookDetail(@RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookDetailService.update(json));
    }

    @Operation(description = "Delete a bookDetail")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping(Routes.DELETE_BOOK_DETAIL_DELETE)
    public ResponseEntity<Object> deleteBookDetail(@PathVariable Long id) {
        bookDetailService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }


}
