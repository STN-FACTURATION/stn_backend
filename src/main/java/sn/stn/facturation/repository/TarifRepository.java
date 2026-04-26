package sn.stn.facturation.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.stn.facturation.domain.Tarif;

/**
 * Spring Data JPA repository for the Tarif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TarifRepository extends JpaRepository<Tarif, Long>, JpaSpecificationExecutor<Tarif> {
    @Query("select t from Tarif t where t.actif = true and t.trancheMin <= :volume and (t.trancheMax is null or t.trancheMax > :volume)")
    Optional<Tarif> findActiveTarifForVolume(@Param("volume") Double volume);
}
