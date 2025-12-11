package sd_009.bookstore.util.mapper.user;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.user.RoleDto;
import sd_009.bookstore.entity.user.Role;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Role toEntity(RoleDto roleDto) {
        if ( roleDto == null ) {
            return null;
        }

        Role.RoleBuilder role = Role.builder();

        if ( roleDto.getId() != null ) {
            role.id( Long.parseLong( roleDto.getId() ) );
        }
        role.name( roleDto.getName() );

        return role.build();
    }

    @Override
    public RoleDto toDto(Role role) {
        if ( role == null ) {
            return null;
        }

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;
        String id = null;
        String name = null;

        createdAt = role.getCreatedAt();
        updatedAt = role.getUpdatedAt();
        enabled = role.getEnabled();
        note = role.getNote();
        if ( role.getId() != null ) {
            id = String.valueOf( role.getId() );
        }
        name = role.getName();

        RoleDto roleDto = new RoleDto( createdAt, updatedAt, enabled, note, id, name );

        return roleDto;
    }

    @Override
    public Role partialUpdate(RoleDto roleDto, Role role) {
        if ( roleDto == null ) {
            return role;
        }

        if ( roleDto.getCreatedAt() != null ) {
            role.setCreatedAt( roleDto.getCreatedAt() );
        }
        if ( roleDto.getUpdatedAt() != null ) {
            role.setUpdatedAt( roleDto.getUpdatedAt() );
        }
        if ( roleDto.getEnabled() != null ) {
            role.setEnabled( roleDto.getEnabled() );
        }
        if ( roleDto.getNote() != null ) {
            role.setNote( roleDto.getNote() );
        }
        if ( roleDto.getId() != null ) {
            role.setId( Long.parseLong( roleDto.getId() ) );
        }
        if ( roleDto.getName() != null ) {
            role.setName( roleDto.getName() );
        }

        return role;
    }
}
