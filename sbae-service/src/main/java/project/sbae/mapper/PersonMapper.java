package project.sbae.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import project.sbae.dto.PersonDto;
import project.sbae.entity.Person;

@Mapper(componentModel = "spring")
@Named("PersonMapper")
public interface PersonMapper {

    @Named("toDto")
    PersonDto mapToDto(Person entity);

    @Named("toModel")
    Person mapToEntity(PersonDto dto);
}
