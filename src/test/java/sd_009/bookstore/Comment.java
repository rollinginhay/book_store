package sd_009.bookstore;

import jsonapi.Id;
import jsonapi.ToOne;
import lombok.Builder;

@Builder
public record Comment(
        @Id
        String id,
        String body,
        @ToOne(name = "author")
        Person author
) {

}
