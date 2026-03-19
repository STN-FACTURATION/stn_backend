package sn.stn.facturation.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.stn.facturation.domain.Supplement;

/**
 * Spring Data JPA repository for the Supplement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplementRepository extends JpaRepository<Supplement, Long>, JpaSpecificationExecutor<Supplement> {}
