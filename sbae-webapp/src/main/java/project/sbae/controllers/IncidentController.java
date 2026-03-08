package project.sbae.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.sbae.dto.IncidentDto;
import project.sbae.entity.Person;
import project.sbae.service.IncidentService;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "Sbae")
@RequestMapping(path = "/api/incident", produces = MediaType.APPLICATION_JSON_VALUE)
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @Operation(summary = "Retourne une liste d'incident selon les paramètres définit")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Paramètres invalides"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur technique")})
    @GetMapping("/search")
    public List<IncidentDto> search(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "severity", required = false) String severity,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "email", required = false) String email
    ) {

        // TODO A reprendre pour recevoir directement un objet Person depuis le Front
        // Reprise des paramètres en objets personnes
        Person filterPerson = new Person();
        filterPerson.setFirstName(firstName);
        filterPerson.setLastName(lastName);
        filterPerson.setEmail(email);

        return incidentService.searchIncidents(title, description, severity, filterPerson);
    }
}
