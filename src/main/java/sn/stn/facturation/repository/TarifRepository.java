package sn.stn.facturation.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.stn.facturation.domain.Tarif;

/**
 * Spring Data JPA repository for the Tarif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TarifRepository extends JpaRepository<Tarif, Long>, JpaSpecificationExecutor<Tarif> {}
