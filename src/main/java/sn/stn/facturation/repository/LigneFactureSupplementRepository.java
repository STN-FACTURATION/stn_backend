package sn.stn.facturation.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.stn.facturation.domain.LigneFactureSupplement;

/**
 * Spring Data JPA repository for the LigneFactureSupplement entity.
 */
@Repository
public interface LigneFactureSupplementRepository
    extends JpaRepository<LigneFactureSupplement, Long>, JpaSpecificationExecutor<LigneFactureSupplement> {
    default Optional<LigneFactureSupplement> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LigneFactureSupplement> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LigneFactureSupplement> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select ligneFactureSupplement from LigneFactureSupplement ligneFactureSupplement left join fetch ligneFactureSupplement.supplement",
        countQuery = "select count(ligneFactureSupplement) from LigneFactureSupplement ligneFactureSupplement"
    )
    Page<LigneFactureSupplement> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select ligneFactureSupplement from LigneFactureSupplement ligneFactureSupplement left join fetch ligneFactureSupplement.supplement"
    )
    List<LigneFactureSupplement> findAllWithToOneRelationships();

    @Query(
        "select ligneFactureSupplement from LigneFactureSupplement ligneFactureSupplement left join fetch ligneFactureSupplement.supplement where ligneFactureSupplement.id =:id"
    )
    Optional<LigneFactureSupplement> findOneWithToOneRelationships(@Param("id") Long id);
}
