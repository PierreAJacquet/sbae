package project.sbae.service.impl;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.sbae.dto.IncidentDto;
import project.sbae.dto.searchFilterDto;
import project.sbae.mapper.IncidentMapper;
import project.sbae.repository.IncidentRepository;
import project.sbae.service.IncidentService;

import java.util.List;

@Slf4j
@Service
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private IncidentRepository repository;

    @Autowired
    private IncidentMapper mapper;

    @Override
    public List<IncidentDto> searchIncidents(searchFilterDto filter) {

        // On remplace les chaînes vides par null pour que la @Query les ignore
        String title = StringUtils.isNotBlank(filter.getTitle()) ? filter.getTitle() : null;
        String description = StringUtils.isNotBlank(filter.getDescription()) ? filter.getDescription() : null;
        String severite = StringUtils.isNotBlank(filter.getSeverity()) ? filter.getSeverity() : null;
        String fullName = StringUtils.isNotBlank(filter.getFirstName()) ? filter.getFirstName() : null;
        String lastName = StringUtils.isNotBlank(filter.getLastName()) ? filter.getLastName() : null;
        String email = StringUtils.isNotBlank(filter.getEmail()) ? filter.getEmail() : null;

        return mapper.mapAllToDto(repository.searchWithFilters(title, description, severite, fullName, lastName, email));
    }
}