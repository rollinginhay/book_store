package sd_009.bookstore.service.cart;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.cart.CartDetailDto;
import sd_009.bookstore.dto.jsonApiResource.cart.CartDetailOwningDto;
import sd_009.bookstore.entity.book.BookDetail;
import sd_009.bookstore.entity.cart.CartDetail;
import sd_009.bookstore.entity.user.User;
import sd_009.bookstore.repository.BookDetailRepository;
import sd_009.bookstore.repository.CartDetailRepository;
import sd_009.bookstore.repository.UserRepository;
import sd_009.bookstore.util.mapper.cart.CartDetailMapper;
import sd_009.bookstore.util.mapper.cart.CartDetailOwningMapper;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartDetailService {

    private final JsonApiAdapterProvider adapterProvider;
    private final JsonApiValidator jsonApiValidator;

    private final CartDetailRepository cartDetailRepository;
    private final BookDetailRepository bookDetailRepository;
    private final UserRepository userRepository;

    private final CartDetailMapper cartDetailMapper;
    private final CartDetailOwningMapper cartDetailOwningMapper;


    // ===============================================================
    // 🔹 Lấy toàn bộ giỏ hàng theo user ID
    // ===============================================================
    @Transactional
    public String findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        List<CartDetail> list = cartDetailRepository.findByUser(user);
        List<CartDetailDto> dtos = list.stream()
                .map(cartDetailMapper::toDto)
                .toList();

        Document<List<CartDetailDto>> doc = Document.with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.MULTI_USER_RELATIONSHIP_CART_DETAIL, userId))
                        .build().toMap()))
                .build();

        return getListAdapter().toJson(doc);
    }

    // ===============================================================
    // 🔹 Lấy chi tiết 1 cart detail theo ID
    // ===============================================================
    @Transactional
    public String findById(Long id) {
        CartDetail entity = cartDetailRepository.findById(id).orElseThrow();
        CartDetailDto dto = cartDetailMapper.toDto(entity);

        Document<CartDetailDto> doc = Document.with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CART_DETAIL_BY_ID, id))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // ===============================================================
    // 🔹 Tạo mới 1 cart detail
    // ===============================================================
    @Transactional
    public String save(String json) {
        CartDetailDto dto = jsonApiValidator.readAndValidate(json, CartDetailDto.class);

        User user = userRepository.findById(Long.valueOf(dto.getUserId()))
                .orElseThrow();

        BookDetail bookDetail = bookDetailRepository.findById(Long.valueOf(dto.getBookDetailId()))
                .orElseThrow();

        CartDetail entity = cartDetailMapper.toEntity(dto);
        entity.setUser(user);
        entity.setBookDetail(bookDetail);


        CartDetail saved = cartDetailRepository.save(entity);

        Document<CartDetailDto> doc = Document.with(cartDetailMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CART_DETAIL_BY_ID, saved.getId()))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // ===============================================================
    // 🔹 Cập nhật giỏ hàng
    // ===============================================================
    @Transactional
    public String update(String json) {
        CartDetailDto dto = jsonApiValidator.readAndValidate(json, CartDetailDto.class);
        if (dto.getId() == null) throw new BadRequestException("No identifier found");

        CartDetail existing = cartDetailRepository.findById(Long.valueOf(dto.getId())).orElseThrow();
        CartDetail updated = cartDetailRepository.save(cartDetailMapper.partialUpdate(dto, existing));

        Document<CartDetailDto> doc = Document.with(cartDetailMapper.toDto(updated))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_CART_DETAIL_BY_ID, updated.getId()))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // ===============================================================
    // 🔹 Xóa mềm cart detail
    // ===============================================================
    @Transactional
    public void delete(Long id) {
        cartDetailRepository.findById(id).ifPresent(e -> {
            e.setEnabled(false);
            cartDetailRepository.save(e);
        });
    }

    // ===============================================================
    // 🔹 Lấy cart detail kèm quan hệ (user + bookDetail)
    // ===============================================================
    @Transactional(readOnly = true)
    public String findOwningById(Long id) {
        CartDetail found = cartDetailRepository.findById(id).orElseThrow();

        // mapper: entity → owning DTO
        CartDetailOwningDto dto = cartDetailOwningMapper.toDto(found);

        // build JSON:API document
        Document<CartDetailOwningDto> doc = Document
                .with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink("/v1/cartDetail/" + id + "/owning"))
                        .build().toMap()))
                .build();

        // serialize to JSON string
        return getSingleOwningAdapter().toJson(doc);
    }

    // ===============================================================
    // 🔹 Adapter hỗ trợ Moshi (JsonAPI)
    // ===============================================================
    private JsonAdapter<Document<CartDetailDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(CartDetailDto.class);
    }

    private JsonAdapter<Document<List<CartDetailDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(CartDetailDto.class);
    }

    private JsonAdapter<Document<CartDetailOwningDto>> getSingleOwningAdapter() {
        return adapterProvider.singleResourceAdapter(CartDetailOwningDto.class);
    }

    private JsonAdapter<Document<List<CartDetailOwningDto>>> getListOwningAdapter() {
        return adapterProvider.listResourceAdapter(CartDetailOwningDto.class);
    }
}
