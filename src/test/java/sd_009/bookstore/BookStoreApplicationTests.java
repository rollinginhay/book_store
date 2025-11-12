package sd_009.bookstore;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import jsonapi.Document;
import jsonapi.JsonApiFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.dto.jsonApiResource.book.BookDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.service.book.BookService;
import sd_009.bookstore.util.mapper.book.BookMapper;

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

    @Test
    void fetchingAllBooks() {
        System.out.println(bookService.find(true, "", PageRequest.of(0, 10, Sort.by("title").descending())));
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

        BookDto dto = bookMapper.toDto(book);

        System.out.println(dto);
    }



}
