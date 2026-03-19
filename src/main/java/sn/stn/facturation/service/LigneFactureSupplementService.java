package sn.stn.facturation.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.stn.facturation.service.dto.LigneFactureSupplementDTO;

/**
 * Service Interface for managing {@link sn.stn.facturation.domain.LigneFactureSupplement}.
 */
public interface LigneFactureSupplementService {
    /**
     * Save a ligneFactureSupplement.
     *
     * @param ligneFactureSupplementDTO the entity to save.
     * @return the persisted entity.
     */
    LigneFactureSupplementDTO save(LigneFactureSupplementDTO ligneFactureSupplementDTO);

    /**
     * Updates a ligneFactureSupplement.
     *
     * @param ligneFactureSupplementDTO the entity to update.
     * @return the persisted entity.
     */
    LigneFactureSupplementDTO update(LigneFactureSupplementDTO ligneFactureSupplementDTO);

    /**
     * Partially updates a ligneFactureSupplement.
     *
     * @param ligneFactureSupplementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LigneFactureSupplementDTO> partialUpdate(LigneFactureSupplementDTO ligneFactureSupplementDTO);

    /**
     * Get all the ligneFactureSupplements with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LigneFactureSupplementDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" ligneFactureSupplement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LigneFactureSupplementDTO> findOne(Long id);

    /**
     * Delete the "id" ligneFactureSupplement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
