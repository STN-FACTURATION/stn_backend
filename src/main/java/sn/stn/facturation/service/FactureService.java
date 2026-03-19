package sn.stn.facturation.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.stn.facturation.service.dto.FactureDTO;

/**
 * Service Interface for managing {@link sn.stn.facturation.domain.Facture}.
 */
public interface FactureService {
    /**
     * Save a facture.
     *
     * @param factureDTO the entity to save.
     * @return the persisted entity.
     */
    FactureDTO save(FactureDTO factureDTO);

    /**
     * Updates a facture.
     *
     * @param factureDTO the entity to update.
     * @return the persisted entity.
     */
    FactureDTO update(FactureDTO factureDTO);

    /**
     * Partially updates a facture.
     *
     * @param factureDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FactureDTO> partialUpdate(FactureDTO factureDTO);

    /**
     * Get all the factures with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FactureDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" facture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FactureDTO> findOne(Long id);

    /**
     * Delete the "id" facture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
