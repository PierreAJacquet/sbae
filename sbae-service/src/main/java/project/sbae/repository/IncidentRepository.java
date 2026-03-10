package project.sbae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.sbae.entity.Incident;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer>, JpaSpecificationExecutor<Incident> {

    @Query("""
            SELECT i FROM Incident i
            JOIN i.person p
            WHERE (?1 IS NULL OR i.title LIKE %?1%)
            AND (?2 IS NULL OR i.description LIKE %?2%)
            AND (?3 IS NULL OR i.severity = ?3)
            AND (?4 IS NULL OR p.firstName LIKE %?4%)
            AND (?5 IS NULL OR p.lastName LIKE %?5%)
            AND (?6 IS NULL OR p.email LIKE %?6%)
            """)
    List<Incident> searchWithFilters(String title, String description, String severite,
                                     String fullName, String lastName, String email);
}