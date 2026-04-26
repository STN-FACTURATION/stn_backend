package sn.stn.facturation.service;

import java.util.Optional;
import sn.stn.facturation.service.dto.NavireDTO;

/**
 * Service Interface for managing {@link sn.stn.facturation.domain.Navire}.
 */
public interface NavireService {
    /**
     * Save a navire.
     *
     * @param navireDTO the entity to save.
     * @return the persisted entity.
     */
    NavireDTO save(NavireDTO navireDTO);

    /**
     * Updates a navire.
     *
     * @param navireDTO the entity to update.
     * @return the persisted entity.
     */
    NavireDTO update(NavireDTO navireDTO);

    /**
     * Partially updates a navire.
     *
     * @param navireDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NavireDTO> partialUpdate(NavireDTO navireDTO);

    /**
     * Get the "id" navire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NavireDTO> findOne(Long id);

    /**
     * Delete the "id" navire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
