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
    // 🔹 Lấy toàn bộ giỏ hàng theo user ID (chỉ lấy items enabled = true)
    // ===============================================================
    @Transactional
    public String findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        // Chỉ lấy những cart items có enabled = true (chưa bị xóa)
        List<CartDetail> list = cartDetailRepository.findByUserAndEnabled(user, true);
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
    // 🔹 Lấy chi tiết 1 cart detail theo ID (chỉ lấy nếu enabled = true)
    // ===============================================================
    @Transactional
    public String findById(Long id) {
        CartDetail entity = cartDetailRepository.findById(id).orElseThrow();
        
        // Kiểm tra nếu item đã bị xóa (enabled = false) thì throw exception
        if (Boolean.FALSE.equals(entity.getEnabled())) {
            throw new BadRequestException("Cart detail not found or has been deleted");
        }
        
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
//    @Transactional
//    public String save(String json) {
//        CartDetailDto dto = jsonApiValidator.readAndValidate(json, CartDetailDto.class);
//
//        User user = userRepository.findById(dto.getUser().getId()).orElseThrow();
//        BookDetail bookDetail = bookDetailRepository.findById(dto.getBookDetail().getId()).orElseThrow();
//
//        CartDetail entity = cartDetailMapper.toEntity(dto);
//        entity.setUser(user);
//        entity.setBookDetail(bookDetail);
//
//        CartDetail saved = cartDetailRepository.save(entity);
//
//        Document<CartDetailDto> doc = Document.with(cartDetailMapper.toDto(saved))
//                .links(Links.from(JsonApiLinksObject.builder()
//                        .self(LinkMapper.toLink(Routes.GET_CART_DETAIL_BY_ID, saved.getId()))
//                        .build().toMap()))
//                .build();
//
//        return getSingleAdapter().toJson(doc);
//    }
    @Transactional
    public String saveOnline(String json) {
        CartDetailDto dto = jsonApiValidator.readAndValidate(json, CartDetailDto.class);

        User user = userRepository.findById(Long.valueOf(dto.getUserId()))
                .orElseThrow();

        BookDetail bookDetail = bookDetailRepository.findById(Long.valueOf(dto.getBookDetailId()))
                .orElseThrow();

        // ===============================================================
        // 🔐 KIỂM TRA TỒN KHO THEO GIỎ HÀNG CỦA USER
        // - Mỗi user không được có số dòng cartDetail cho cùng 1 BookDetail
        //   vượt quá stock hiện tại (đáp ứng rule: giỏ không vượt tồn kho)
        // - FE đang tạo từng dòng cartDetail với quantity = 1,
        //   nên có thể xem mỗi dòng = 1 đơn vị sách.
        // ===============================================================
        Long currentStock = bookDetail.getStock() == null ? 0L : bookDetail.getStock();
        if (currentStock <= 0) {
            throw new BadRequestException("Sản phẩm đã hết hàng, không thể thêm vào giỏ.");
        }

        // Lấy tất cả cartDetail đang enabled của user cho cùng BookDetail
        List<CartDetail> existingDetails =
                cartDetailRepository.findByUserAndBookDetailAndEnabled(user, bookDetail, true);

        long currentInCart = existingDetails.size();
        if (currentInCart >= currentStock) {
            // Giỏ của user đã "đụng trần" tồn kho cho cuốn này
            throw new BadRequestException("Số lượng sách trong giỏ đã bằng tồn kho, không thể thêm nữa.");
        }

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
    // 🔹 Cập nhật giỏ hàng (chỉ update nếu enabled = true)
    // ===============================================================
    @Transactional
    public String update(String json) {
        CartDetailDto dto = jsonApiValidator.readAndValidate(json, CartDetailDto.class);
        if (dto.getId() == null) throw new BadRequestException("No identifier found");

        CartDetail existing = cartDetailRepository.findById(Long.valueOf(dto.getId())).orElseThrow();
        
        // Kiểm tra nếu item đã bị xóa (enabled = false) thì không cho update
        if (Boolean.FALSE.equals(existing.getEnabled())) {
            throw new BadRequestException("Cannot update cart detail that has been deleted");
        }
        
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
    // 🔹 Lấy cart detail kèm quan hệ (user + bookDetail) (chỉ lấy nếu enabled = true)
    // ===============================================================
    @Transactional(readOnly = true)
    public String findOwningById(Long id) {
        CartDetail found = cartDetailRepository.findById(id).orElseThrow();
        
        // Kiểm tra nếu item đã bị xóa (enabled = false) thì throw exception
        if (Boolean.FALSE.equals(found.getEnabled())) {
            throw new BadRequestException("Cart detail not found or has been deleted");
        }

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
