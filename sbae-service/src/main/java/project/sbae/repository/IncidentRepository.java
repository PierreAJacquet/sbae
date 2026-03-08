package project.sbae.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import project.sbae.entity.Incident;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer>, JpaSpecificationExecutor<Incident> {

    @Override
    // TODO Reprendre pour optimisation
    List<Incident> findAll();
}