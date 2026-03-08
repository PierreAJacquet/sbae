package project.sbae.service;

import project.sbae.dto.IncidentDto;
import project.sbae.entity.Person;

import java.util.List;

public interface IncidentService {

    /**
     * Recherche des incidents en fonction de différents critères.
     * <p>
     * Cette méthode permet de filtrer les incidents selon leur titre, leur description, leur niveau de sévérité et la personne associée.
     * Les paramètres peuvent être null afin d'effectuer une recherche partielle selon les critères fournis.
     *
     * @param title le titre de l'incident à rechercher
     * @param description la description de l'incident à rechercher
     * @param severity le niveau de sévérité de l'incident
     * @param person la personne associée à l'incident
     * @return une liste d'objets {@link IncidentDto} correspondant aux critères de recherche
     */
    public List<IncidentDto> searchIncidents(String title, String description, String severity, Person person);
}
