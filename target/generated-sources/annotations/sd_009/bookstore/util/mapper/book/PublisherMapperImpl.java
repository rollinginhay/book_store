package sd_009.bookstore.util.mapper.book;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sd_009.bookstore.dto.jsonApiResource.book.PublisherDto;
import sd_009.bookstore.entity.book.Publisher;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class PublisherMapperImpl implements PublisherMapper {

    @Override
    public Publisher toEntity(PublisherDto publisherDto) {
        if ( publisherDto == null ) {
            return null;
        }

        Publisher.PublisherBuilder publisher = Publisher.builder();

        if ( publisherDto.getId() != null ) {
            publisher.id( Long.parseLong( publisherDto.getId() ) );
        }
        publisher.name( publisherDto.getName() );

        return publisher.build();
    }

    @Override
    public PublisherDto toDto(Publisher publisher) {
        if ( publisher == null ) {
            return null;
        }

        PublisherDto.PublisherDtoBuilder publisherDto = PublisherDto.builder();

        publisherDto.createdAt( publisher.getCreatedAt() );
        publisherDto.updatedAt( publisher.getUpdatedAt() );
        publisherDto.enabled( publisher.getEnabled() );
        publisherDto.note( publisher.getNote() );
        if ( publisher.getId() != null ) {
            publisherDto.id( String.valueOf( publisher.getId() ) );
        }
        publisherDto.name( publisher.getName() );

        return publisherDto.build();
    }

    @Override
    public Publisher partialUpdate(PublisherDto publisherDto, Publisher publisher) {
        if ( publisherDto == null ) {
            return publisher;
        }

        if ( publisherDto.getCreatedAt() != null ) {
            publisher.setCreatedAt( publisherDto.getCreatedAt() );
        }
        if ( publisherDto.getUpdatedAt() != null ) {
            publisher.setUpdatedAt( publisherDto.getUpdatedAt() );
        }
        if ( publisherDto.getEnabled() != null ) {
            publisher.setEnabled( publisherDto.getEnabled() );
        }
        if ( publisherDto.getNote() != null ) {
            publisher.setNote( publisherDto.getNote() );
        }
        if ( publisherDto.getId() != null ) {
            publisher.setId( Long.parseLong( publisherDto.getId() ) );
        }
        if ( publisherDto.getName() != null ) {
            publisher.setName( publisherDto.getName() );
        }

        return publisher;
    }
}
