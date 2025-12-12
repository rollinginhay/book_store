package sd_009.bookstore.util.mapper.book;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.CreatorDto;
import sd_009.bookstore.entity.book.Creator;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class CreatorMapperImpl implements CreatorMapper {

    @Override
    public Creator toEntity(CreatorDto creatorDto) {
        if ( creatorDto == null ) {
            return null;
        }

        Creator.CreatorBuilder creator = Creator.builder();

        if ( creatorDto.getId() != null ) {
            creator.id( Long.parseLong( creatorDto.getId() ) );
        }
        creator.name( creatorDto.getName() );
        creator.role( creatorDto.getRole() );

        return creator.build();
    }

    @Override
    public CreatorDto toDto(Creator creator) {
        if ( creator == null ) {
            return null;
        }

        CreatorDto.CreatorDtoBuilder creatorDto = CreatorDto.builder();

        creatorDto.createdAt( creator.getCreatedAt() );
        creatorDto.updatedAt( creator.getUpdatedAt() );
        creatorDto.enabled( creator.getEnabled() );
        creatorDto.note( creator.getNote() );
        if ( creator.getId() != null ) {
            creatorDto.id( String.valueOf( creator.getId() ) );
        }
        creatorDto.name( creator.getName() );
        creatorDto.role( creator.getRole() );

        return creatorDto.build();
    }

    @Override
    public Creator partialUpdate(CreatorDto creatorDto, Creator creator) {
        if ( creatorDto == null ) {
            return creator;
        }

        if ( creatorDto.getCreatedAt() != null ) {
            creator.setCreatedAt( creatorDto.getCreatedAt() );
        }
        if ( creatorDto.getUpdatedAt() != null ) {
            creator.setUpdatedAt( creatorDto.getUpdatedAt() );
        }
        if ( creatorDto.getEnabled() != null ) {
            creator.setEnabled( creatorDto.getEnabled() );
        }
        if ( creatorDto.getNote() != null ) {
            creator.setNote( creatorDto.getNote() );
        }
        if ( creatorDto.getId() != null ) {
            creator.setId( Long.parseLong( creatorDto.getId() ) );
        }
        if ( creatorDto.getName() != null ) {
            creator.setName( creatorDto.getName() );
        }
        if ( creatorDto.getRole() != null ) {
            creator.setRole( creatorDto.getRole() );
        }

        return creator;
    }
}
