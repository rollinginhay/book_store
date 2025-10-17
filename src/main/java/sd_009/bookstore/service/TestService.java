package sd_009.bookstore.service;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.entity.Test;
import sd_009.bookstore.entity.TestDto;
import sd_009.bookstore.entity.TestMapper;
import sd_009.bookstore.entity.TestRepository;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

@Service
@RequiredArgsConstructor
public class TestService {
    private final JsonApiAdapterProvider adapterProvider;
    private final TestRepository testRepository;
    private final TestMapper testMapper;
    private final JsonApiValidator jsonApiValidator;

    public String findById(Long id) {
        Test test = testRepository.findById(id).orElseThrow();

        Document<TestDto> doc = Document.with(testMapper.toDto(test))
                // books/{id}
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_BOOK_BY_ID, id))
                        .build()
                        .toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    @Transactional
    public String save(String json) {
        TestDto dto = jsonApiValidator.readAndValidate(json, TestDto.class);

        Test saved = testRepository.save(testMapper.toEntity(dto));

        return getSingleAdapter().toJson(Document
                .with(testMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_BOOK_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    private JsonAdapter<Document<TestDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(TestDto.class);
    }


}
