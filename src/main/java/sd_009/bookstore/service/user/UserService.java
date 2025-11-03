package sd_009.bookstore.service.user;

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
import sd_009.bookstore.dto.jsonApiResource.user.UserDto;
import sd_009.bookstore.entity.user.User;
import sd_009.bookstore.repository.UserRepository;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.user.UserMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JsonApiAdapterProvider adapterProvider;
    private final JsonApiValidator jsonApiValidator;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // ===============================================================
    // 🔹 Lấy toàn bộ user
    // ===============================================================
    @Transactional(readOnly = true)
    public String findAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtos = users.stream()
                .map(userMapper::toDto)
                .toList();

        Document<List<UserDto>> doc = Document.with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_USERS))
                        .build().toMap()))
                .build();

        return getListAdapter().toJson(doc);
    }

    // ===============================================================
    // 🔹 Lấy user theo ID
    // ===============================================================
    @Transactional(readOnly = true)
    public String findById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        UserDto dto = userMapper.toDto(user);

        Document<UserDto> doc = Document.with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_USER_BY_ID, id))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // ===============================================================
    // 🔹 Tạo mới user
    // ===============================================================
    @Transactional
    public String save(String json) {
        UserDto dto = jsonApiValidator.readAndValidate(json, UserDto.class);
        User entity = userMapper.toEntity(dto);
        User saved = userRepository.save(entity);

        Document<UserDto> doc = Document.with(userMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_USER_BY_ID, saved.getId()))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // ===============================================================
    // 🔹 Cập nhật user
    // ===============================================================
    @Transactional
    public String update(String json) {
        UserDto dto = jsonApiValidator.readAndValidate(json, UserDto.class);
        if (dto.getId() == null) throw new BadRequestException("No identifier found");

        User existing = userRepository.findById(Long.valueOf(dto.getId())).orElseThrow();
        User updated = userRepository.save(userMapper.partialUpdate(dto, existing));

        Document<UserDto> doc = Document.with(userMapper.toDto(updated))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_USER_BY_ID, updated.getId()))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // ===============================================================
    // 🔹 Xóa mềm user
    // ===============================================================
    @Transactional
    public void delete(Long id) {
        userRepository.findById(id).ifPresent(u -> {
            u.setEnabled(false);
            userRepository.save(u);
        });
    }

    // ===============================================================
    // 🔹 Adapter Moshi
    // ===============================================================
    private JsonAdapter<Document<UserDto>> getSingleAdapter() {
        return adapterProvider.singleResourceAdapter(UserDto.class);
    }

    private JsonAdapter<Document<List<UserDto>>> getListAdapter() {
        return adapterProvider.listResourceAdapter(UserDto.class);
    }
}
