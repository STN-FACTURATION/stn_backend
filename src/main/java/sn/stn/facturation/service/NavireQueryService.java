package sn.stn.facturation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.domain.*; // for static metamodels
import sn.stn.facturation.domain.Navire;
import sn.stn.facturation.repository.NavireRepository;
import sn.stn.facturation.service.criteria.NavireCriteria;
import sn.stn.facturation.service.dto.NavireDTO;
import sn.stn.facturation.service.mapper.NavireMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Navire} entities in the database.
 * The main input is a {@link NavireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NavireDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NavireQueryService extends QueryService<Navire> {

    private static final Logger LOG = LoggerFactory.getLogger(NavireQueryService.class);

    private final NavireRepository navireRepository;

    private final NavireMapper navireMapper;

    public NavireQueryService(NavireRepository navireRepository, NavireMapper navireMapper) {
        this.navireRepository = navireRepository;
        this.navireMapper = navireMapper;
    }

    /**
     * Return a {@link Page} of {@link NavireDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NavireDTO> findByCriteria(NavireCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Navire> specification = createSpecification(criteria);
        return navireRepository.findAll(specification, page).map(navireMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NavireCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Navire> specification = createSpecification(criteria);
        return navireRepository.count(specification);
    }

    /**
     * Function to convert {@link NavireCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Navire> createSpecification(NavireCriteria criteria) {
        Specification<Navire> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Navire_.id),
                buildStringSpecification(criteria.getNom(), Navire_.nom),
                buildStringSpecification(criteria.getNumeroImo(), Navire_.numeroImo),
                buildRangeSpecification(criteria.getJaugeBrute(), Navire_.jaugeBrute),
                buildRangeSpecification(criteria.getLongueur(), Navire_.longueur),
                buildRangeSpecification(criteria.getLargeur(), Navire_.largeur),
                buildRangeSpecification(criteria.getTirant(), Navire_.tirant),
                buildStringSpecification(criteria.getPavillon(), Navire_.pavillon),
                buildSpecification(criteria.getActif(), Navire_.actif)
            );
        }
        return specification;
    }
}
