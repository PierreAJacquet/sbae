package project.sbae.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import project.sbae.dto.searchFilterDto;
import project.sbae.dto.IncidentDto;
import project.sbae.service.IncidentService;

@Slf4j
@RestController
@Tag(name = "Sbae")
@RequestMapping(path = "/api/incidents", produces = MediaType.APPLICATION_JSON_VALUE)
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @Operation(summary = "Retourne une page d'incident selon les paramètres définit")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "204", description = "Appel valide mais aucun contenue"),
            @ApiResponse(responseCode = "400", description = "Paramètres invalides"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur technique")})
    /* Post est plus adapté que Get dans ce contexte.
     RequestBody permet :
     1. Clarté : Le contrat entre le front et le back est défini par un DTO, ce qui limite les erreurs de nommage de paramètres.
     2. Évolutivité : L'ajout d'un nouveau filtre modifie uniquement le DTO sans changer la signature de la méthode.
     3. Confort : Simplifie la gestion des données nulles ou vides.
     */
    @PostMapping("/search-incidents")
    public Page<IncidentDto> searchIncidents(@RequestBody searchFilterDto searchFilterDto,
                                                       @PageableDefault(size = 10) Pageable pageable) {
        return incidentService.searchIncidents(searchFilterDto, pageable);
    }
}
