package sd_009.bookstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import jsonapi.Document;
import jsonapi.JsonApiFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDetailDto;
import sd_009.bookstore.dto.jsonApiResource.receipt.ReceiptDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.repository.GenreClosureRepository;
import sd_009.bookstore.service.book.BookService;
import sd_009.bookstore.util.mapper.book.BookMapper;
import sd_009.bookstore.util.mapper.book.GenreMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@SpringBootTest
class BookStoreApplicationTests {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    JsonApiAdapterProvider adapterProvider;
    @Autowired
    BookService bookService;
    @Autowired
    private GenreClosureRepository genreClosureRepository;
    @Autowired
    private GenreMapper genreMapper;
    @Autowired
    JsonApiValidator validator;

    @Test
    void fetchingAllBooks() {
        System.out.println(
                bookService.find(
                        true,                         // enabled
                        "",                           // titleQuery
                        PageRequest.of(0, 10, Sort.by("title").descending()),
                        null,                         // genreName
                        null                          // genreId (filter.genre)
                )
        );
    }


    @Test
    void contextLoads() {
        JsonAdapter.Factory factoryBuilder = new JsonApiFactory.Builder().addType(Person.class).addType(Comment.class).build();

        Moshi moshi = new Moshi.Builder().add(factoryBuilder).build();

        JsonAdapter<Document<Comment>> adapter = moshi.adapter(Types.newParameterizedType(Document.class, Comment.class));

        Person person = new Person("1", "name");

        Comment comment = new Comment("1", "body", person);

        Document<Comment> doc = Document.with(comment).build();

        String json = adapter.toJson(doc);

        System.out.println(json);
    }

    @Test
    void bookToDto() {
        Book book = bookRepository.findAll().get(0);

        BookDto dto = bookMapper.toDto(book, genreClosureRepository, genreMapper);

        System.out.println(dto);
    }

    @Test
    void readJsonWithRelationship() throws IOException {
        String json = "{\n" +
                "  \"data\": {\n" +
                "    \"type\": \"book\",\n" +
                "    \"id\": \"0\",\n" +
                "    \"attributes\": {\n" +
                "      \"title\": \"sdfsdfsdfsdf\",\n" +
                "      \"edition\": \"\",\n" +
                "      \"language\": \"\",\n" +
                "      \"published\": \"2025-11-06\",\n" +
                "      \"imageUrl\": \"\",\n" +
                "      \"blurb\": \"\"\n" +
                "    },\n" +
                "    \"relationships\": {\n" +
                "      \"genres\": {\n" +
                "        \"data\": []\n" +
                "      },\n" +
                "      \"creators\": {\n" +
                "        \"data\": []\n" +
                "      },\n" +
                "      \"publisher\": {\n" +
                "        \"data\": {\n" +
                "          \"id\": \"1038\",\n" +
                "          \"type\": \"publisher\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"series\": {\n" +
                "        \"data\": {\n" +
                "          \"id\": \"1037\",\n" +
                "          \"type\": \"series\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"included\": [\n" +
                "    {\n" +
                "      \"type\": \"publisher\",\n" +
                "      \"id\": \"1038\",\n" +
                "      \"attributes\": {\n" +
                "        \"createdAt\": \"2025-09-24T22:05:14.818099\",\n" +
                "        \"enabled\": true,\n" +
                "        \"name\": \"publisher 1\",\n" +
                "        \"updatedAt\": \"2025-09-24T22:05:14.818099\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"series\",\n" +
                "      \"id\": \"1037\",\n" +
                "      \"attributes\": {\n" +
                "        \"createdAt\": \"2025-09-24T22:05:14.855328\",\n" +
                "        \"enabled\": true,\n" +
                "        \"name\": \"series 1\",\n" +
                "        \"updatedAt\": \"2025-09-24T22:05:14.855328\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"creator\",\n" +
                "      \"id\": \"7\",\n" +
                "      \"attributes\": {\n" +
                "        \"createdAt\": \"2025-09-24T22:05:14.872646\",\n" +
                "        \"enabled\": true,\n" +
                "        \"name\": \"creator 1\",\n" +
                "        \"updatedAt\": \"2025-09-24T22:05:14.872646\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"creator\",\n" +
                "      \"id\": \"8\",\n" +
                "      \"attributes\": {\n" +
                "        \"createdAt\": \"2025-09-24T22:05:14.875645\",\n" +
                "        \"enabled\": true,\n" +
                "        \"name\": \"creator 2\",\n" +
                "        \"updatedAt\": \"2025-09-24T22:05:14.875645\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"genre\",\n" +
                "      \"id\": \"7\",\n" +
                "      \"attributes\": {\n" +
                "        \"createdAt\": \"2025-09-24T22:05:14.877645\",\n" +
                "        \"enabled\": true,\n" +
                "        \"name\": \"genre 1\",\n" +
                "        \"updatedAt\": \"2025-09-24T22:05:14.877645\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"genre\",\n" +
                "      \"id\": \"8\",\n" +
                "      \"attributes\": {\n" +
                "        \"createdAt\": \"2025-09-24T22:05:14.880794\",\n" +
                "        \"enabled\": true,\n" +
                "        \"name\": \"genre 2\",\n" +
                "        \"updatedAt\": \"2025-09-24T22:05:14.880794\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "\n" +
                "}";

        BookDto dto = validator.readAndValidate(json, BookDto.class);
        File output = new File("src/test/resources/output/book.json");
        // Make sure parent directories exist
        output.getParentFile().mkdirs();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // optional but usually needed to avoid timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Write JSON (pretty-printed)
        mapper.writerWithDefaultPrettyPrinter().writeValue(output, dto);
    }

    @Test
    void loadJsonAsString() throws Exception {
        InputStream is = getClass().getResourceAsStream("/receipt.json");

        String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        ReceiptDto dto = validator.readAndValidate(json, ReceiptDto.class);

        List<ReceiptDetailDto> receiptDetailDtos = dto.getReceiptDetails();
        receiptDetailDtos.get(0).getBookCopy().getId();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // optional but usually needed to avoid timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String result;
        try {
            result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(receiptDetailDtos);
        } catch (JsonProcessingException e) {
            result = "failed to map";
        }
        log.warn("receiptDetails: {}", result);
    }


}
