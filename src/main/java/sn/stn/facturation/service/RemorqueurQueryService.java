package sn.stn.facturation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.domain.*; // for static metamodels
import sn.stn.facturation.domain.Remorqueur;
import sn.stn.facturation.repository.RemorqueurRepository;
import sn.stn.facturation.service.criteria.RemorqueurCriteria;
import sn.stn.facturation.service.dto.RemorqueurDTO;
import sn.stn.facturation.service.mapper.RemorqueurMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Remorqueur} entities in the database.
 * The main input is a {@link RemorqueurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RemorqueurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RemorqueurQueryService extends QueryService<Remorqueur> {

    private static final Logger LOG = LoggerFactory.getLogger(RemorqueurQueryService.class);

    private final RemorqueurRepository remorqueurRepository;

    private final RemorqueurMapper remorqueurMapper;

    public RemorqueurQueryService(RemorqueurRepository remorqueurRepository, RemorqueurMapper remorqueurMapper) {
        this.remorqueurRepository = remorqueurRepository;
        this.remorqueurMapper = remorqueurMapper;
    }

    /**
     * Return a {@link Page} of {@link RemorqueurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RemorqueurDTO> findByCriteria(RemorqueurCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Remorqueur> specification = createSpecification(criteria);
        return remorqueurRepository.findAll(specification, page).map(remorqueurMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RemorqueurCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Remorqueur> specification = createSpecification(criteria);
        return remorqueurRepository.count(specification);
    }

    /**
     * Function to convert {@link RemorqueurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Remorqueur> createSpecification(RemorqueurCriteria criteria) {
        Specification<Remorqueur> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Remorqueur_.id),
                buildStringSpecification(criteria.getCode(), Remorqueur_.code),
                buildStringSpecification(criteria.getNom(), Remorqueur_.nom),
                buildStringSpecification(criteria.getStatut(), Remorqueur_.statut),
                buildStringSpecification(criteria.getObservation(), Remorqueur_.observation)
            );
        }
        return specification;
    }
}
