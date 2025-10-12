package sd_009.reviewstore.controller.review;

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
import sd_009.bookstore.dto.jsonApiResource.book.ReviewDto;
import sd_009.bookstore.dto.jsonApiResource.book.ReviewOwningDto;
import sd_009.bookstore.service.book.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Tag(name = "Review CRUD")
public class ReviewController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final ReviewService reviewService;

    @Operation(description = "Create a new review")
    @ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = ReviewDto.class)))
    @PostMapping("/review/create")
    public ResponseEntity<Object> createReview(@Valid @RequestBody ReviewOwningDto reviewOwningDto) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(reviewService.save(reviewOwningDto));
    }

    @Operation(description = "Get review by id, with attached relationship")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ReviewOwningDto.class)))
    @GetMapping("/review/{id}")
    public ResponseEntity<Object> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(reviewService.findById(id));
    }

    @Operation(description = "Update a review")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ReviewOwningDto.class)))
    @PutMapping("/review/update")
    public ResponseEntity<Object> updateReview(@Valid @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(reviewService.update(reviewDto));
    }

    @Operation(description = "Delete a review")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping("/review/delete/{id}")
    public ResponseEntity<Object> deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }


}
