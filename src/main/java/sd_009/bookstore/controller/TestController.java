package sd_009.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd_009.bookstore.service.TestService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Test crud")
public class TestController {
    private final TestService testService;

    @Value("${config.jsonapi.contentType}")
    private String contentType;

    @Operation(
            summary = "create test",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "test create", externalValue = "/jsonExample/test/post_test.json"))),
            responses = @ApiResponse(content = @Content(examples = @ExampleObject(name = "test get by id", externalValue = "/jsonExample/test/get_test.json"))))
    @PostMapping("test/create")
    public ResponseEntity<Object> create(@RequestBody String json) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(testService.save(json));
    }

    @Operation(
            summary = "get test by id",
            responses = @ApiResponse(content = @Content(examples = @ExampleObject(name = "test get by id", externalValue = "/jsonExample/test/get_test.json"))))
    @GetMapping("test/{id}")
    public ResponseEntity<Object> get(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).body(testService.findById(id));
    }
}
