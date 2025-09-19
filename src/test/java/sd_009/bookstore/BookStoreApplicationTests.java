package sd_009.bookstore;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.util.mapper.book.BookMapper;

import java.util.Map;

@SpringBootTest
class BookStoreApplicationTests {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    JsonApiAdapterProvider adapterProvider;

    @Test
    void bookToDto() {
        Book book = bookRepository.findAll().get(0);

        BookDto dto = bookMapper.toDto(book);

        System.out.println(dto);
    }

    @Test
    void bookDtoToJson() {
        Book book = bookRepository.findAll().get(0);

        BookDto dto = bookMapper.toDto(book);

        Document<BookDto> doc = Document.with(dto).links(Links.from(Map.of("self", "link.com"))).build();

        JsonAdapter<Document<BookDto>> adapter = adapterProvider.singleResourceAdapter(BookDto.class);

        String json = adapter.toJson(doc);

        System.out.println(json);
    }

}
