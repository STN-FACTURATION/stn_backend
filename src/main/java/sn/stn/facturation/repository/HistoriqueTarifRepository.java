package sn.stn.facturation.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.stn.facturation.domain.HistoriqueTarif;

/**
 * Spring Data JPA repository for the HistoriqueTarif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriqueTarifRepository extends JpaRepository<HistoriqueTarif, Long>, JpaSpecificationExecutor<HistoriqueTarif> {}
