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
import sn.stn.facturation.domain.LigneFactureSupplement;
import sn.stn.facturation.repository.LigneFactureSupplementRepository;
import sn.stn.facturation.service.criteria.LigneFactureSupplementCriteria;
import sn.stn.facturation.service.dto.LigneFactureSupplementDTO;
import sn.stn.facturation.service.mapper.LigneFactureSupplementMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LigneFactureSupplement} entities in the database.
 * The main input is a {@link LigneFactureSupplementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link LigneFactureSupplementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LigneFactureSupplementQueryService extends QueryService<LigneFactureSupplement> {

    private static final Logger LOG = LoggerFactory.getLogger(LigneFactureSupplementQueryService.class);

    private final LigneFactureSupplementRepository ligneFactureSupplementRepository;

    private final LigneFactureSupplementMapper ligneFactureSupplementMapper;

    public LigneFactureSupplementQueryService(
        LigneFactureSupplementRepository ligneFactureSupplementRepository,
        LigneFactureSupplementMapper ligneFactureSupplementMapper
    ) {
        this.ligneFactureSupplementRepository = ligneFactureSupplementRepository;
        this.ligneFactureSupplementMapper = ligneFactureSupplementMapper;
    }

    /**
     * Return a {@link Page} of {@link LigneFactureSupplementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LigneFactureSupplementDTO> findByCriteria(LigneFactureSupplementCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LigneFactureSupplement> specification = createSpecification(criteria);
        return ligneFactureSupplementRepository.findAll(specification, page).map(ligneFactureSupplementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LigneFactureSupplementCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<LigneFactureSupplement> specification = createSpecification(criteria);
        return ligneFactureSupplementRepository.count(specification);
    }

    /**
     * Function to convert {@link LigneFactureSupplementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LigneFactureSupplement> createSpecification(LigneFactureSupplementCriteria criteria) {
        Specification<LigneFactureSupplement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), LigneFactureSupplement_.id),
                buildRangeSpecification(criteria.getMontantCalcule(), LigneFactureSupplement_.montantCalcule),
                buildStringSpecification(criteria.getDescription(), LigneFactureSupplement_.description),
                buildSpecification(criteria.getSupplementId(), root ->
                    root.join(LigneFactureSupplement_.supplement, JoinType.LEFT).get(Supplement_.id)
                ),
                buildSpecification(criteria.getFactureId(), root ->
                    root.join(LigneFactureSupplement_.facture, JoinType.LEFT).get(Facture_.id)
                )
            );
        }
        return specification;
    }
}
