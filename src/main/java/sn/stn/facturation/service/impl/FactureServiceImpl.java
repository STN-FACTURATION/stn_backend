package sn.stn.facturation.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.domain.Facture;
import sn.stn.facturation.domain.enumeration.StatutFacture;
import sn.stn.facturation.repository.FactureRepository;
import sn.stn.facturation.service.FactureService;
import sn.stn.facturation.service.dto.FactureDTO;
import sn.stn.facturation.service.mapper.FactureMapper;
import sn.stn.facturation.repository.LigneFactureSupplementRepository;
import sn.stn.facturation.service.dto.LigneFactureSupplementDTO;

/**
 * Service Implementation for managing
 * {@link sn.stn.facturation.domain.Facture}.
 */
@Service
@Transactional
public class FactureServiceImpl implements FactureService {

    private static final Logger LOG = LoggerFactory.getLogger(FactureServiceImpl.class);

    private final FactureRepository factureRepository;

    private final FactureMapper factureMapper;

    private final sn.stn.facturation.repository.NavireRepository navireRepository;

    private final sn.stn.facturation.service.BillingService billingService;

    private final LigneFactureSupplementRepository ligneFactureSupplementRepository;

    public FactureServiceImpl(
            FactureRepository factureRepository,
            FactureMapper factureMapper,
            sn.stn.facturation.repository.NavireRepository navireRepository,
            sn.stn.facturation.service.BillingService billingService,
            LigneFactureSupplementRepository ligneFactureSupplementRepository) {
        this.factureRepository = factureRepository;
        this.factureMapper = factureMapper;
        this.navireRepository = navireRepository;
        this.billingService = billingService;
        this.ligneFactureSupplementRepository = ligneFactureSupplementRepository;
    }

    @Override
    public FactureDTO save(FactureDTO factureDTO) {
        LOG.debug("Request to save Facture : {}", factureDTO);
        Facture facture = factureMapper.toEntity(factureDTO);

        if (facture.getId() == null && (facture.getNumero() == null || facture.getNumero().isBlank())) {
            facture.setNumero("FACT-" + java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        }

        if (facture.getId() == null) {
            facture.setDateEmission(java.time.LocalDate.now());
        }

        if (facture.getVolumeM3() == null && facture.getNavire() != null && facture.getNavire().getId() != null) {
            final Facture f = facture;
            navireRepository.findById(f.getNavire().getId()).ifPresent(navire -> f.setVolumeM3(navire.getJaugeBrute()));
        }

        billingService.recalculateTotals(facture);
        Facture savedFacture = factureRepository.save(facture);
        FactureDTO savedDTO = factureMapper.toDto(savedFacture);

        return savedDTO;
    }

    @Override
    public FactureDTO update(FactureDTO factureDTO) {
        LOG.debug("Request to update Facture : {}", factureDTO);
        Facture facture = factureMapper.toEntity(factureDTO);

        billingService.recalculateTotals(facture);
        Facture savedFacture = factureRepository.save(facture);

        FactureDTO savedDTO = factureMapper.toDto(savedFacture);

        return savedDTO;
    }

    @Override
    public Optional<FactureDTO> partialUpdate(FactureDTO factureDTO) {
        LOG.debug("Request to partially update Facture : {}", factureDTO);

        return factureRepository
                .findById(factureDTO.getId())
                .map(existingFacture -> {
                    factureMapper.partialUpdate(existingFacture, factureDTO);
                    billingService.recalculateTotals(existingFacture);
                    return existingFacture;
                })
                .map(factureRepository::save)
                .map(factureMapper::toDto);
    }

    @Override
    public Optional<FactureDTO> updateStatut(Long id, StatutFacture statut) {
        LOG.debug("Request to update statut of Facture : {} to {}", id, statut);
        return factureRepository.findById(id).map(facture -> {
            facture.setStatut(statut);
            if (statut == StatutFacture.PAYEE) {
                facture.setDatePaiement(java.time.LocalDate.now());
            } else if (statut == StatutFacture.BROUILLON
                    || statut == StatutFacture.VALIDEE) {
                facture.setDatePaiement(null);
            }
            return factureRepository.save(facture);
        }).map(factureMapper::toDto);
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
        ligneFactureSupplementRepository.deleteByFactureId(id);
        factureRepository.deleteById(id);
    }
}
