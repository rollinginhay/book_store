package sd_009.bookDetailstore.controller.bookDetail;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailOwningDto;
import sd_009.bookstore.service.book.BookDetailService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Tag(name = "BookDetail CRUD")
public class BookDetailController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final BookDetailService bookDetailService;

    @Operation(description = "Create a new bookDetail")
    @ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = BookDetailDto.class)))
    @PostMapping("/bookDetail/create")
    public ResponseEntity<Object> createBookDetail(@Valid @RequestBody BookDetailOwningDto bookDetailOwningDto) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(bookDetailService.save(bookDetailOwningDto));
    }

    @Operation(description = "Get creator by id, with attached relationship")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = BookDetailOwningDto.class)))
    @GetMapping("/bookDetail/{id}")
    public ResponseEntity<Object> getBookDetailById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookDetailService.findById(id));
    }

    @Operation(description = "Update a bookDetail")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = BookDetailOwningDto.class)))
    @PutMapping("/bookDetail/update")
    public ResponseEntity<Object> updateBookDetail(@Valid @RequestBody BookDetailDto bookDetailDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(bookDetailService.update(bookDetailDto));
    }

    @Operation(description = "Delete a bookDetail")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping("/bookDetail/delete/{id}")
    public ResponseEntity<Object> deleteBookDetail(@PathVariable Long id) {
        bookDetailService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }


}
