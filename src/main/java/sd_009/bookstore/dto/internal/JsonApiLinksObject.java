package sd_009.bookstore.dto.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class JsonApiLinksObject {
    private String self;
    private String first;
    private String last;
    private String next;
    private String prev;

    public Map<String, String> toMap() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(this, new TypeReference<Map<String, String>>() {
        });
    }
}
