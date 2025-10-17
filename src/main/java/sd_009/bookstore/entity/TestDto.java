package sd_009.bookstore.entity;

import jsonapi.Id;
import jsonapi.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link Test}
 */
@AllArgsConstructor
@Getter
@Resource(type = "test")
public class TestDto implements Serializable {
    @Id
    private final String id;
    private final String name;
}