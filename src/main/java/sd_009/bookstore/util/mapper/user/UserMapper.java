package sd_009.bookstore.util.mapper.user;

import org.mapstruct.*;
import sd_009.bookstore.entity.user.User;
import sd_009.bookstore.dto.jsonApiResource.user.UserDto;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {RoleMapper.class}
)
public interface UserMapper {

    // Khi map từ DTO -> Entity (tạo mới / cập nhật)
    User toEntity(UserDto userDto);

    // Khi map từ Entity -> DTO (trả về client)
    UserDto toDto(User user);

    // Partial update (update từng phần, không override null field)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);
}
