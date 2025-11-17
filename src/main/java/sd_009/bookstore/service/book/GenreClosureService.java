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
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.internal.JsonApiMetaObject;
import sd_009.bookstore.dto.jsonApiResource.book.GenreClosureDto;
import sd_009.bookstore.entity.book.GenreClosure;
import sd_009.bookstore.repository.GenreClosureRepository;
import sd_009.bookstore.util.mapper.book.GenreClosureMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.link.LinkParamMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreClosureService {

    private final GenreClosureRepository genreClosureRepository;
    private final GenreClosureMapper genreClosureMapper;
    private final JsonApiAdapterProvider adapterProvider;

    @Transactional(readOnly = true)
    public String findAll(Pageable pageable) {
        Page<GenreClosure> page = genreClosureRepository.findAll(pageable);
        List<GenreClosureDto> dtos = page.getContent().stream()
                .map(genreClosureMapper::toDto)
                .toList();

        LinkParamMapper<GenreClosure> paramMapper = LinkParamMapper.<GenreClosure>builder()
                .page(page)
                .build();

        Document<List<GenreClosureDto>> doc = Document
                .with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLinkWithQuery(Routes.GET_GENRE_CLOSURES, paramMapper.getSelfParams()))
                        .first(LinkMapper.toLinkWithQuery(Routes.GET_GENRE_CLOSURES, paramMapper.getFirstParams()))
                        .last(LinkMapper.toLinkWithQuery(Routes.GET_GENRE_CLOSURES, paramMapper.getLastParams()))
                        .next(paramMapper.getNextParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_GENRE_CLOSURES, paramMapper.getNextParams()))
                        .prev(paramMapper.getPrevParams() == null ? null : LinkMapper.toLinkWithQuery(Routes.GET_GENRE_CLOSURES, paramMapper.getPrevParams()))
                        .build().toMap()))
                .meta(Meta.from(JsonApiMetaObject.builder()
                        .firstPage(0)
                        .lastPage(page.getTotalPages() - 1)
                        .totalPages(page.getTotalPages())
                        .build()))
                .build();

        return getListAdapter().toJson(doc);
    }

    @Transactional(readOnly = true)
    public String findDescendants(Long ancestorId) {
        List<Long> ids = genreClosureRepository.findAllDescendantIds(ancestorId);
        return "{\"ancestorId\": " + ancestorId + ", \"descendantIds\": " + ids + "}";
    }

    @Transactional(readOnly = true)
    public String findAncestors(Long descendantId) {
        List<Long> ids = genreClosureRepository.findAllAncestorIds(descendantId);
        return "{\"descendantId\": " + descendantId + ", \"ancestorIds\": " + ids + "}";
    }

    private JsonAdapter<Document<List<GenreClosureDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(GenreClosureDto.class);
    }

}
