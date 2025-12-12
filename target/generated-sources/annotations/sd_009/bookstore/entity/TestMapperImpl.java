package sd_009.bookstore.entity;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:13:04+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class TestMapperImpl implements TestMapper {

    @Override
    public Test toEntity(TestDto testDto) {
        if ( testDto == null ) {
            return null;
        }

        Test.TestBuilder test = Test.builder();

        if ( testDto.getId() != null ) {
            test.id( Long.parseLong( testDto.getId() ) );
        }
        test.name( testDto.getName() );

        return test.build();
    }

    @Override
    public TestDto toDto(Test test) {
        if ( test == null ) {
            return null;
        }

        String id = null;
        String name = null;

        if ( test.getId() != null ) {
            id = String.valueOf( test.getId() );
        }
        name = test.getName();

        TestDto testDto = new TestDto( id, name );

        return testDto;
    }

    @Override
    public Test partialUpdate(TestDto testDto, Test test) {
        if ( testDto == null ) {
            return test;
        }

        if ( testDto.getId() != null ) {
            test.setId( Long.parseLong( testDto.getId() ) );
        }
        if ( testDto.getName() != null ) {
            test.setName( testDto.getName() );
        }

        return test;
    }
}
