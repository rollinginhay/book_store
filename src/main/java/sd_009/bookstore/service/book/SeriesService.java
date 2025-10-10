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
import sd_009.bookstore.dto.jsonApiResource.book.SeriesDto;
import sd_009.bookstore.dto.jsonApiResource.book.SeriesOwningDto;
import sd_009.bookstore.entity.book.Book;
import sd_009.bookstore.entity.book.Series;
import sd_009.bookstore.repository.BookRepository;
import sd_009.bookstore.repository.SeriesRepository;
import sd_009.bookstore.util.mapper.book.SeriesMapper;
import sd_009.bookstore.util.mapper.book.SeriesOwningMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.link.LinkParamMapper;
import sd_009.bookstore.util.spec.Routes;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeriesService {
    private final JsonApiAdapterProvider adapterProvider;
    private final SeriesMapper seriesMapper;
    private final SeriesOwningMapper seriesOwningMapper;
    private final SeriesRepository seriesRepository;
    private final BookRepository bookRepository;

    @Transactional
    public String find(Boolean enabled, String name, Pageable pageable) {

        Page<Series> page;
        if (name == null || name.isEmpty()) {
            page = seriesRepository.findByEnabled(enabled, pageable);
        } else {
            page = seriesRepository.findByNameContainingAndEnabled(name, enabled, pageable);
        }
        List<SeriesDto> dtos = page.getContent().stream().map(seriesMapper::toDto).toList();

        LinkParamMapper<Series> paramMapper = LinkParamMapper.<Series>builder()
                .keyword(name)
                .enabled(enabled)
                .page(page)
                .build();

        Document<List<SeriesDto>> doc = Document
                .with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLinkWithQuery(Routes.GET_SERIES.toString(), paramMapper.getSelfParams()))
                        .first(LinkMapper.toLinkWithQuery(Routes.GET_SERIES.toString(), paramMapper.getFirstParams()))
                        .last(LinkMapper.toLinkWithQuery(Routes.GET_SERIES.toString(), paramMapper.getLastParams()))
                        //has to manually check for null in case of invalid pages
                        .next(paramMapper.getNextParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_SERIES.toString(), paramMapper.getNextParams()))
                        .prev(paramMapper.getPrevParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_SERIES.toString(), paramMapper.getPrevParams()))
                        .build().toMap()))
                .build();
        return getListAdapter().toJson(doc);
    }

    @Transactional
    public String findById(Long id) {

        Series found = seriesRepository.findById(id).orElseThrow();

        SeriesOwningDto dto = seriesOwningMapper.toDto(found);

        Document<SeriesOwningDto> doc = Document
                .with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_SERIES_BY_ID.toString(), id))
                        .build().toMap()))
                .build();

        return getSingleOwningAdapter().toJson(doc);
    }

    @Transactional
    public String save(SeriesDto SeriesDto) {
        Series Series = seriesMapper.toEntity(SeriesDto);

        Optional<Series> existing = seriesRepository.findByName(Series.getName());

        if (existing.isPresent()) {
            if (existing.get().getEnabled()) {
                throw new DuplicateElementException("Name already exists");
            }

            throw new IsDisabledException("Series is disabled. Can be reinstated");
        }

        return getSingleAdapter().toJson(Document.with(seriesMapper.toDto(seriesRepository.save(Series))).build());
    }

    @Transactional
    public String update(SeriesDto SeriesDto) {
        Series Series = seriesMapper.toEntity(SeriesDto);
        if (Series.getId() == null) {
            throw new BadRequestException("No identifier found");
        }
        return getSingleAdapter().toJson(Document.with(seriesMapper.toDto(seriesRepository.save(Series))).build());
    }

    @Transactional
    public void delete(Long id) {
        seriesRepository.findById(id).ifPresent(e -> {
            List<Book> associated = bookRepository.findBySeries(e);

            if (!associated.isEmpty()) {
                throw new DependencyConflictException("Cannot delete due to existing associations");
            }
            e.setEnabled(false);
            seriesRepository.save(e);
        });

    }

    private JsonAdapter<Document<SeriesDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(SeriesDto.class);
    }

    private JsonAdapter<Document<List<SeriesDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(SeriesDto.class);
    }


    private JsonAdapter<Document<SeriesOwningDto>> getSingleOwningAdapter() {
        return adapterProvider.singleResourceAdapter(SeriesOwningDto.class);
    }

}
