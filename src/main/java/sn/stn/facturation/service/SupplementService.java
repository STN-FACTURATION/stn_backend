package sn.stn.facturation.service;

import java.util.Optional;
import sn.stn.facturation.service.dto.SupplementDTO;

/**
 * Service Interface for managing {@link sn.stn.facturation.domain.Supplement}.
 */
public interface SupplementService {
    /**
     * Save a supplement.
     *
     * @param supplementDTO the entity to save.
     * @return the persisted entity.
     */
    SupplementDTO save(SupplementDTO supplementDTO);

    /**
     * Updates a supplement.
     *
     * @param supplementDTO the entity to update.
     * @return the persisted entity.
     */
    SupplementDTO update(SupplementDTO supplementDTO);

    /**
     * Partially updates a supplement.
     *
     * @param supplementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplementDTO> partialUpdate(SupplementDTO supplementDTO);

    /**
     * Get the "id" supplement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplementDTO> findOne(Long id);

    /**
     * Delete the "id" supplement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
