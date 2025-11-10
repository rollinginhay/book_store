package sd_009.bookstore.dto.internal;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JsonApiMetaObject {
    private int firstPage;
    private int lastPage;
    private int totalPages;
}
