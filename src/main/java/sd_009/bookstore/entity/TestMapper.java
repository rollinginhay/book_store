package sd_009.bookstore.entity;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TestMapper {
    Test toEntity(TestDto testDto);

    TestDto toDto(Test test);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Test partialUpdate(TestDto testDto, @MappingTarget Test test);
}