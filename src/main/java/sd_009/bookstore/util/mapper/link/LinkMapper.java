package sd_009.bookstore.util.mapper.link;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * Builds a URI with query params and path variables
 **/
@Slf4j
public final class LinkMapper {
    public static String toLink(String path, Object... pathVars) {
        //defer to other signature as no params
        return toLinkWithQuery(path, null, pathVars);
    }

    public static String toLinkWithQuery(String path, Map<String, ?> queryParams, Object... pathVars) {
        //compomentBuilder processes pathVar syntax
        //path from current request
        UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath().path(path);

        //add params first, buildAndExpand is final
        if (queryParams != null) {
            for (Map.Entry<String, ?> entry : queryParams.entrySet()) {
                Object value = entry.getValue();
                if (value == null) continue;
                //note: can pass collection values for param
                builder.queryParam(entry.getKey(), value);
            }
        }
        //add pathVars
        UriComponents uriComponents = pathVars != null && pathVars.length > 0 ?
                builder.buildAndExpand(pathVars) :
                builder.build();


        return uriComponents.toUriString();
    }
}


