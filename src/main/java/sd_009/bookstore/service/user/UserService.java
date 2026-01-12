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
import sd_009.bookstore.util.mapper.user.RoleMapper;
import sd_009.bookstore.util.mapper.user.UserMapper;
import sd_009.bookstore.util.security.SecurityUtils;
import sd_009.bookstore.util.validation.helper.JsonApiValidator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.ArrayList;
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
    private final RoleMapper roleMapper;
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
    // 🔹 Lấy user hiện tại từ token
    // ===============================================================
    @Transactional(readOnly = true)
    public String findCurrentUser(sd_009.bookstore.util.security.SecurityUtils securityUtils) {
        User user = securityUtils.getCurrentUser();
        UserDto dto = userMapper.toDto(user);

        Document<UserDto> doc = Document.with(dto)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_USER_ME))
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

    // ===============================================================
    // 🔹 Tạo mới user với role (bypass JsonApiValidator)
    // ===============================================================
    @Transactional
    public String saveWithRole(String json) {
        System.out.println("🚀 [CREATE_WITH_ROLE] Starting saveWithRole");
        System.out.println("🚀 [CREATE_WITH_ROLE] Input JSON: " + json);
        
        try {
            // Parse JSON manually (bypass JsonApiValidator completely)
            UserDto dto = parseUserJsonManually(json);
            List<String> roleIds = extractRoleIdsFromJson(json);
            System.out.println("✅ [CREATE_WITH_ROLE] Manual parsing successful");
            System.out.println("✅ [CREATE_WITH_ROLE] Extracted role IDs: " + roleIds);
            
            // Validate email/phone không trùng
            Optional<User> existing = userRepository.findByEmailOrPhoneNumber(dto.getEmail(), dto.getPhoneNumber());
            if (existing.isPresent()) {
                throw new BadRequestException("Email hoặc số điện thoại đã tồn tại");
            }
            
            // Xử lý roles
            List<Role> roles;
            if (roleIds != null && !roleIds.isEmpty()) {
                System.out.println("🔧 [CREATE_WITH_ROLE] Processing role IDs: " + roleIds);
                roles = roleIds.stream()
                        .map(roleIdStr -> {
                            try {
                                Long roleId = Long.valueOf(roleIdStr);
                                Optional<Role> roleOpt = roleRepository.findById(roleId);
                                if (roleOpt.isPresent()) {
                                    System.out.println("✅ Found role: " + roleOpt.get().getName());
                                    return roleOpt;
                                } else {
                                    System.err.println("❌ Role not found: " + roleId);
                                    return Optional.<Role>empty();
                                }
                            } catch (NumberFormatException e) {
                                System.err.println("❌ Invalid role ID: " + roleIdStr);
                                throw new BadRequestException("Role id không hợp lệ: " + roleIdStr);
                            }
                        })
                        .flatMap(Optional::stream)
                        .toList();
                
                if (roles.isEmpty()) {
                    System.err.println("❌ No valid roles found, using default ROLE_USER");
                    roles = List.of(roleRepository.findByNameAndEnabled("ROLE_USER", true)
                            .orElseThrow(() -> new BadRequestException("Role ROLE_USER không tồn tại")));
                }
            } else {
                System.out.println("⚠️ [CREATE_WITH_ROLE] No roles specified, using default ROLE_USER");
                roles = List.of(roleRepository.findByNameAndEnabled("ROLE_USER", true)
                        .orElseThrow(() -> new BadRequestException("Role ROLE_USER không tồn tại")));
            }
            
            // Tạo user entity
            User user = userMapper.toEntity(dto);
            if (user.getId() == null || user.getId() == 0) {
                user.setId(null);
            }
            
            // Set roles
            user.setRoles(roles);
            System.out.println("🎯 [CREATE_WITH_ROLE] Final roles set: " + roles.stream().map(Role::getName).toList());
            
            // Auto-generate username nếu cần
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                if (user.getEmail() == null || user.getEmail().isEmpty()) {
                    user.setUsername("user" + Instant.now().getEpochSecond());
                } else {
                    user.setUsername(user.getEmail().split("@")[0]);
                }
            }
            
            // Set OAuth2 defaults
            user.setIsOauth2User(false);
            user.setOauth2Id(null);
            
            // Encode password
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getUsername()));
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            
            // Save user
            User saved = userRepository.save(user);
            System.out.println("✅ [CREATE_WITH_ROLE] User saved successfully with ID: " + saved.getId());
            
            // Return response
            Document<UserDto> doc = Document.with(userMapper.toDto(saved))
                    .links(Links.from(JsonApiLinksObject.builder()
                            .self(LinkMapper.toLink(Routes.GET_USER_BY_ID, saved.getId()))
                            .build().toMap()))
                    .build();
            
            return getSingleAdapter().toJson(doc);
            
        } catch (Exception e) {
            System.err.println("❌ [CREATE_WITH_ROLE] Error: " + e.getMessage());
            e.printStackTrace();
            throw new BadRequestException("Lỗi tạo user với role: " + e.getMessage());
        }
    }

    @Transactional
    public User buildEntityWithRelationships(String json) {
        System.out.println("🔍 [CREATE] Starting buildEntityWithRelationships");
        System.out.println("🔍 [CREATE] Input JSON: " + json);
        
        // Validate và parse JSON
        UserDto dto;
        List<String> manualRoleIds = null;
        
        try {
            System.out.println("🔍 [CREATE] Attempting JsonApiValidator...");
            dto = jsonApiValidator.readAndValidate(json, UserDto.class);
            System.out.println("✅ [CREATE] JsonApiValidator SUCCESS!");
            System.out.println("✅ [CREATE] DTO roles from validator: " + (dto.getRoles() != null ? dto.getRoles().size() + " roles - " + dto.getRoles() : "NULL"));
        } catch (IllegalArgumentException e) {
            System.err.println("❌ [CREATE] IllegalArgumentException: " + e.getMessage());
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("resource identifier")) {
                // 🔧 FIX: Parse manually khi JsonApiValidator fails với relationships
                System.out.println("⚠️ [CREATE] JsonApiValidator failed with relationships, parsing manually...");
                try {
                    dto = parseUserJsonManually(json);
                    manualRoleIds = extractRoleIdsFromJson(json);
                    System.out.println("✅ [CREATE] Manual parsing successful. Role IDs: " + manualRoleIds);
                } catch (Exception parseEx) {
                    System.err.println("❌ [CREATE] Manual parsing also failed: " + parseEx.getMessage());
                    parseEx.printStackTrace();
                    throw new BadRequestException("JSON không hợp lệ: Không thể parse relationships. " +
                            "Format đúng: {\"type\": \"role\", \"id\": \"1\"}. " +
                            "Nếu không muốn gửi roles, hãy bỏ phần 'relationships' hoàn toàn.");
                }
            } else {
                System.err.println("❌ [CREATE] Other IllegalArgumentException: " + errorMsg);
                throw new BadRequestException("JSON không hợp lệ: " + errorMsg);
            }
        } catch (RuntimeException e) {
            // Catch RuntimeException từ JsonApiValidator
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("resource identifier") || errorMsg.contains("id") || errorMsg.contains("lid"))) {
                // 🔧 FIX: Parse manually khi JsonApiValidator fails với relationships
                System.out.println("⚠️ JsonApiValidator failed with relationships (RuntimeException), parsing manually...");
                try {
                    dto = parseUserJsonManually(json);
                    manualRoleIds = extractRoleIdsFromJson(json);
                    System.out.println("✅ Manual parsing successful. Role IDs: " + manualRoleIds);
                } catch (Exception parseEx) {
                    System.err.println("❌ Manual parsing also failed: " + parseEx.getMessage());
                    throw new BadRequestException("JSON không hợp lệ: Không thể parse relationships. " +
                            "Format đúng: {\"type\": \"role\", \"id\": \"1\"}. " +
                            "Nếu không muốn gửi roles, hãy bỏ phần 'relationships' hoàn toàn.");
                }
            } else {
                throw new BadRequestException("Lỗi parse JSON: " + errorMsg);
            }
        } catch (Exception e) {
            throw new BadRequestException("Lỗi parse JSON: " + e.getMessage());
        }
        
        // Kiểm tra email/phoneNumber đã tồn tại chưa (cho create)
        Optional<User> existing = userRepository.findByEmailOrPhoneNumber(dto.getEmail(), dto.getPhoneNumber());
        if (existing.isPresent()) {
            throw new BadRequestException("Email hoặc số điện thoại đã tồn tại");
        }

        // Xử lý roles: ưu tiên manualRoleIds từ manual parsing, sau đó mới đến dto.getRoles()
        List<RoleDto> roleDtos = dto.getRoles();
        System.out.println("🔍 [CREATE] DTO roles: " + (roleDtos != null ? roleDtos.size() + " roles" : "null"));
        System.out.println("🔍 [CREATE] Manual role IDs: " + manualRoleIds);
        List<Role> roles;
        
        // 🔧 FIX: Sử dụng manualRoleIds nếu có (từ manual parsing)
        if (manualRoleIds != null && !manualRoleIds.isEmpty()) {
            System.out.println("🔧 Using manual role IDs: " + manualRoleIds);
            roles = manualRoleIds.stream()
                    .map(roleIdStr -> {
                        try {
                            Long roleId = Long.valueOf(roleIdStr);
                            return roleRepository.findById(roleId);
                        } catch (NumberFormatException e) {
                            System.err.println("❌ Invalid role ID: " + roleIdStr);
                            throw new BadRequestException("Role id không hợp lệ: " + roleIdStr);
                        }
                    })
                    .flatMap(Optional::stream)
                    .toList();
            
            if (roles.isEmpty()) {
                throw new BadRequestException("Không tìm thấy role nào hợp lệ trong manual parsing");
            }
            System.out.println("✅ Successfully loaded " + roles.size() + " roles from manual parsing");
        } else if (roleDtos != null && !roleDtos.isEmpty()) {
            // Có roles trong DTO (từ JsonApiValidator) -> convert từ RoleDto sang Role entity
            System.out.println("🔧 Using DTO roles: " + roleDtos.size());
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
        } else {
            // Không có roles trong request hoặc manual parsing -> set default role
            System.out.println("🔧 No roles found, using default ROLE_USER");
            // Chỉ lấy role enabled để tránh lỗi khi có nhiều role cùng tên
            roles = List.of(roleRepository.findByNameAndEnabled("ROLE_USER", true)
                    .orElseThrow(
                            () -> new BadRequestException("Role ROLE_USER không tồn tại trong hệ thống")
                    ));
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
    // ===============================================================
// 🔹 Cập nhật user
// ===============================================================
    @Transactional
    public String update(String json, SecurityUtils securityUtils) {
        UserDto dto;
        List<String> manualRoleIds = null;

        try {
            dto = jsonApiValidator.readAndValidate(json, UserDto.class);
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null && e.getMessage().contains("resource identifier")) {
                // 🔧 FIX: Parse manually khi JsonApiValidator fails với relationships  
                System.out.println("⚠️ JsonApiValidator failed with relationships (UPDATE), parsing manually...");
                try {
                    dto = parseUserJsonManually(json);
                    manualRoleIds = extractRoleIdsFromJson(json);
                    System.out.println("✅ Manual parsing successful (UPDATE). Role IDs: " + manualRoleIds);
                } catch (Exception parseEx) {
                    System.err.println("❌ Manual parsing also failed (UPDATE): " + parseEx.getMessage());
                    throw new BadRequestException("JSON không hợp lệ: Không thể parse relationships. " +
                            "Format đúng: {\"type\": \"role\", \"id\": \"1\"}.");
                }
            } else {
                throw new BadRequestException("JSON không hợp lệ: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new BadRequestException("Lỗi parse JSON: " + e.getMessage());
        }

        // ===============================================================
        // 🔹 Lấy userId từ token và user hiện tại
        // ===============================================================
        Long currentUserId = securityUtils.getCurrentUserId();
        User currentUser = securityUtils.getCurrentUser();

        // ===================== DEBUG LOG =====================
        System.out.println("========== UPDATE USER DEBUG ==========");
        System.out.println("dtoId       = " + dto.getId());
        System.out.println("tokenUserId = " + currentUserId);
        System.out.println("======================================");

        // ===============================================================
        // 🔹 Xác định user nào sẽ được update
        // ===============================================================
        Long targetUserId;
        
        if (dto.getId() != null && !dto.getId().equals("0") && !dto.getId().isEmpty()) {
            // Có ID trong DTO - kiểm tra quyền
            targetUserId = Long.valueOf(dto.getId());
            
            // Kiểm tra xem user hiện tại có phải admin/employee không
            boolean isAdminOrEmployee = currentUser.getRoles() != null && 
                    currentUser.getRoles().stream()
                            .anyMatch(role -> role.getName() != null && 
                                    (role.getName().equals("ROLE_ADMIN") || 
                                     role.getName().equals("ROLE_EMPLOYEE") ||
                                     role.getName().equals("ROLE_MANAGER")));
            
            // Nếu không phải admin/employee và đang cố update user khác -> từ chối
            if (!isAdminOrEmployee && !targetUserId.equals(currentUserId)) {
                System.out.println("⚠️ FORBIDDEN UPDATE ATTEMPT ⚠️");
                System.out.println("dtoId       = " + dto.getId());
                System.out.println("tokenUserId = " + currentUserId);
                System.out.println("isAdminOrEmployee = " + isAdminOrEmployee);
                throw new BadRequestException("Không được cập nhật thông tin của user khác");
            }
        } else {
            // Không có ID trong DTO -> update chính mình
            targetUserId = currentUserId;
        }

        // ===============================================================
        // 🔹 Lấy user cần update
        // ===============================================================
        User existing = userRepository.findById(targetUserId)
                .orElseThrow(() -> new BadRequestException("User không tồn tại"));
        
        // Cập nhật userId để dùng cho các phần còn lại của code
        Long userId = targetUserId;

        // ===============================================================
        // 🔹 Check email unique
        // ===============================================================
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {

            System.out.println("Update Email:");
            System.out.println("targetUserId = " + targetUserId);
            System.out.println("oldEmail = " + existing.getEmail());
            System.out.println("newEmail = " + dto.getEmail());

            boolean emailChanged = !dto.getEmail().equals(existing.getEmail());
            if (emailChanged) {
                Optional<User> userWithEmail = userRepository.findByEmail(dto.getEmail());
                if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(targetUserId)) {
                    throw new BadRequestException("Email đã được sử dụng bởi user khác");
                }
            }
        }

        // ===============================================================
        // 🔹 Check phone number unique
        // ===============================================================
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty()) {

            System.out.println("Update Phone:");
            System.out.println("targetUserId = " + targetUserId);
            System.out.println("oldPhone  = " + existing.getPhoneNumber());
            System.out.println("newPhone  = " + dto.getPhoneNumber());

            boolean phoneNumberChanged = existing.getPhoneNumber() == null
                    || !dto.getPhoneNumber().equals(existing.getPhoneNumber());

            if (phoneNumberChanged) {
                Optional<User> userWithPhone = userRepository.findByEmailOrPhoneNumber(
                        "___CHECK_PHONE_NUMBER_UNIQUE___",
                        dto.getPhoneNumber()
                );

                if (userWithPhone.isPresent() && !userWithPhone.get().getId().equals(targetUserId)) {
                    throw new BadRequestException("Số điện thoại đã được sử dụng bởi user khác");
                }
            }
        }

        // ===============================================================
        // 🔹 Xử lý roles (nếu có) - ưu tiên manual parsing
        // ===============================================================
        
        // 🔧 FIX: Ưu tiên manualRoleIds từ manual parsing
        if (manualRoleIds != null && !manualRoleIds.isEmpty()) {
            System.out.println("🔧 Update Roles (Manual):");
            System.out.println("userId = " + userId + ", manualRoleIds = " + manualRoleIds);

            List<Role> roles = manualRoleIds.stream()
                    .map(roleIdStr -> {
                        try {
                            Long roleId = Long.valueOf(roleIdStr);
                            return roleRepository.findById(roleId);
                        } catch (NumberFormatException e) {
                            System.err.println("❌ Invalid manual role ID: " + roleIdStr);
                            throw new BadRequestException("Role id không hợp lệ: " + roleIdStr);
                        }
                    })
                    .flatMap(Optional::stream)
                    .toList();

            if (roles.isEmpty()) {
                throw new BadRequestException("Không tìm thấy role nào hợp lệ trong manual parsing");
            }

            existing.setRoles(roles);
            System.out.println("✅ Successfully updated roles using manual parsing");
        } else if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            // Fallback: Có roles trong DTO (từ JsonApiValidator)
            System.out.println("🔧 Update Roles (DTO):");
            System.out.println(
                    "userId  = " + userId +
                            ", roleIds = " + dto.getRoles()
                            .stream()
                            .map(RoleDto::getId)
                            .toList()
            );

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
            System.out.println("✅ Successfully updated roles using DTO");
        } else {
            System.out.println("ℹ️ No roles in request, keeping existing roles");
        }

        // ===============================================================
        // 🔹 Partial update các field khác
        // ===============================================================
        User updated = userMapper.partialUpdate(dto, existing);

        // ===============================================================
        // 🔹 Update password (nếu có)
        // ===============================================================
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            System.out.println("Update Password for userId = " + userId);
            updated.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // ===============================================================
        // 🔹 Save user
        // ===============================================================
        User saved = userRepository.save(updated);

        System.out.println("✅ USER UPDATED SUCCESSFULLY");
        System.out.println("userId = " + saved.getId());
        System.out.println("email  = " + saved.getEmail());
        System.out.println("phone  = " + saved.getPhoneNumber());

        // ===============================================================
        // 🔹 Build JSON API response
        // ===============================================================
        Document<UserDto> doc = Document.with(userMapper.toDto(saved))
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_USER_BY_ID, saved.getId()))
                        .build().toMap()))
                .build();

        return getSingleAdapter().toJson(doc);
    }

    // ===============================================================
    // 🔹 Cập nhật user với role (bypass JsonApiValidator)
    // ===============================================================
    @Transactional
    public String updateWithRole(String json, SecurityUtils securityUtils) {
        System.out.println("🚀 [UPDATE_WITH_ROLE] Starting updateWithRole");
        System.out.println("🚀 [UPDATE_WITH_ROLE] Input JSON: " + json);
        
        try {
            // Parse JSON manually (bypass JsonApiValidator completely)
            UserDto dto = parseUserJsonManually(json);
            List<String> roleIds = extractRoleIdsFromJson(json);
            System.out.println("✅ [UPDATE_WITH_ROLE] Manual parsing successful");
            System.out.println("✅ [UPDATE_WITH_ROLE] Extracted role IDs: " + roleIds);
            
            // Extract ID từ JSON để xác định user nào sẽ được update
            Long targetUserId;
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(json);
                JsonNode data = root.get("data");
                if (data.has("id") && !data.get("id").asText().equals("0") && !data.get("id").asText().isEmpty()) {
                    targetUserId = Long.valueOf(data.get("id").asText());
                } else {
                    // Không có ID trong JSON -> cần lấy từ token
                    try {
                        targetUserId = securityUtils.getCurrentUserId();
                    } catch (Exception e) {
                        System.err.println("⚠️ [UPDATE_WITH_ROLE] Cannot get currentUserId and no ID in JSON: " + e.getMessage());
                        throw new BadRequestException("Cần có ID trong JSON hoặc đăng nhập để xác định user cần update");
                    }
                }
            } catch (Exception e) {
                // Fallback: thử lấy từ token
                try {
                    targetUserId = securityUtils.getCurrentUserId();
                } catch (Exception ex) {
                    System.err.println("⚠️ [UPDATE_WITH_ROLE] Cannot get currentUserId: " + ex.getMessage());
                    throw new BadRequestException("Không thể xác định user cần update. Vui lòng cung cấp ID trong JSON.");
                }
            }
            
            System.out.println("🔍 [UPDATE_WITH_ROLE] targetUserId: " + targetUserId);
            
            // ===============================================================
            // 🔹 Lấy user cần update
            // ===============================================================
            User existing = userRepository.findById(targetUserId)
                    .orElseThrow(() -> new BadRequestException("User không tồn tại"));
            
            // ===============================================================
            // 🔹 Kiểm tra quyền (chỉ khi có JWT token, OAuth2 sẽ skip)
            // ===============================================================
            Long currentUserId = null;
            try {
                currentUserId = securityUtils.getCurrentUserId();
                System.out.println("🔍 [UPDATE_WITH_ROLE] currentUserId from JWT: " + currentUserId);
                
                // Có JWT token -> check quyền bình thường
                if (!targetUserId.equals(currentUserId)) {
                    // Cần check quyền khi update user khác
                    try {
                        User currentUser = securityUtils.getCurrentUser();
                        boolean isAdminOrEmployee = currentUser.getRoles() != null && 
                                currentUser.getRoles().stream()
                                        .anyMatch(role -> role.getName() != null && 
                                                (role.getName().equals("ROLE_ADMIN") || 
                                                 role.getName().equals("ROLE_EMPLOYEE") ||
                                                 role.getName().equals("ROLE_MANAGER")));
                        
                        if (!isAdminOrEmployee) {
                            throw new BadRequestException("Không được cập nhật thông tin của user khác");
                        }
                    } catch (Exception e) {
                        // Nếu không lấy được currentUser, từ chối
                        throw new BadRequestException("Không được cập nhật thông tin của user khác");
                    }
                }
            } catch (Exception e) {
                // Không có JWT token (OAuth2 hoặc không authenticated) -> skip permission check
                // Trust ID trong JSON (for Swagger UI testing or OAuth2 users)
                System.out.println("⚠️ [UPDATE_WITH_ROLE] Cannot get currentUserId (OAuth2 or not authenticated), skipping permission check");
                System.out.println("⚠️ [UPDATE_WITH_ROLE] Will update user with ID: " + targetUserId);
            }
            
            // ===============================================================
            // 🔹 Validate và check phone number unique (nếu có thay đổi)
            // ===============================================================
            if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty()) {
                boolean phoneNumberChanged = existing.getPhoneNumber() == null
                        || !dto.getPhoneNumber().equals(existing.getPhoneNumber());
                
                if (phoneNumberChanged) {
                    Optional<User> userWithPhone = userRepository.findByEmailOrPhoneNumber(
                            "___CHECK_PHONE_NUMBER_UNIQUE___",
                            dto.getPhoneNumber()
                    );
                    
                    if (userWithPhone.isPresent() && !userWithPhone.get().getId().equals(targetUserId)) {
                        throw new BadRequestException("Số điện thoại đã được sử dụng bởi user khác");
                    }
                }
            }
            
            // ===============================================================
            // 🔹 Xử lý roles TRƯỚC khi partialUpdate (giống method update())
            // ===============================================================
            if (roleIds != null && !roleIds.isEmpty()) {
                System.out.println("🔧 [UPDATE_WITH_ROLE] Processing role IDs: " + roleIds);
                List<Role> roles = roleIds.stream()
                        .map(roleIdStr -> {
                            try {
                                Long roleId = Long.valueOf(roleIdStr);
                                Optional<Role> roleOpt = roleRepository.findById(roleId);
                                if (roleOpt.isPresent()) {
                                    System.out.println("✅ Found role: " + roleOpt.get().getName());
                                    return roleOpt;
                                } else {
                                    System.err.println("❌ Role not found: " + roleId);
                                    return Optional.<Role>empty();
                                }
                            } catch (NumberFormatException e) {
                                System.err.println("❌ Invalid role ID: " + roleIdStr);
                                throw new BadRequestException("Role id không hợp lệ: " + roleIdStr);
                            }
                        })
                        .flatMap(Optional::stream)
                        .toList();
                
                if (roles.isEmpty()) {
                    System.err.println("❌ No valid roles found, keeping existing roles");
                    // Giữ nguyên roles hiện tại
                } else {
                    // ✅ Set roles vào existing TRƯỚC khi partialUpdate (tránh immutable collection error)
                    existing.setRoles(new ArrayList<>(roles)); // Tạo ArrayList mới để tránh immutable
                    System.out.println("🎯 [UPDATE_WITH_ROLE] Final roles set: " + roles.stream().map(Role::getName).toList());
                }
            } else {
                System.out.println("⚠️ [UPDATE_WITH_ROLE] No roles specified, keeping existing roles");
                // Giữ nguyên roles hiện tại
            }
            
            // ===============================================================
            // 🔹 Tạo DTO mới chỉ với các field được phép update (không có email và username)
            // ===============================================================
            UserDto updateDto = new UserDto(
                null, // createdAt
                null, // updatedAt
                dto.getEnabled(),
                dto.getNote(),
                dto.getId(),
                null, // email - không cho phép update
                null, // username - không cho phép update
                dto.getPassword(),
                dto.getPersonName(),
                dto.getPhoneNumber(),
                dto.getAddress(),
                null, // oauth2Id
                null, // isOauth2User
                null  // roles - đã xử lý ở trên
            );
            
            // ===============================================================
            // 🔹 Partial update các field được phép (personName, phoneNumber, address, enabled, note)
            // ===============================================================
            User updated = userMapper.partialUpdate(updateDto, existing);
            
            // ===============================================================
            // 🔹 Đảm bảo email và username không bị thay đổi (giữ nguyên giá trị hiện tại)
            // ===============================================================
            updated.setEmail(existing.getEmail());
            updated.setUsername(existing.getUsername());
            
            // ===============================================================
            // 🔹 Update password (nếu có)
            // ===============================================================
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                System.out.println("🔧 [UPDATE_WITH_ROLE] Updating password");
                updated.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            
            // ===============================================================
            // 🔹 Save user
            // ===============================================================
            User saved = userRepository.save(updated);
            System.out.println("✅ [UPDATE_WITH_ROLE] User updated successfully with ID: " + saved.getId());
            
            // ===============================================================
            // 🔹 Build JSON API response
            // ===============================================================
            Document<UserDto> doc = Document.with(userMapper.toDto(saved))
                    .links(Links.from(JsonApiLinksObject.builder()
                            .self(LinkMapper.toLink(Routes.GET_USER_BY_ID, saved.getId()))
                            .build().toMap()))
                    .build();
            
            return getSingleAdapter().toJson(doc);
            
        } catch (Exception e) {
            System.err.println("❌ [UPDATE_WITH_ROLE] Error: " + e.getMessage());
            e.printStackTrace();
            throw new BadRequestException("Lỗi cập nhật user với role: " + e.getMessage());
        }
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
    // 🔹 Lấy toàn bộ roles
    // ===============================================================
    @Transactional(readOnly = true)
    public String findAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleDto> dtos = roles.stream()
                .map(roleMapper::toDto)
                .toList();

        Document<List<RoleDto>> doc = Document.with(dtos)
                .links(Links.from(JsonApiLinksObject.builder()
                        .self(LinkMapper.toLink(Routes.GET_ROLES))
                        .build().toMap()))
                .build();

        return getRoleListAdapter().toJson(doc);
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

    private JsonAdapter<Document<List<RoleDto>>> getRoleListAdapter() {
        return adapterProvider.listResourceAdapter(RoleDto.class);
    }
    
    // ===============================================================
    // 🔧 MANUAL PARSING METHODS - Fix JsonApiValidator relationships bug
    // ===============================================================
    
    /**
     * Parse UserDto manually khi JsonApiValidator fails với relationships
     */
    private UserDto parseUserJsonManually(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode data = root.get("data");
            JsonNode attributes = data.get("attributes");
            
            // Extract attributes
            String email = attributes.has("email") ? attributes.get("email").asText() : null;
            String username = attributes.has("username") ? attributes.get("username").asText() : null;
            String password = attributes.has("password") ? attributes.get("password").asText() : null;
            String personName = attributes.has("personName") ? attributes.get("personName").asText() : null;
            String phoneNumber = attributes.has("phoneNumber") ? attributes.get("phoneNumber").asText() : null;
            String address = attributes.has("address") ? attributes.get("address").asText() : null;
            Boolean enabled = attributes.has("enabled") ? attributes.get("enabled").asBoolean() : true;
            String note = attributes.has("note") ? attributes.get("note").asText() : null;
            
            // Tạo UserDto với constructor (không có roles - sẽ xử lý riêng)
            return new UserDto(
                null, // createdAt
                null, // updatedAt  
                enabled,
                note,
                null, // id
                email,
                username,
                password,
                personName,
                phoneNumber,
                address,
                null, // oauth2Id
                false, // isOauth2User
                null // roles - sẽ xử lý riêng bằng manualRoleIds
            );
            
        } catch (Exception e) {
            System.err.println("❌ Manual parsing failed: " + e.getMessage());
            e.printStackTrace();
            throw new BadRequestException("Không thể parse JSON manually: " + e.getMessage());
        }
    }
    
    /**
     * Extract role IDs từ relationships trong JSON
     */
    private List<String> extractRoleIdsFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode data = root.get("data");
            
            if (!data.has("relationships")) {
                return new ArrayList<>();
            }
            
            JsonNode relationships = data.get("relationships");
            if (!relationships.has("roles")) {
                return new ArrayList<>();
            }
            
            JsonNode roles = relationships.get("roles");
            if (!roles.has("data")) {
                return new ArrayList<>();
            }
            
            JsonNode rolesData = roles.get("data");
            List<String> roleIds = new ArrayList<>();
            
            if (rolesData.isArray()) {
                for (JsonNode roleNode : rolesData) {
                    if (roleNode.has("id") && roleNode.has("type") && 
                        "role".equals(roleNode.get("type").asText())) {
                        roleIds.add(roleNode.get("id").asText());
                    }
                }
            } else if (rolesData.has("id") && rolesData.has("type") && 
                      "role".equals(rolesData.get("type").asText())) {
                roleIds.add(rolesData.get("id").asText());
            }
            
            System.out.println("🔍 Extracted role IDs from JSON: " + roleIds);
            return roleIds;
            
        } catch (Exception e) {
            System.err.println("❌ Failed to extract role IDs: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
