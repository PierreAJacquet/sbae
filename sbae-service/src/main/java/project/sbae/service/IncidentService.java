package project.sbae.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.sbae.dto.IncidentDto;
import project.sbae.dto.searchFilterDto;



public interface IncidentService {

    /**
     * Recherche paginée des incidents en fonction de différents critères.
     * <p>
     * Cette méthode extrait les critères du DTO de recherche pour interroger la base via
     * une requête SQL optimisée. Elle supporte la recherche partielle (LIKE) et l'omission de certains filtres.
     *
     * @param searchFilterDto Objet métier contenant l'ensemble des filtres sur les incidents
     * @param pageable Configuration de la pagination (index de page, taille, tri)
     * @return une page d'objets {@link IncidentDto} correspondant aux critères de recherche
     */
     Page<IncidentDto> searchIncidents(searchFilterDto searchFilterDto, Pageable pageable);
}
