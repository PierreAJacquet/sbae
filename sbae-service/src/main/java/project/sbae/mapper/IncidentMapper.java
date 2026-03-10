package project.sbae.mapper;

import org.mapstruct.*;
import project.sbae.dto.IncidentDto;
import project.sbae.entity.Incident;

@Mapper(uses = {
        PersonMapper.class
}, componentModel = "spring")
@Named("IncidentMapper")
public interface IncidentMapper {

    @Mapping(target = "person", source = "person", qualifiedByName = {"PersonMapper", "toDto"}, conditionQualifiedByName = "isInitialized")
    @Named("toDto")
    IncidentDto mapToDto(Incident entity);

    @Mapping(target = "person", source = "person", qualifiedByName = {"PersonMapper", "toModel"}, conditionQualifiedByName = "isInitialized")
    @Named("toModel")
    Incident mapToEntity(IncidentDto dto);

}
