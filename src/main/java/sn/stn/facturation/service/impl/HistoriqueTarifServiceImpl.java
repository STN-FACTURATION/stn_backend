package sn.stn.facturation.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.domain.HistoriqueTarif;
import sn.stn.facturation.repository.HistoriqueTarifRepository;
import sn.stn.facturation.service.HistoriqueTarifService;
import sn.stn.facturation.service.dto.HistoriqueTarifDTO;
import sn.stn.facturation.service.mapper.HistoriqueTarifMapper;

/**
 * Service Implementation for managing {@link sn.stn.facturation.domain.HistoriqueTarif}.
 */
@Service
@Transactional
public class HistoriqueTarifServiceImpl implements HistoriqueTarifService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueTarifServiceImpl.class);

    private final HistoriqueTarifRepository historiqueTarifRepository;

    private final HistoriqueTarifMapper historiqueTarifMapper;

    public HistoriqueTarifServiceImpl(HistoriqueTarifRepository historiqueTarifRepository, HistoriqueTarifMapper historiqueTarifMapper) {
        this.historiqueTarifRepository = historiqueTarifRepository;
        this.historiqueTarifMapper = historiqueTarifMapper;
    }

    @Override
    public HistoriqueTarifDTO save(HistoriqueTarifDTO historiqueTarifDTO) {
        LOG.debug("Request to save HistoriqueTarif : {}", historiqueTarifDTO);
        HistoriqueTarif historiqueTarif = historiqueTarifMapper.toEntity(historiqueTarifDTO);
        historiqueTarif = historiqueTarifRepository.save(historiqueTarif);
        return historiqueTarifMapper.toDto(historiqueTarif);
    }

    @Override
    public HistoriqueTarifDTO update(HistoriqueTarifDTO historiqueTarifDTO) {
        LOG.debug("Request to update HistoriqueTarif : {}", historiqueTarifDTO);
        HistoriqueTarif historiqueTarif = historiqueTarifMapper.toEntity(historiqueTarifDTO);
        historiqueTarif = historiqueTarifRepository.save(historiqueTarif);
        return historiqueTarifMapper.toDto(historiqueTarif);
    }

    @Override
    public Optional<HistoriqueTarifDTO> partialUpdate(HistoriqueTarifDTO historiqueTarifDTO) {
        LOG.debug("Request to partially update HistoriqueTarif : {}", historiqueTarifDTO);

        return historiqueTarifRepository
            .findById(historiqueTarifDTO.getId())
            .map(existingHistoriqueTarif -> {
                historiqueTarifMapper.partialUpdate(existingHistoriqueTarif, historiqueTarifDTO);

                return existingHistoriqueTarif;
            })
            .map(historiqueTarifRepository::save)
            .map(historiqueTarifMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriqueTarifDTO> findOne(Long id) {
        LOG.debug("Request to get HistoriqueTarif : {}", id);
        return historiqueTarifRepository.findById(id).map(historiqueTarifMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete HistoriqueTarif : {}", id);
        historiqueTarifRepository.deleteById(id);
    }
}
