package sd_009.bookstore.config.jsonapi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import jsonapi.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.error.ErrorObject;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JsonApiAdapterProvider {
    private final Moshi moshi;

    public <T> JsonAdapter<Document<T>> singleResourceAdapter(Class<T> schema) {
        return moshi.adapter(Types.newParameterizedType(Document.class, schema));
    }

    public <T> JsonAdapter<Document<List<T>>> listResourceAdapter(Class<T> schema) {
        return moshi.adapter(Types.newParameterizedType(Document.class, Types.newParameterizedType(List.class, schema)));
    }

    public <T> JsonAdapter<Document<T>> getAdapter(ParameterizedTypeReference<T> typeRef) {
        return moshi.adapter(typeRef.getType());
    }

}
