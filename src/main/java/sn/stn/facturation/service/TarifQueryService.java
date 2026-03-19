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
import sn.stn.facturation.domain.Tarif;
import sn.stn.facturation.repository.TarifRepository;
import sn.stn.facturation.service.criteria.TarifCriteria;
import sn.stn.facturation.service.dto.TarifDTO;
import sn.stn.facturation.service.mapper.TarifMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Tarif} entities in the database.
 * The main input is a {@link TarifCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TarifDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TarifQueryService extends QueryService<Tarif> {

    private static final Logger LOG = LoggerFactory.getLogger(TarifQueryService.class);

    private final TarifRepository tarifRepository;

    private final TarifMapper tarifMapper;

    public TarifQueryService(TarifRepository tarifRepository, TarifMapper tarifMapper) {
        this.tarifRepository = tarifRepository;
        this.tarifMapper = tarifMapper;
    }

    /**
     * Return a {@link Page} of {@link TarifDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TarifDTO> findByCriteria(TarifCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tarif> specification = createSpecification(criteria);
        return tarifRepository.findAll(specification, page).map(tarifMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TarifCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Tarif> specification = createSpecification(criteria);
        return tarifRepository.count(specification);
    }

    /**
     * Function to convert {@link TarifCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tarif> createSpecification(TarifCriteria criteria) {
        Specification<Tarif> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Tarif_.id),
                buildRangeSpecification(criteria.getTrancheMin(), Tarif_.trancheMin),
                buildRangeSpecification(criteria.getTrancheMax(), Tarif_.trancheMax),
                buildRangeSpecification(criteria.getPrixEuro(), Tarif_.prixEuro),
                buildSpecification(criteria.getActif(), Tarif_.actif),
                buildRangeSpecification(criteria.getDateDebut(), Tarif_.dateDebut),
                buildRangeSpecification(criteria.getDateFin(), Tarif_.dateFin),
                buildStringSpecification(criteria.getDescription(), Tarif_.description),
                buildSpecification(criteria.getHistoriquesId(), root ->
                    root.join(Tarif_.historiques, JoinType.LEFT).get(HistoriqueTarif_.id)
                )
            );
        }
        return specification;
    }
}
