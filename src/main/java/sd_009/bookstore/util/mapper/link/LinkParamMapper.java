package sd_009.bookstore.util.mapper.link;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helper to return a map of viable query params for LinkMapper
 */
@Builder
public class LinkParamMapper<T> {
    private final String keyword;
    private final Boolean enabled;
    private final Page<T> page;

    private final Map<String, String> params = new HashMap<>();

    public LinkParamMapper<T> addParam(String key, String value) {
        if (value != null) {
            params.put(key, value);
        }
        return this;
    }

    private Map<String, String> toParamsMap(Page<T> page, int pageParam) {

        if (keyword != null && !keyword.isEmpty()) {
            params.put("q", keyword);
        }

        params.put("e", enabled == null ? "true" : enabled.toString());

        params.put("page", pageParam + "");
        params.put("limit", page.getSize() + "");

        if (page.getSort().isSorted()) {
            String sortQuery = page.getSort().stream().map(o ->
                    o.getProperty() + "," + o.getDirection().name().toLowerCase()).collect(Collectors.joining(";"));
            params.put("sort", sortQuery);
        }
        return params;
    }

    public Map<String, String> getSelfParams() {
        return toParamsMap(page, page.getNumber());
    }

    public Map<String, String> getLastParams() {
        return toParamsMap(page, page.getTotalPages() - 1);
    }

    public Map<String, String> getFirstParams() {
        return toParamsMap(page, 0);
    }

    public Map<String, String> getNextParams() {
        if (page.isLast()) {
            return null;
        }

        return toParamsMap(page, page.getNumber() + 1);
    }

    public Map<String, String> getPrevParams() {
        if (page.isFirst()) {
            return null;
        }
        return toParamsMap(page, page.getNumber() - 1);
    }

}
