package sn.stn.facturation.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.domain.*; // for static metamodels
import sn.stn.facturation.domain.Facture;
import sn.stn.facturation.repository.FactureRepository;
import sn.stn.facturation.service.criteria.FactureCriteria;
import sn.stn.facturation.service.dto.FactureDTO;
import sn.stn.facturation.service.mapper.FactureMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Facture} entities in the database.
 * The main input is a {@link FactureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FactureDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FactureQueryService extends QueryService<Facture> {

    private static final Logger LOG = LoggerFactory.getLogger(FactureQueryService.class);

    private final FactureRepository factureRepository;

    private final FactureMapper factureMapper;

    public FactureQueryService(FactureRepository factureRepository, FactureMapper factureMapper) {
        this.factureRepository = factureRepository;
        this.factureMapper = factureMapper;
    }

    /**
     * Return a {@link Page} of {@link FactureDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FactureDTO> findByCriteria(FactureCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Facture> specification = createSpecification(criteria);
        return factureRepository.findAll(specification, page).map(factureMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FactureCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Facture> specification = createSpecification(criteria);
        return factureRepository.count(specification);
    }

    /**
     * Function to convert {@link FactureCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Facture> createSpecification(FactureCriteria criteria) {
        Specification<Facture> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Facture_.id),
                buildStringSpecification(criteria.getNumero(), Facture_.numero),
                buildRangeSpecification(criteria.getDateEmission(), Facture_.dateEmission),
                buildRangeSpecification(criteria.getDatePaiement(), Facture_.datePaiement),
                buildRangeSpecification(criteria.getVolumeM3(), Facture_.volumeM3),
                buildRangeSpecification(criteria.getMontantBaseHt(), Facture_.montantBaseHt),
                buildRangeSpecification(criteria.getMontantSupplementsHt(), Facture_.montantSupplementsHt),
                buildRangeSpecification(criteria.getMontantHt(), Facture_.montantHt),
                buildRangeSpecification(criteria.getTauxTva(), Facture_.tauxTva),
                buildRangeSpecification(criteria.getMontantTva(), Facture_.montantTva),
                buildRangeSpecification(criteria.getMontantTtc(), Facture_.montantTtc),
                buildSpecification(criteria.getDevise(), Facture_.devise),
                buildRangeSpecification(criteria.getTauxChangeCfa(), Facture_.tauxChangeCfa),
                buildSpecification(criteria.getStatut(), Facture_.statut),
                buildStringSpecification(criteria.getNotes(), Facture_.notes),
                buildStringSpecification(criteria.getCheminPdf(), Facture_.cheminPdf),
                buildStringSpecification(criteria.getCreeParLogin(), Facture_.creeParLogin),
                buildSpecification(criteria.getSupplementsId(), root ->
                    root.join(Facture_.supplements, JoinType.LEFT).get(LigneFactureSupplement_.id)
                ),
                buildSpecification(criteria.getNavireId(), root -> root.join(Facture_.navire, JoinType.LEFT).get(Navire_.id)),
                buildSpecification(criteria.getClientId(), root -> root.join(Facture_.client, JoinType.LEFT).get(Client_.id))
            );
        }
        return specification;
    }
}
