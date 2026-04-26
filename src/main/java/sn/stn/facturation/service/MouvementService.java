package sn.stn.facturation.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.stn.facturation.service.dto.MouvementDTO;

/**
 * Service Interface for managing {@link sn.stn.facturation.domain.Mouvement}.
 */
public interface MouvementService {
    /**
     * Save a mouvement.
     *
     * @param mouvementDTO the entity to save.
     * @return the persisted entity.
     */
    MouvementDTO save(MouvementDTO mouvementDTO);

    /**
     * Updates a mouvement.
     *
     * @param mouvementDTO the entity to update.
     * @return the persisted entity.
     */
    MouvementDTO update(MouvementDTO mouvementDTO);

    /**
     * Partially updates a mouvement.
     *
     * @param mouvementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MouvementDTO> partialUpdate(MouvementDTO mouvementDTO);

    /**
     * Get all the mouvements with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MouvementDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" mouvement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MouvementDTO> findOne(Long id);

    /**
     * Delete the "id" mouvement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
