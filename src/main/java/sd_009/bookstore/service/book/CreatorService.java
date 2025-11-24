package sd_009.bookstore.service.book;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import jsonapi.Meta;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.DependencyConflictException;
import sd_009.bookstore.config.exceptionHanding.exception.DuplicateElementException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.internal.JsonApiMetaObject;
import sd_009.bookstore.dto.jsonApiResource.book.CreatorDto;
import sd_009.bookstore.dto.jsonApiResource.book.CreatorOwningDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Creator;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.repository.CreatorRepository;
import sd_009.bookstore.util.mapper.book.CreatorMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.link.LinkParamMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreatorService {
    private final JsonApiAdapterProvider adapterProvider;
    private final CreatorMapper creatorMapper;
    private final CreatorRepository creatorRepository;
    private final BookRepository bookRepository;
    private final JsonApiValidator jsonApiValidator;

    @Transactional
    public String find(Boolean enabled, String name, Pageable pageable) {

        Page<Creator> page;
        if (name == null || name.isEmpty()) {
            page = creatorRepository.findByEnabled(enabled, pageable);
        } else {
            page = creatorRepository.findByNameContainingAndEnabled(name, enabled, pageable);
        }
        List<CreatorDto> dtos = page.getContent().stream().map(creatorMapper::toDto).toList();

        LinkParamMapper<Creator> paramMapper = LinkParamMapper.<Creator>builder()
                .keyword(name)
                .enabled(enabled)
                .page(page)
                .build();

        Document<List<CreatorDto>> doc = Document
                .with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLinkWithQuery(Routes.GET_CREATORS, paramMapper.getSelfParams()))
                        .first(LinkMapper.toLinkWithQuery(Routes.GET_CREATORS, paramMapper.getFirstParams()))
                        .last(LinkMapper.toLinkWithQuery(Routes.GET_CREATORS, paramMapper.getLastParams()))
                        //has to manually check for null in case of invalid pages
                        .next(paramMapper.getNextParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_CREATORS, paramMapper.getNextParams()))
                        .prev(paramMapper.getPrevParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_CREATORS, paramMapper.getPrevParams()))
                        .build().toMap()))
                .meta(Meta.from(JsonApiMetaObject.builder()
                        .firstPage(0)
                        .lastPage(page.getTotalPages() - 1)
                        .totalPages(page.getTotalPages()
                        )
                        .build()))
                .build();

        return getListAdapter().toJson(doc);
    }

    @Transactional
    public String findById(Long id) {

        Creator found = creatorRepository.findById(id).orElseThrow();

        CreatorDto dto = creatorMapper.toDto(found);

        Document<CreatorDto> doc = Document
                .with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CREATOR_BY_ID, id))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    @Transactional
    public String save(String json) {
        CreatorDto dto = jsonApiValidator.readAndValidate(json, CreatorDto.class);

        Optional<Creator> existing = creatorRepository.findByName(dto.getName());

        if (existing.isPresent()) {
            if (existing.get().getEnabled()) {
                throw new DuplicateElementException("Name already exists");
            }

            existing.get().setEnabled(true);
            Creator saved = creatorRepository.save(existing.get());
            return getSingleAdapter().toJson(Document
                    .with(creatorMapper.toDto(saved))
                    .links(Links.from(JsonApiLinksObject.builder()
                            .self(LinkMapper.toLink(Routes.GET_CREATOR_BY_ID, saved.getId()))
                            .build().toMap()))
                    .build());
        }

        Creator saved = creatorRepository.save(creatorMapper.toEntity(dto));
        return getSingleAdapter().toJson(Document
                .with(creatorMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CREATOR_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public String update(String json) {
        CreatorDto dto = jsonApiValidator.readAndValidate(json, CreatorDto.class);

        Creator existing = creatorRepository.findById(Long.valueOf(dto.getId())).orElseThrow();

        Creator saved = creatorRepository.save(creatorMapper.partialUpdate(dto, existing));
        return getSingleAdapter().toJson(Document
                .with(creatorMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CREATOR_BY_ID, saved.getId()))
                        .build().toMap()))
                .build());
    }

    @Transactional
    public void delete(Long id) {
        creatorRepository.findById(id).ifPresent(e -> {
            List<Book> associated = bookRepository.findByCreators(Collections.singletonList(e));

            if (!associated.isEmpty()) {
                throw new DependencyConflictException("Cannot delete due to existing associations");
            }
            e.setEnabled(false);
            creatorRepository.save(e);
        });
    }

    private JsonAdapter<Document<CreatorDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(CreatorDto.class);
    }

    private JsonAdapter<Document<List<CreatorDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(CreatorDto.class);
    }

    private JsonAdapter<Document<CreatorOwningDto>> getSingleOwningAdapter() {
        return adapterProvider.singleResourceAdapter(CreatorOwningDto.class);
    }

}
