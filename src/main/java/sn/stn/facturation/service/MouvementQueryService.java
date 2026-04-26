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
import sn.stn.facturation.domain.Mouvement;
import sn.stn.facturation.repository.MouvementRepository;
import sn.stn.facturation.service.criteria.MouvementCriteria;
import sn.stn.facturation.service.dto.MouvementDTO;
import sn.stn.facturation.service.mapper.MouvementMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Mouvement} entities in the database.
 * The main input is a {@link MouvementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MouvementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MouvementQueryService extends QueryService<Mouvement> {

    private static final Logger LOG = LoggerFactory.getLogger(MouvementQueryService.class);

    private final MouvementRepository mouvementRepository;

    private final MouvementMapper mouvementMapper;

    public MouvementQueryService(MouvementRepository mouvementRepository, MouvementMapper mouvementMapper) {
        this.mouvementRepository = mouvementRepository;
        this.mouvementMapper = mouvementMapper;
    }

    /**
     * Return a {@link Page} of {@link MouvementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MouvementDTO> findByCriteria(MouvementCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Mouvement> specification = createSpecification(criteria);
        return mouvementRepository.findAll(specification, page).map(mouvementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MouvementCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Mouvement> specification = createSpecification(criteria);
        return mouvementRepository.count(specification);
    }

    /**
     * Function to convert {@link MouvementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Mouvement> createSpecification(MouvementCriteria criteria) {
        Specification<Mouvement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Mouvement_.id),
                buildSpecification(criteria.getType(), Mouvement_.type),
                buildStringSpecification(criteria.getPosteA(), Mouvement_.posteA),
                buildStringSpecification(criteria.getPosteB(), Mouvement_.posteB),
                buildRangeSpecification(criteria.getDuree(), Mouvement_.duree),
                buildRangeSpecification(criteria.getMontantCalcule(), Mouvement_.montantCalcule),
                buildStringSpecification(criteria.getLibelle(), Mouvement_.libelle),
                buildSpecification(criteria.getRemorqueurId(), root -> root.join(Mouvement_.remorqueur, JoinType.LEFT).get(Remorqueur_.id)),
                buildSpecification(criteria.getFactureId(), root -> root.join(Mouvement_.facture, JoinType.LEFT).get(Facture_.id))
            );
        }
        return specification;
    }
}
