package sn.stn.facturation.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.stn.facturation.domain.Mouvement;

/**
 * Spring Data JPA repository for the Mouvement entity.
 */
@Repository
public interface MouvementRepository extends JpaRepository<Mouvement, Long>, JpaSpecificationExecutor<Mouvement> {
    default Optional<Mouvement> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Mouvement> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Mouvement> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select mouvement from Mouvement mouvement left join fetch mouvement.remorqueur left join fetch mouvement.facture",
        countQuery = "select count(mouvement) from Mouvement mouvement"
    )
    Page<Mouvement> findAllWithToOneRelationships(Pageable pageable);

    @Query("select mouvement from Mouvement mouvement left join fetch mouvement.remorqueur left join fetch mouvement.facture")
    List<Mouvement> findAllWithToOneRelationships();

    @Query(
        "select mouvement from Mouvement mouvement left join fetch mouvement.remorqueur left join fetch mouvement.facture where mouvement.id =:id"
    )
    Optional<Mouvement> findOneWithToOneRelationships(@Param("id") Long id);
}
