package sn.stn.facturation.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.domain.LigneFactureSupplement;
import sn.stn.facturation.repository.LigneFactureSupplementRepository;
import sn.stn.facturation.service.LigneFactureSupplementService;
import sn.stn.facturation.service.dto.LigneFactureSupplementDTO;
import sn.stn.facturation.service.mapper.LigneFactureSupplementMapper;

/**
 * Service Implementation for managing
 * {@link sn.stn.facturation.domain.LigneFactureSupplement}.
 */
@Service
@Transactional
public class LigneFactureSupplementServiceImpl implements LigneFactureSupplementService {

    private static final Logger LOG = LoggerFactory.getLogger(LigneFactureSupplementServiceImpl.class);

    private final LigneFactureSupplementRepository ligneFactureSupplementRepository;

    private final LigneFactureSupplementMapper ligneFactureSupplementMapper;

    private final sn.stn.facturation.repository.FactureRepository factureRepository;

    private final sn.stn.facturation.service.BillingService billingService;

    public LigneFactureSupplementServiceImpl(
            LigneFactureSupplementRepository ligneFactureSupplementRepository,
            LigneFactureSupplementMapper ligneFactureSupplementMapper,
            sn.stn.facturation.repository.FactureRepository factureRepository,
            sn.stn.facturation.service.BillingService billingService) {
        this.ligneFactureSupplementRepository = ligneFactureSupplementRepository;
        this.ligneFactureSupplementMapper = ligneFactureSupplementMapper;
        this.factureRepository = factureRepository;
        this.billingService = billingService;
    }

    @Override
    public LigneFactureSupplementDTO save(LigneFactureSupplementDTO ligneFactureSupplementDTO) {
        LOG.debug("Request to save LigneFactureSupplement : {}", ligneFactureSupplementDTO);
        LigneFactureSupplement ligneFactureSupplement = ligneFactureSupplementMapper
                .toEntity(ligneFactureSupplementDTO);
        ligneFactureSupplement = ligneFactureSupplementRepository.save(ligneFactureSupplement);
        updateFactureTotals(ligneFactureSupplement);
        return ligneFactureSupplementMapper.toDto(ligneFactureSupplement);
    }

    @Override
    public LigneFactureSupplementDTO update(LigneFactureSupplementDTO ligneFactureSupplementDTO) {
        LOG.debug("Request to update LigneFactureSupplement : {}", ligneFactureSupplementDTO);
        LigneFactureSupplement ligneFactureSupplement = ligneFactureSupplementMapper
                .toEntity(ligneFactureSupplementDTO);
        ligneFactureSupplement = ligneFactureSupplementRepository.save(ligneFactureSupplement);
        updateFactureTotals(ligneFactureSupplement);
        return ligneFactureSupplementMapper.toDto(ligneFactureSupplement);
    }

    @Override
    public Optional<LigneFactureSupplementDTO> partialUpdate(LigneFactureSupplementDTO ligneFactureSupplementDTO) {
        LOG.debug("Request to partially update LigneFactureSupplement : {}", ligneFactureSupplementDTO);

        return ligneFactureSupplementRepository
                .findById(ligneFactureSupplementDTO.getId())
                .map(existingLigneFactureSupplement -> {
                    ligneFactureSupplementMapper.partialUpdate(existingLigneFactureSupplement,
                            ligneFactureSupplementDTO);

                    return existingLigneFactureSupplement;
                })
                .map(ligneFactureSupplementRepository::save)
                .map(savedSupplement -> {
                    updateFactureTotals(savedSupplement);
                    return savedSupplement;
                })
                .map(ligneFactureSupplementMapper::toDto);
    }

    private void updateFactureTotals(LigneFactureSupplement supplement) {
        if (supplement.getFacture() != null && supplement.getFacture().getId() != null) {
            factureRepository
                    .findOneWithAllRelationships(supplement.getFacture().getId())
                    .ifPresent(facture -> {
                        billingService.recalculateTotals(facture);
                        factureRepository.save(facture);
                    });
        }
    }

    public Page<LigneFactureSupplementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ligneFactureSupplementRepository.findAllWithEagerRelationships(pageable)
                .map(ligneFactureSupplementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LigneFactureSupplementDTO> findOne(Long id) {
        LOG.debug("Request to get LigneFactureSupplement : {}", id);
        return ligneFactureSupplementRepository.findOneWithEagerRelationships(id)
                .map(ligneFactureSupplementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete LigneFactureSupplement : {}", id);
        ligneFactureSupplementRepository.deleteById(id);
    }
}
