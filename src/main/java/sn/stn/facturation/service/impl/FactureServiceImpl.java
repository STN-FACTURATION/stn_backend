package sn.stn.facturation.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.domain.Facture;
import sn.stn.facturation.repository.FactureRepository;
import sn.stn.facturation.service.FactureService;
import sn.stn.facturation.service.dto.FactureDTO;
import sn.stn.facturation.service.mapper.FactureMapper;

/**
 * Service Implementation for managing {@link sn.stn.facturation.domain.Facture}.
 */
@Service
@Transactional
public class FactureServiceImpl implements FactureService {

    private static final Logger LOG = LoggerFactory.getLogger(FactureServiceImpl.class);

    private final FactureRepository factureRepository;

    private final FactureMapper factureMapper;

    public FactureServiceImpl(FactureRepository factureRepository, FactureMapper factureMapper) {
        this.factureRepository = factureRepository;
        this.factureMapper = factureMapper;
    }

    @Override
    public FactureDTO save(FactureDTO factureDTO) {
        LOG.debug("Request to save Facture : {}", factureDTO);
        Facture facture = factureMapper.toEntity(factureDTO);
        facture = factureRepository.save(facture);
        return factureMapper.toDto(facture);
    }

    @Override
    public FactureDTO update(FactureDTO factureDTO) {
        LOG.debug("Request to update Facture : {}", factureDTO);
        Facture facture = factureMapper.toEntity(factureDTO);
        facture = factureRepository.save(facture);
        return factureMapper.toDto(facture);
    }

    @Override
    public Optional<FactureDTO> partialUpdate(FactureDTO factureDTO) {
        LOG.debug("Request to partially update Facture : {}", factureDTO);

        return factureRepository
            .findById(factureDTO.getId())
            .map(existingFacture -> {
                factureMapper.partialUpdate(existingFacture, factureDTO);

                return existingFacture;
            })
            .map(factureRepository::save)
            .map(factureMapper::toDto);
    }

    public Page<FactureDTO> findAllWithEagerRelationships(Pageable pageable) {
        return factureRepository.findAllWithEagerRelationships(pageable).map(factureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FactureDTO> findOne(Long id) {
        LOG.debug("Request to get Facture : {}", id);
        return factureRepository.findOneWithEagerRelationships(id).map(factureMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Facture : {}", id);
        factureRepository.deleteById(id);
    }
}
