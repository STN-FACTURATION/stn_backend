package sn.stn.facturation.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.stn.facturation.domain.Facture;

/**
 * Spring Data JPA repository for the Facture entity.
 */
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long>, JpaSpecificationExecutor<Facture> {
    default Optional<Facture> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Facture> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Facture> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select facture from Facture facture left join fetch facture.navire left join fetch facture.client", countQuery = "select count(facture) from Facture facture")
    Page<Facture> findAllWithToOneRelationships(Pageable pageable);

    @Query("select facture from Facture facture left join fetch facture.navire left join fetch facture.client")
    List<Facture> findAllWithToOneRelationships();

    @Query("select facture from Facture facture left join fetch facture.navire left join fetch facture.client where facture.id =:id")
    Optional<Facture> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select facture from Facture facture " +
            "left join fetch facture.navire " +
            "left join fetch facture.client " +
            "left join fetch facture.mouvements " +
            "left join fetch facture.supplements " +
            "where facture.id =:id")
    Optional<Facture> findOneWithAllRelationships(@Param("id") Long id);
}
