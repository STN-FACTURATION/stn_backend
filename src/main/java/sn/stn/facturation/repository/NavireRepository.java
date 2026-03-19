package sn.stn.facturation.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.stn.facturation.domain.Navire;

/**
 * Spring Data JPA repository for the Navire entity.
 */
@Repository
public interface NavireRepository extends JpaRepository<Navire, Long>, JpaSpecificationExecutor<Navire> {
    default Optional<Navire> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Navire> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Navire> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select navire from Navire navire left join fetch navire.client", countQuery = "select count(navire) from Navire navire")
    Page<Navire> findAllWithToOneRelationships(Pageable pageable);

    @Query("select navire from Navire navire left join fetch navire.client")
    List<Navire> findAllWithToOneRelationships();

    @Query("select navire from Navire navire left join fetch navire.client where navire.id =:id")
    Optional<Navire> findOneWithToOneRelationships(@Param("id") Long id);
}
