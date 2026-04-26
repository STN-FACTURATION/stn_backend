package sn.stn.facturation.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.stn.facturation.domain.Remorqueur;

/**
 * Spring Data JPA repository for the Remorqueur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RemorqueurRepository extends JpaRepository<Remorqueur, Long>, JpaSpecificationExecutor<Remorqueur> {}
