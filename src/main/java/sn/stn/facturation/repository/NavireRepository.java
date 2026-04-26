package sn.stn.facturation.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.stn.facturation.domain.Navire;

/**
 * Spring Data JPA repository for the Navire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NavireRepository extends JpaRepository<Navire, Long>, JpaSpecificationExecutor<Navire> {}
