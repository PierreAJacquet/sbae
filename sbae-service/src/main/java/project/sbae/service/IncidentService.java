package project.sbae.service;

import project.sbae.dto.IncidentDto;
import project.sbae.dto.searchFilterDto;

import java.util.List;

public interface IncidentService {

    /**
     * Recherche des incidents en fonction de différents critères.
     * <p>
     * Cette méthode permet de filtrer les incidents selon leur titre, leur description, leur niveau de sévérité et les informations de la personne associée.
     * L'ensemble de ses informations sont stockés dans un objet Filter.
     * Les paramètres peuvent être null afin d'effectuer une recherche partielle selon les critères fournis.
     *
     * @param searchFilterDto Objet métier contenant l'ensemble des filtres sur les incidents
     * @return une liste d'objets {@link IncidentDto} correspondant aux critères de recherche
     */
    public List<IncidentDto> searchIncidents(searchFilterDto searchFilterDto);
}
