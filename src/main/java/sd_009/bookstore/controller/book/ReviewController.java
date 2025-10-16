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
import sd_009.bookstore.service.book.ReviewService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review CRUD")
public class ReviewController {
    @Value("${config.jsonapi.contentType}")
    private String contentType;
    private final ReviewService reviewService;

    @Operation(
            summary = "Get review by id, with attached relationship",
            responses = @ApiResponse(responseCode = "200", description = "Success", content = @Content(examples = @ExampleObject(name = "Get review by id resp", externalValue = "/jsonExample/review/get_review.json"))))
    @GetMapping(Routes.GET_REVIEW_BY_ID)
    public ResponseEntity<Object> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(reviewService.findById(id));
    }

    @Operation(
            summary = "Create a new review",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create review req", externalValue = "/jsonExample/review/post_review.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create review resp", externalValue = "/jsonExample/review/get_review.json"))))
    @PostMapping(Routes.POST_REVIEW_CREATE)
    public ResponseEntity<Object> createReview(@RequestBody String json) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.valueOf(contentType)).body(reviewService.save(json));
    }

    @Operation(
            summary = "Update a review",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Create review req", externalValue = "/jsonExample/review/put_review.json"))),
            responses = @ApiResponse(responseCode = "201", description = "Success", content = @Content(examples = @ExampleObject(name = "Create review resp", externalValue = "/jsonExample/review/get_review.json"))))
    @PutMapping(Routes.PUT_REVIEW_UPDATE)
    public ResponseEntity<Object> updateReview(@RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(reviewService.update(json));
    }

    @Operation(description = "Delete a review")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping(Routes.DELETE_REVIEW_DELETE)
    public ResponseEntity<Object> deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(null);
    }
}
