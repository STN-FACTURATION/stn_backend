package sn.stn.facturation.service;

import java.util.Optional;
import sn.stn.facturation.service.dto.TarifDTO;

/**
 * Service Interface for managing {@link sn.stn.facturation.domain.Tarif}.
 */
public interface TarifService {
    /**
     * Save a tarif.
     *
     * @param tarifDTO the entity to save.
     * @return the persisted entity.
     */
    TarifDTO save(TarifDTO tarifDTO);

    /**
     * Updates a tarif.
     *
     * @param tarifDTO the entity to update.
     * @return the persisted entity.
     */
    TarifDTO update(TarifDTO tarifDTO);

    /**
     * Partially updates a tarif.
     *
     * @param tarifDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TarifDTO> partialUpdate(TarifDTO tarifDTO);

    /**
     * Get the "id" tarif.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TarifDTO> findOne(Long id);

    /**
     * Delete the "id" tarif.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
