package sd_009.bookstore.service.user;

import com.squareup.moshi.JsonAdapter;
import jsonapi.Document;
import jsonapi.Links;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.jsonapi.JsonApiAdapterProvider;
import sd_009.bookstore.config.spec.Routes;
import sd_009.bookstore.dto.internal.JsonApiLinksObject;
import sd_009.bookstore.dto.jsonApiResource.user.RoleDto;
import sd_009.bookstore.dto.jsonApiResource.user.UserDto;
import sd_009.bookstore.entity.user.Role;
import sd_009.bookstore.entity.user.User;
import sd_009.bookstore.repository.RoleRepository;
import sd_009.bookstore.repository.UserRepository;
import sd_009.bookstore.util.mapper.link.LinkMapper;
import sd_009.bookstore.util.mapper.user.UserMapper;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JsonApiAdapterProvider adapterProvider;
    private final JsonApiValidator jsonApiValidator;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // ===============================================================
    // 🔹 Lấy toàn bộ user
    // ===============================================================
    @Transactional(readOnly = true)
    public String findAll() {
        List<User> users = userRepository.findAll(Sort.by("updatedAt").descending());
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
        User user = buildEntityWithRelationships(json);

        User saved = userRepository.save(user);

        Document<UserDto> doc = Document.with(userMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_USER_BY_ID, saved.getId()))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    @Transactional
    public User buildEntityWithRelationships(String json) {
        // Validate và parse JSON
        UserDto dto;
        try {
            dto = jsonApiValidator.readAndValidate(json, UserDto.class);
        } catch (IllegalArgumentException e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("resource identifier")) {
                throw new BadRequestException("JSON không hợp lệ: Mỗi role trong relationships.roles.data phải có cả 'type' và 'id'. " +
                        "Format đúng: {\"type\": \"role\", \"id\": \"1\"}. " +
                        "Nếu không muốn gửi roles, hãy bỏ phần 'relationships' hoàn toàn.");
            }
            throw new BadRequestException("JSON không hợp lệ: " + errorMsg);
        } catch (RuntimeException e) {
            // Catch RuntimeException từ JsonApiValidator
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("resource identifier") || errorMsg.contains("id") || errorMsg.contains("lid"))) {
                throw new BadRequestException("JSON không hợp lệ: Mỗi role trong relationships.roles.data phải có cả 'type' và 'id'. " +
                        "Format đúng: {\"type\": \"role\", \"id\": \"1\"}. " +
                        "Nếu không muốn gửi roles, hãy bỏ phần 'relationships' hoàn toàn.");
            }
            throw new BadRequestException("Lỗi parse JSON: " + errorMsg);
        } catch (Exception e) {
            throw new BadRequestException("Lỗi parse JSON: " + e.getMessage());
        }
        
        // Kiểm tra email/phoneNumber đã tồn tại chưa (cho create)
        Optional<User> existing = userRepository.findByEmailOrPhoneNumber(dto.getEmail(), dto.getPhoneNumber());
        if (existing.isPresent()) {
            throw new BadRequestException("Email hoặc số điện thoại đã tồn tại");
        }

        // Xử lý roles: nếu có trong request thì convert, không thì set default
        List<RoleDto> roleDtos = dto.getRoles();
        List<Role> roles;
        if (roleDtos == null || roleDtos.isEmpty()) {
            // Không có roles trong request -> set default role
            roles = List.of(roleRepository.findByName("ROLE_USER").orElseThrow(
                    () -> new BadRequestException("Role ROLE_USER không tồn tại trong hệ thống")
            ));
        } else {
            // Có roles trong request -> convert từ RoleDto sang Role entity
            // Validate: tất cả roles phải có id hợp lệ
            for (RoleDto roleDto : roleDtos) {
                if (roleDto.getId() == null || roleDto.getId().isEmpty()) {
                    throw new BadRequestException("Role phải có id hợp lệ");
                }
            }
            
            roles = roleDtos.stream()
                    .map(roleDto -> {
                        try {
                            return roleRepository.findById(Long.valueOf(roleDto.getId()));
                        } catch (NumberFormatException e) {
                            throw new BadRequestException("Role id không hợp lệ: " + roleDto.getId());
                        }
                    })
                    .flatMap(Optional::stream)
                    .toList();
            
            if (roles.isEmpty()) {
                throw new BadRequestException("Không tìm thấy role nào hợp lệ");
            }
        }

        // Map DTO sang Entity
        User user = userMapper.toEntity(dto);
        if (user.getId() == null || user.getId() == 0) {
            user.setId(null);
        }
        
        // Set roles
        user.setRoles(roles);
        
        // Auto-generate username nếu không có
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                user.setUsername("user" + Instant.now().getEpochSecond());
            } else {
                user.setUsername(user.getEmail().split("@")[0]);
            }
        }

        // Set các giá trị mặc định cho OAuth2
        user.setIsOauth2User(false);
        user.setOauth2Id(null);
        
        // Encode password: nếu không có password thì dùng username làm password mặc định
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getUsername()));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return user;
    }

    // ===============================================================
    // 🔹 Cập nhật user
    // ===============================================================
    @Transactional
    public String update(String json) {
        UserDto dto;
        try {
            dto = jsonApiValidator.readAndValidate(json, UserDto.class);
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null && e.getMessage().contains("resource identifier")) {
                throw new BadRequestException("JSON không hợp lệ: Mỗi role trong relationships phải có cả 'type' và 'id'. Vui lòng kiểm tra lại format JSON request.");
            }
            throw new BadRequestException("JSON không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Lỗi parse JSON: " + e.getMessage());
        }
        
        // Validate ID
        if (dto.getId() == null || dto.getId().equals("0")) {
            throw new BadRequestException("ID user không hợp lệ");
        }

        Long userId = Long.valueOf(dto.getId());
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User không tồn tại"));

        // Kiểm tra email unique (không được trùng với user khác, nhưng có thể giữ nguyên email của chính user đó)
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            // Chỉ check duplicate nếu email thay đổi
            boolean emailChanged = !dto.getEmail().equals(existing.getEmail());
            if (emailChanged) {
                Optional<User> userWithEmail = userRepository.findByEmail(dto.getEmail());
                if (userWithEmail.isPresent()) {
                    throw new BadRequestException("Email đã được sử dụng bởi user khác");
                }
            }
        }
        
        // Kiểm tra phoneNumber unique (không được trùng với user khác, nhưng có thể giữ nguyên phoneNumber của chính user đó)
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty()) {
            // Chỉ check duplicate nếu phoneNumber thay đổi
            boolean phoneNumberChanged = existing.getPhoneNumber() == null || 
                                        !dto.getPhoneNumber().equals(existing.getPhoneNumber());
            if (phoneNumberChanged) {
                // Query để tìm user có phoneNumber này
                // Dùng email không bao giờ tồn tại để trigger OR query, sẽ chỉ match phoneNumber
                Optional<User> userWithPhone = userRepository.findByEmailOrPhoneNumber(
                        "___CHECK_PHONE_NUMBER_UNIQUE___", 
                        dto.getPhoneNumber()
                );
                if (userWithPhone.isPresent() && !userWithPhone.get().getId().equals(userId)) {
                    throw new BadRequestException("Số điện thoại đã được sử dụng bởi user khác");
                }
            }
        }

        // Xử lý roles: nếu có trong request thì update, không thì giữ nguyên
        // Set roles trước partialUpdate vì MapStruct không map relationships tự động
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            // Validate: tất cả roles phải có id hợp lệ
            for (RoleDto roleDto : dto.getRoles()) {
                if (roleDto.getId() == null || roleDto.getId().isEmpty()) {
                    throw new BadRequestException("Role phải có id hợp lệ");
                }
            }
            
            List<Role> roles = dto.getRoles().stream()
                    .map(roleDto -> {
                        try {
                            return roleRepository.findById(Long.valueOf(roleDto.getId()));
                        } catch (NumberFormatException e) {
                            throw new BadRequestException("Role id không hợp lệ: " + roleDto.getId());
                        }
                    })
                    .flatMap(Optional::stream)
                    .toList();
            
            if (roles.isEmpty()) {
                throw new BadRequestException("Không tìm thấy role nào hợp lệ");
            }
            
            existing.setRoles(roles);
        }
        // Nếu roles không có trong request, giữ nguyên roles hiện tại

        // Partial update các trường khác (roles và password sẽ được xử lý riêng)
        // createdAt sẽ không bị update vì được set @CreatedDate trong AuditableEntity
        User updated = userMapper.partialUpdate(dto, existing);
        
        // Xử lý password: chỉ update nếu có trong request
        // PartialUpdate có thể set password từ DTO (plain text), cần encode lại
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            updated.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        // Nếu password không có trong request, giữ nguyên password hiện tại (đã được giữ bởi partialUpdate)
        
        // Lưu user
        User saved = userRepository.save(updated);

        Document<UserDto> doc = Document.with(userMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_USER_BY_ID, saved.getId()))
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
