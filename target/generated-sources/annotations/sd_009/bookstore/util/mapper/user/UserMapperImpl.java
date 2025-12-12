package sd_009.bookstore.util.mapper.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.user.RoleDto;
import sd_009.bookstore.dto.jsonApiResource.user.UserDto;
import sd_009.bookstore.entity.user.Role;
import sd_009.bookstore.entity.user.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:04+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public User toEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        if ( userDto.getId() != null ) {
            user.id( Long.parseLong( userDto.getId() ) );
        }
        user.email( userDto.getEmail() );
        user.password( userDto.getPassword() );
        user.username( userDto.getUsername() );
        user.personName( userDto.getPersonName() );
        user.phoneNumber( userDto.getPhoneNumber() );
        user.address( userDto.getAddress() );
        user.oauth2Id( userDto.getOauth2Id() );
        user.isOauth2User( userDto.getIsOauth2User() );
        user.roles( roleDtoListToRoleList( userDto.getRoles() ) );

        return user.build();
    }

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        List<RoleDto> roles = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Boolean enabled = null;
        String note = null;
        String id = null;
        String email = null;
        String username = null;
        String password = null;
        String personName = null;
        String phoneNumber = null;
        String address = null;
        String oauth2Id = null;
        Boolean isOauth2User = null;

        roles = roleListToRoleDtoList( user.getRoles() );
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
        enabled = user.getEnabled();
        note = user.getNote();
        if ( user.getId() != null ) {
            id = String.valueOf( user.getId() );
        }
        email = user.getEmail();
        username = user.getUsername();
        password = user.getPassword();
        personName = user.getPersonName();
        phoneNumber = user.getPhoneNumber();
        address = user.getAddress();
        oauth2Id = user.getOauth2Id();
        isOauth2User = user.getIsOauth2User();

        UserDto userDto = new UserDto( createdAt, updatedAt, enabled, note, id, email, username, password, personName, phoneNumber, address, oauth2Id, isOauth2User, roles );

        return userDto;
    }

    @Override
    public User partialUpdate(UserDto userDto, User user) {
        if ( userDto == null ) {
            return user;
        }

        if ( userDto.getCreatedAt() != null ) {
            user.setCreatedAt( userDto.getCreatedAt() );
        }
        if ( userDto.getUpdatedAt() != null ) {
            user.setUpdatedAt( userDto.getUpdatedAt() );
        }
        if ( userDto.getEnabled() != null ) {
            user.setEnabled( userDto.getEnabled() );
        }
        if ( userDto.getNote() != null ) {
            user.setNote( userDto.getNote() );
        }
        if ( userDto.getId() != null ) {
            user.setId( Long.parseLong( userDto.getId() ) );
        }
        if ( userDto.getEmail() != null ) {
            user.setEmail( userDto.getEmail() );
        }
        if ( userDto.getPassword() != null ) {
            user.setPassword( userDto.getPassword() );
        }
        if ( userDto.getUsername() != null ) {
            user.setUsername( userDto.getUsername() );
        }
        if ( userDto.getPersonName() != null ) {
            user.setPersonName( userDto.getPersonName() );
        }
        if ( userDto.getPhoneNumber() != null ) {
            user.setPhoneNumber( userDto.getPhoneNumber() );
        }
        if ( userDto.getAddress() != null ) {
            user.setAddress( userDto.getAddress() );
        }
        if ( userDto.getOauth2Id() != null ) {
            user.setOauth2Id( userDto.getOauth2Id() );
        }
        if ( userDto.getIsOauth2User() != null ) {
            user.setIsOauth2User( userDto.getIsOauth2User() );
        }
        if ( user.getRoles() != null ) {
            List<Role> list = roleDtoListToRoleList( userDto.getRoles() );
            if ( list != null ) {
                user.getRoles().clear();
                user.getRoles().addAll( list );
            }
        }
        else {
            List<Role> list = roleDtoListToRoleList( userDto.getRoles() );
            if ( list != null ) {
                user.setRoles( list );
            }
        }

        return user;
    }

    protected List<Role> roleDtoListToRoleList(List<RoleDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Role> list1 = new ArrayList<Role>( list.size() );
        for ( RoleDto roleDto : list ) {
            list1.add( roleMapper.toEntity( roleDto ) );
        }

        return list1;
    }

    protected List<RoleDto> roleListToRoleDtoList(List<Role> list) {
        if ( list == null ) {
            return null;
        }

        List<RoleDto> list1 = new ArrayList<RoleDto>( list.size() );
        for ( Role role : list ) {
            list1.add( roleMapper.toDto( role ) );
        }

        return list1;
    }
}
