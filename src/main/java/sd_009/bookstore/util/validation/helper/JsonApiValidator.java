package sd_009.bookstore.util.validation.helper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class JsonApiValidator {
    private final Validator validator;
    private final JsonApiAdapterProvider adapterProvider;

    public <T> T readAndValidate(final String json, final Class<T> dtoType) {
        try {
            T data = adapterProvider.singleResourceAdapter(dtoType).fromJson(json).dataOrThrow();

            Set<ConstraintViolation<T>> violations = validator.validate(data);

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }

            return data;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
