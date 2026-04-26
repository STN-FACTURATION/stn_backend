package sn.stn.facturation.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.domain.Mouvement;
import sn.stn.facturation.repository.MouvementRepository;
import sn.stn.facturation.service.MouvementService;
import sn.stn.facturation.service.dto.MouvementDTO;
import sn.stn.facturation.service.mapper.MouvementMapper;

/**
 * Service Implementation for managing
 * {@link sn.stn.facturation.domain.Mouvement}.
 */
@Service
@Transactional
public class MouvementServiceImpl implements MouvementService {

    private static final Logger LOG = LoggerFactory.getLogger(MouvementServiceImpl.class);

    private final MouvementRepository mouvementRepository;

    private final MouvementMapper mouvementMapper;

    private final sn.stn.facturation.repository.FactureRepository factureRepository;

    private final sn.stn.facturation.service.BillingService billingService;

    public MouvementServiceImpl(
            MouvementRepository mouvementRepository,
            MouvementMapper mouvementMapper,
            sn.stn.facturation.repository.FactureRepository factureRepository,
            sn.stn.facturation.service.BillingService billingService) {
        this.mouvementRepository = mouvementRepository;
        this.mouvementMapper = mouvementMapper;
        this.factureRepository = factureRepository;
        this.billingService = billingService;
    }

    @Override
    public MouvementDTO save(MouvementDTO mouvementDTO) {
        LOG.debug("Request to save Mouvement : {}", mouvementDTO);
        Mouvement mouvement = mouvementMapper.toEntity(mouvementDTO);

        if (mouvement.getFacture() != null && mouvement.getFacture().getId() != null) {
            final Mouvement m = mouvement;
            factureRepository
                    .findOneWithAllRelationships(mouvement.getFacture().getId())
                    .ifPresent(facture -> {
                        m.setMontantCalcule(billingService.calculateAmount(m, facture.getNavire()));
                        m.setLibelle(billingService.generateLibelle(m, facture.getNavire()));

                        // We need to save the mouvement first then recalculate facture
                    });
        }

        mouvement = mouvementRepository.save(mouvement);

        // Refresh and recalculate facture
        updateFactureTotals(mouvement);

        return mouvementMapper.toDto(mouvement);
    }

    @Override
    public MouvementDTO update(MouvementDTO mouvementDTO) {
        LOG.debug("Request to update Mouvement : {}", mouvementDTO);
        Mouvement mouvement = mouvementMapper.toEntity(mouvementDTO);

        if (mouvement.getFacture() != null && mouvement.getFacture().getId() != null) {
            final Mouvement m = mouvement;
            factureRepository
                    .findOneWithAllRelationships(mouvement.getFacture().getId())
                    .ifPresent(facture -> {
                        m.setMontantCalcule(billingService.calculateAmount(m, facture.getNavire()));
                        m.setLibelle(billingService.generateLibelle(m, facture.getNavire()));
                    });
        }

        mouvement = mouvementRepository.save(mouvement);
        updateFactureTotals(mouvement);

        return mouvementMapper.toDto(mouvement);
    }

    @Override
    public Optional<MouvementDTO> partialUpdate(MouvementDTO mouvementDTO) {
        LOG.debug("Request to partially update Mouvement : {}", mouvementDTO);

        return mouvementRepository
                .findById(mouvementDTO.getId())
                .map(existingMouvement -> {
                    mouvementMapper.partialUpdate(existingMouvement, mouvementDTO);

                    if (existingMouvement.getFacture() != null && existingMouvement.getFacture().getId() != null) {
                        factureRepository
                                .findOneWithAllRelationships(existingMouvement.getFacture().getId())
                                .ifPresent(facture -> {
                                    existingMouvement.setMontantCalcule(
                                            billingService.calculateAmount(existingMouvement, facture.getNavire()));
                                    existingMouvement.setLibelle(
                                            billingService.generateLibelle(existingMouvement, facture.getNavire()));
                                });
                    }

                    return existingMouvement;
                })
                .map(mouvementRepository::save)
                .map(savedMouvement -> {
                    updateFactureTotals(savedMouvement);
                    return savedMouvement;
                })
                .map(mouvementMapper::toDto);
    }

    private void updateFactureTotals(Mouvement mouvement) {
        if (mouvement.getFacture() != null && mouvement.getFacture().getId() != null) {
            factureRepository
                    .findOneWithAllRelationships(mouvement.getFacture().getId())
                    .ifPresent(facture -> {
                        billingService.recalculateTotals(facture);
                        factureRepository.save(facture);
                    });
        }
    }

    public Page<MouvementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return mouvementRepository.findAllWithEagerRelationships(pageable).map(mouvementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MouvementDTO> findOne(Long id) {
        LOG.debug("Request to get Mouvement : {}", id);
        return mouvementRepository.findOneWithEagerRelationships(id).map(mouvementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Mouvement : {}", id);
        mouvementRepository.deleteById(id);
    }
}
