package sn.stn.facturation.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.domain.Tarif;
import sn.stn.facturation.repository.TarifRepository;
import sn.stn.facturation.service.TarifService;
import sn.stn.facturation.service.dto.TarifDTO;
import sn.stn.facturation.service.mapper.TarifMapper;

/**
 * Service Implementation for managing {@link sn.stn.facturation.domain.Tarif}.
 */
@Service
@Transactional
public class TarifServiceImpl implements TarifService {

    private static final Logger LOG = LoggerFactory.getLogger(TarifServiceImpl.class);

    private final TarifRepository tarifRepository;

    private final TarifMapper tarifMapper;

    public TarifServiceImpl(TarifRepository tarifRepository, TarifMapper tarifMapper) {
        this.tarifRepository = tarifRepository;
        this.tarifMapper = tarifMapper;
    }

    @Override
    public TarifDTO save(TarifDTO tarifDTO) {
        LOG.debug("Request to save Tarif : {}", tarifDTO);
        Tarif tarif = tarifMapper.toEntity(tarifDTO);
        tarif = tarifRepository.save(tarif);
        return tarifMapper.toDto(tarif);
    }

    @Override
    public TarifDTO update(TarifDTO tarifDTO) {
        LOG.debug("Request to update Tarif : {}", tarifDTO);
        Tarif tarif = tarifMapper.toEntity(tarifDTO);
        tarif = tarifRepository.save(tarif);
        return tarifMapper.toDto(tarif);
    }

    @Override
    public Optional<TarifDTO> partialUpdate(TarifDTO tarifDTO) {
        LOG.debug("Request to partially update Tarif : {}", tarifDTO);

        return tarifRepository
            .findById(tarifDTO.getId())
            .map(existingTarif -> {
                tarifMapper.partialUpdate(existingTarif, tarifDTO);

                return existingTarif;
            })
            .map(tarifRepository::save)
            .map(tarifMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TarifDTO> findOne(Long id) {
        LOG.debug("Request to get Tarif : {}", id);
        return tarifRepository.findById(id).map(tarifMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Tarif : {}", id);
        tarifRepository.deleteById(id);
    }
}
