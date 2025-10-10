package sd_009.bookstore.service.book;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.exceptionHanding.exception.DependencyConflictException;
import sd_009.bookstore.config.exceptionHanding.exception.DuplicateElementException;
import sd_009.bookstore.config.exceptionHanding.exception.IsDisabledException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.book.PublisherDto;
import sd_009.bookstore.dto.jsonApiResource.book.PublisherOwningDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Publisher;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.repository.PublisherRepository;
import sd_009.bookstore.util.mapper.book.BookMapper;
import sd_009.bookstore.util.mapper.book.PublisherMapper;
import sd_009.bookstore.util.mapper.book.PublisherOwningMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.link.LinkParamMapper;
import sd_009.bookstore.util.spec.Routes;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublisherService {
    private final JsonApiAdapterProvider adapterProvider;
    private final PublisherMapper publisherMapper;
    private final PublisherOwningMapper publisherOwningMapper;
    private final BookMapper bookMapper;
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    @Transactional
    public String find(Boolean enabled, String name, Pageable pageable) {

        Page<Publisher> page;
        if (name == null || name.isEmpty()) {
            page = publisherRepository.findByEnabled(enabled, pageable);
        } else {
            page = publisherRepository.findByNameContainingAndEnabled(name, enabled, pageable);
        }
        List<PublisherDto> dtos = page.getContent().stream().map(publisherMapper::toDto).toList();

        LinkParamMapper<Publisher> paramMapper = LinkParamMapper.<Publisher>builder()
                .keyword(name)
                .enabled(enabled)
                .page(page)
                .build();

        Document<List<PublisherDto>> doc = Document
                .with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLinkWithQuery(Routes.GET_PUBLISHERS.toString(), paramMapper.getSelfParams()))
                        .first(LinkMapper.toLinkWithQuery(Routes.GET_PUBLISHERS.toString(), paramMapper.getFirstParams()))
                        .last(LinkMapper.toLinkWithQuery(Routes.GET_PUBLISHERS.toString(), paramMapper.getLastParams()))
                        //has to manually check for null in case of invalid pages
                        .next(paramMapper.getNextParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_PUBLISHERS.toString(), paramMapper.getNextParams()))
                        .prev(paramMapper.getPrevParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_PUBLISHERS.toString(), paramMapper.getPrevParams()))
                        .build().toMap()))
                .build();
        return getListAdapter().toJson(doc);
    }

    @Transactional
    public String findById(Long id) {

        Publisher found = publisherRepository.findById(id).orElseThrow();

        PublisherOwningDto dto = publisherOwningMapper.toDto(found);

        Document<PublisherOwningDto> doc = Document
                .with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_PUBLISHER_BY_ID.toString(), id))
                        .build().toMap()))
                .build();

        return getSingleOwningAdapter().toJson(doc);
    }

    @Transactional
    public String save(PublisherDto publisherDto) {
        Publisher publisher = publisherMapper.toEntity(publisherDto);

        Optional<Publisher> existing = publisherRepository.findByName(publisher.getName());

        if (existing.isPresent()) {
            if (existing.get().getEnabled()) {
                throw new DuplicateElementException("Name already exists");
            }

            throw new IsDisabledException("Publisher is disabled. Can be reinstated");
        }

        return getSingleAdapter().toJson(Document.with(publisherMapper.toDto(publisherRepository.save(publisher))).build());
    }

    @Transactional
    public String update(PublisherDto publisherDto) {
        Publisher publisher = publisherMapper.toEntity(publisherDto);
        if (publisher.getId() == null) {
            throw new BadRequestException("No identifier found");
        }
        return getSingleAdapter().toJson(Document.with(publisherMapper.toDto(publisherRepository.save(publisher))).build());
    }

    @Transactional
    public void delete(Long id) {
        publisherRepository.findById(id).ifPresent(e -> {
            List<Book> associated = bookRepository.findByEnabledAndPublisher(true, e);

            if (!associated.isEmpty()) {
                throw new DependencyConflictException("Cannot delete due to existing associations");
            }
            e.setEnabled(false);
            publisherRepository.save(e);
        });

    }

    private JsonAdapter<Document<PublisherDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(PublisherDto.class);
    }

    private JsonAdapter<Document<List<PublisherDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(PublisherDto.class);
    }

    private JsonAdapter<Document<PublisherOwningDto>> getSingleOwningAdapter() {
        return adapterProvider.singleResourceAdapter(PublisherOwningDto.class);
    }

}
