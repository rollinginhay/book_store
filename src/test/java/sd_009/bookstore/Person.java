package sd_009.bookstore;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;

@Resource(type = "person")
public record Person(
        @Id
        String id,
        String name
) {

}