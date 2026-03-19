package sn.stn.facturation.service;

import java.util.Optional;
import sn.stn.facturation.service.dto.HistoriqueTarifDTO;

/**
 * Service Interface for managing {@link sn.stn.facturation.domain.HistoriqueTarif}.
 */
public interface HistoriqueTarifService {
    /**
     * Save a historiqueTarif.
     *
     * @param historiqueTarifDTO the entity to save.
     * @return the persisted entity.
     */
    HistoriqueTarifDTO save(HistoriqueTarifDTO historiqueTarifDTO);

    /**
     * Updates a historiqueTarif.
     *
     * @param historiqueTarifDTO the entity to update.
     * @return the persisted entity.
     */
    HistoriqueTarifDTO update(HistoriqueTarifDTO historiqueTarifDTO);

    /**
     * Partially updates a historiqueTarif.
     *
     * @param historiqueTarifDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HistoriqueTarifDTO> partialUpdate(HistoriqueTarifDTO historiqueTarifDTO);

    /**
     * Get the "id" historiqueTarif.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoriqueTarifDTO> findOne(Long id);

    /**
     * Delete the "id" historiqueTarif.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
