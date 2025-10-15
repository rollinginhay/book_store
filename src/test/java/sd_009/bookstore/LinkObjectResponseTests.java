package sd_009.bookstore;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailDto;
import sd_009.bookstore.dto.jsonApiResource.book.BookDetailOwningDto;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.repository.BookDetailRepository;
import sd_009.bookstore.service.book.GenreService;
import sd_009.bookstore.util.mapper.book.BookDetailMapper;
import sd_009.bookstore.util.mapper.book.BookDetailOwningMapper;

@SpringBootTest
public class LinkObjectResponseTests {
    @Autowired
    GenreService genreService;
    @Autowired
    BookDetailRepository bookDetailRepository;
    @Autowired
    BookDetailMapper bookDetailMapper;
    @Autowired
    BookDetailOwningMapper bookDetailOwningMapper;
    @Autowired
    JsonApiAdapterProvider jsonApiAdapterProvider;

    @Test
    public void serializeSuccessfulWithLinks() {
        String json = genreService.
                find(true, "g", PageRequest.of(0, 10, Sort.by("name").descending().and(Sort.by("id").ascending())));

        System.out.println(json);
    }


    @Test
    void reverseLinkedDtoTest() {
        BookDetail bookDetail = bookDetailRepository.findById(1L).orElseThrow();

        BookDetailDto dto = bookDetailMapper.toDto(bookDetail);

        Document<BookDetailDto> doc = Document.with(dto).build();

        JsonAdapter<Document<BookDetailDto>> adapter = jsonApiAdapterProvider.singleResourceAdapter(BookDetailDto.class);

        String json = adapter.toJson(doc);

        System.out.println(json);

    }

    @Test
    void owningBookDetailTest() {
        BookDetail bookDetail = bookDetailRepository.findById(1L).orElseThrow();

        BookDetailOwningDto dto = bookDetailOwningMapper.toDto(bookDetail);

        Document<BookDetailOwningDto> doc = Document.with(dto).build();

        JsonAdapter<Document<BookDetailOwningDto>> adapter = jsonApiAdapterProvider.singleResourceAdapter(BookDetailOwningDto.class);

        String json = adapter.toJson(doc);

        System.out.println(json);
    }

    @Test
    void testPutRequestCreate() {
        String json = "{\n" +
                "  \"data\": {\n" +
                "    \"type\": \"genre\",\n" +
                "    \"relationships\": {\n" +
                "      \"books\": {\n" +
                "        \"data\": [\n" +
                "          {\n" +
                "            \"type\": \"book\",\n" +
                "            \"id\": \"37\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    \"attributes\": {\n" +
                "      \"createdAt\": \"2025-10-01T18:29:52.390751\",\n" +
                "      \"enabled\": true,\n" +
                "      \"name\": \"test genre\",\n" +
                "      \"updatedAt\": \"2025-10-01T18:29:52.390751\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        System.out.println(genreService.save(json));
    }

}
