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
import sn.stn.facturation.domain.HistoriqueTarif;
import sn.stn.facturation.repository.HistoriqueTarifRepository;
import sn.stn.facturation.service.criteria.HistoriqueTarifCriteria;
import sn.stn.facturation.service.dto.HistoriqueTarifDTO;
import sn.stn.facturation.service.mapper.HistoriqueTarifMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HistoriqueTarif} entities in the database.
 * The main input is a {@link HistoriqueTarifCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HistoriqueTarifDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoriqueTarifQueryService extends QueryService<HistoriqueTarif> {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueTarifQueryService.class);

    private final HistoriqueTarifRepository historiqueTarifRepository;

    private final HistoriqueTarifMapper historiqueTarifMapper;

    public HistoriqueTarifQueryService(HistoriqueTarifRepository historiqueTarifRepository, HistoriqueTarifMapper historiqueTarifMapper) {
        this.historiqueTarifRepository = historiqueTarifRepository;
        this.historiqueTarifMapper = historiqueTarifMapper;
    }

    /**
     * Return a {@link Page} of {@link HistoriqueTarifDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoriqueTarifDTO> findByCriteria(HistoriqueTarifCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HistoriqueTarif> specification = createSpecification(criteria);
        return historiqueTarifRepository.findAll(specification, page).map(historiqueTarifMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistoriqueTarifCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HistoriqueTarif> specification = createSpecification(criteria);
        return historiqueTarifRepository.count(specification);
    }

    /**
     * Function to convert {@link HistoriqueTarifCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HistoriqueTarif> createSpecification(HistoriqueTarifCriteria criteria) {
        Specification<HistoriqueTarif> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), HistoriqueTarif_.id),
                buildRangeSpecification(criteria.getAncienneValeur(), HistoriqueTarif_.ancienneValeur),
                buildRangeSpecification(criteria.getNouvelleValeur(), HistoriqueTarif_.nouvelleValeur),
                buildRangeSpecification(criteria.getDateModification(), HistoriqueTarif_.dateModification),
                buildStringSpecification(criteria.getCommentaire(), HistoriqueTarif_.commentaire),
                buildStringSpecification(criteria.getModifieParLogin(), HistoriqueTarif_.modifieParLogin),
                buildSpecification(criteria.getTarifId(), root -> root.join(HistoriqueTarif_.tarif, JoinType.LEFT).get(Tarif_.id))
            );
        }
        return specification;
    }
}
