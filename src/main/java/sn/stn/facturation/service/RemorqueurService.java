package sn.stn.facturation.service;

import java.util.Optional;
import sn.stn.facturation.service.dto.RemorqueurDTO;

/**
 * Service Interface for managing {@link sn.stn.facturation.domain.Remorqueur}.
 */
public interface RemorqueurService {
    /**
     * Save a remorqueur.
     *
     * @param remorqueurDTO the entity to save.
     * @return the persisted entity.
     */
    RemorqueurDTO save(RemorqueurDTO remorqueurDTO);

    /**
     * Updates a remorqueur.
     *
     * @param remorqueurDTO the entity to update.
     * @return the persisted entity.
     */
    RemorqueurDTO update(RemorqueurDTO remorqueurDTO);

    /**
     * Partially updates a remorqueur.
     *
     * @param remorqueurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RemorqueurDTO> partialUpdate(RemorqueurDTO remorqueurDTO);

    /**
     * Get the "id" remorqueur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RemorqueurDTO> findOne(Long id);

    /**
     * Delete the "id" remorqueur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
