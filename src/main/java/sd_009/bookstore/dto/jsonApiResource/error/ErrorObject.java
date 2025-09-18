package sd_009.bookstore.dto.jsonApiResource.error;

import jsonapi.Resource;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorObject {
    private String status;

    private String title;

    private String detail;
}
