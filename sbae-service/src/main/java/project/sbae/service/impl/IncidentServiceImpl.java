package project.sbae.service.impl;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.sbae.dto.IncidentDto;
import project.sbae.dto.searchFilterDto;
import project.sbae.entity.Person;
import project.sbae.mapper.IncidentMapper;
import project.sbae.repository.IncidentRepository;
import project.sbae.service.IncidentService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private IncidentRepository repository;

    @Autowired
    private IncidentMapper mapper;

    @Override
    public List<IncidentDto> searchIncidents(searchFilterDto searchFilterDto) {

        // Reprise des paramètres en objets personnes
        Person filterPerson = new Person();
        filterPerson.setFirstName(searchFilterDto.getTitle());
        filterPerson.setLastName(searchFilterDto.getDescription());
        filterPerson.setEmail(searchFilterDto.getSeverity());

        // TODO A reprendre pour faire le filtrage directement dans le repo via EntityGraph ou @Query
        return mapper.mapAllToDto(
                repository.findAll()
                        .stream()
                        .filter(incident -> matches(searchFilterDto.getTitle(), incident.getTitle()))
                        .filter(incident -> matches(searchFilterDto.getDescription(), incident.getDescription()))
                        .filter(incident -> matches(searchFilterDto.getSeverity(), incident.getSeverity()))
                        .filter(incident -> matchesPerson(filterPerson, incident.getPerson()))
                        .collect(Collectors.toList())
        );
    }

    private boolean matches(String filter, String value) {
        return StringUtils.isBlank(filter) || filter.equals(value);
    }

    private boolean matchesPerson(Person filterPerson, Person incidentPerson) {

        if (filterPerson == null) {
            return true;
        }

        if (incidentPerson == null) {
            return false;
        }

        return matches(filterPerson.getFirstName(), incidentPerson.getFirstName())
                && matches(filterPerson.getLastName(), incidentPerson.getLastName())
                && matches(filterPerson.getEmail(), incidentPerson.getEmail());
    }
}