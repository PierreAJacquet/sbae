package project.sbae.service.impl;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.sbae.dto.IncidentDto;
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
    public List<IncidentDto> searchIncidents(String title, String description, String severity, Person person) {

        // TODO A reprendre pour faire le filtrage directement dans le repo via EntityGraph ou @Query
        return mapper.mapAllToDto(
                repository.findAll()
                        .stream()
                        .filter(incident -> matches(title, incident.getTitle()))
                        .filter(incident -> matches(description, incident.getDescription()))
                        .filter(incident -> matches(severity, incident.getSeverity()))
                        .filter(incident -> matchesPerson(person, incident.getPerson()))
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