package sd_009.bookstore;

import jsonapi.Id;
import jsonapi.Resource;
import jsonapi.ToOne;
import lombok.Builder;

@Builder
@Resource(type = "comment")
public record Comment(
        @Id
        String id,
        String body,
        @ToOne(name = "author")
        Person author
) {

}
