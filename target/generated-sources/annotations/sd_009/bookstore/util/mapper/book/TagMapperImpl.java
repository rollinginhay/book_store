package sd_009.bookstore.util.mapper.book;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.TagDto;
import sd_009.bookstore.entity.book.Tag;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class TagMapperImpl implements TagMapper {

    @Override
    public Tag toEntity(TagDto tagDto) {
        if ( tagDto == null ) {
            return null;
        }

        Tag.TagBuilder tag = Tag.builder();

        if ( tagDto.getId() != null ) {
            tag.id( Long.parseLong( tagDto.getId() ) );
        }
        tag.name( tagDto.getName() );

        return tag.build();
    }

    @Override
    public TagDto toDto(Tag tag) {
        if ( tag == null ) {
            return null;
        }

        TagDto.TagDtoBuilder tagDto = TagDto.builder();

        tagDto.createdAt( tag.getCreatedAt() );
        tagDto.updatedAt( tag.getUpdatedAt() );
        tagDto.enabled( tag.getEnabled() );
        tagDto.note( tag.getNote() );
        if ( tag.getId() != null ) {
            tagDto.id( String.valueOf( tag.getId() ) );
        }
        tagDto.name( tag.getName() );

        return tagDto.build();
    }

    @Override
    public Tag partialUpdate(TagDto tagDto, Tag tag) {
        if ( tagDto == null ) {
            return tag;
        }

        if ( tagDto.getCreatedAt() != null ) {
            tag.setCreatedAt( tagDto.getCreatedAt() );
        }
        if ( tagDto.getUpdatedAt() != null ) {
            tag.setUpdatedAt( tagDto.getUpdatedAt() );
        }
        if ( tagDto.getEnabled() != null ) {
            tag.setEnabled( tagDto.getEnabled() );
        }
        if ( tagDto.getNote() != null ) {
            tag.setNote( tagDto.getNote() );
        }
        if ( tagDto.getId() != null ) {
            tag.setId( Long.parseLong( tagDto.getId() ) );
        }
        if ( tagDto.getName() != null ) {
            tag.setName( tagDto.getName() );
        }

        return tag;
    }
}
