package sn.stn.facturation.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * Get all the navires with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NavireDTO> findAllWithEagerRelationships(Pageable pageable);

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
