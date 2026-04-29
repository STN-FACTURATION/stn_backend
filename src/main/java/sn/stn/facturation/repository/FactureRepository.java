package sn.stn.facturation.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.stn.facturation.domain.Facture;
import sn.stn.facturation.repository.projection.*;

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

    @Query(value = "SELECT " +
            "SUM(IF(date_emission >= :startOfMonth, montant_ttc, 0)) as billedThisMonth, " +
            "SUM(IF(date_emission >= :startOfMonth AND statut = 'PAYEE', montant_ttc, 0)) as paidThisMonth, " +
            "SUM(IF(date_emission >= :startOfMonth, volume_m_3, 0)) as totalVolume, " +
            "SUM(IF(date_emission >= :startOfLastMonth AND date_emission < :startOfMonth, montant_ttc, 0)) as billedLastMonth " +
            "FROM facture", nativeQuery = true)
    Optional<GeneralStatsProjection> getGeneralStats(@Param("startOfMonth") java.time.Instant startOfMonth, @Param("startOfLastMonth") java.time.Instant startOfLastMonth);

    @Query(value = "SELECT c.nom as nom, SUM(f.montant_ttc) as total, COUNT(f.id) as count " +
            "FROM facture f JOIN client c ON f.client_id = c.id " +
            "WHERE f.date_emission >= :sixMonthsAgo " +
            "GROUP BY c.id, c.nom " +
            "ORDER BY total DESC LIMIT 5", nativeQuery = true)
    List<TopClientProjection> getTopClients(@Param("sixMonthsAgo") java.time.Instant sixMonthsAgo);

    @Query(value = "SELECT DATE_FORMAT(date_emission, '%m/%Y') as month, " +
            "SUM(montant_ttc) as billed, " +
            "SUM(IF(statut = 'PAYEE', montant_ttc, 0)) as paid, " +
            "COUNT(id) as count " +
            "FROM facture " +
            "WHERE date_emission >= :sixMonthsAgo " +
            "GROUP BY month " +
            "ORDER BY MIN(date_emission) ASC", nativeQuery = true)
    List<MonthlyEvolutionProjection> getMonthlyEvolution(@Param("sixMonthsAgo") java.time.Instant sixMonthsAgo);

    @Query(value = "SELECT SUM(montant_ttc) as totalBilled, SUM(IF(statut = 'PAYEE', montant_ttc, 0)) as totalPaid FROM facture WHERE date_emission >= :sixMonthsAgo", nativeQuery = true)
    Optional<RecoveryTotalsProjection> getRecoveryTotals(@Param("sixMonthsAgo") java.time.Instant sixMonthsAgo);
}
