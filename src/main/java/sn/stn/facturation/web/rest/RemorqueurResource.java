package sn.stn.facturation.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.stn.facturation.repository.RemorqueurRepository;
import sn.stn.facturation.service.RemorqueurQueryService;
import sn.stn.facturation.service.RemorqueurService;
import sn.stn.facturation.service.criteria.RemorqueurCriteria;
import sn.stn.facturation.service.dto.RemorqueurDTO;
import sn.stn.facturation.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.stn.facturation.domain.Remorqueur}.
 */
@RestController
@RequestMapping("/api/remorqueurs")
public class RemorqueurResource {

    private static final Logger LOG = LoggerFactory.getLogger(RemorqueurResource.class);

    private static final String ENTITY_NAME = "stnBackendRemorqueur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RemorqueurService remorqueurService;

    private final RemorqueurRepository remorqueurRepository;

    private final RemorqueurQueryService remorqueurQueryService;

    public RemorqueurResource(
        RemorqueurService remorqueurService,
        RemorqueurRepository remorqueurRepository,
        RemorqueurQueryService remorqueurQueryService
    ) {
        this.remorqueurService = remorqueurService;
        this.remorqueurRepository = remorqueurRepository;
        this.remorqueurQueryService = remorqueurQueryService;
    }

    /**
     * {@code POST  /remorqueurs} : Create a new remorqueur.
     *
     * @param remorqueurDTO the remorqueurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new remorqueurDTO, or with status {@code 400 (Bad Request)} if the remorqueur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RemorqueurDTO> createRemorqueur(@Valid @RequestBody RemorqueurDTO remorqueurDTO) throws URISyntaxException {
        LOG.debug("REST request to save Remorqueur : {}", remorqueurDTO);
        if (remorqueurDTO.getId() != null) {
            throw new BadRequestAlertException("A new remorqueur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        remorqueurDTO = remorqueurService.save(remorqueurDTO);
        return ResponseEntity.created(new URI("/api/remorqueurs/" + remorqueurDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, remorqueurDTO.getId().toString()))
            .body(remorqueurDTO);
    }

    /**
     * {@code PUT  /remorqueurs/:id} : Updates an existing remorqueur.
     *
     * @param id the id of the remorqueurDTO to save.
     * @param remorqueurDTO the remorqueurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated remorqueurDTO,
     * or with status {@code 400 (Bad Request)} if the remorqueurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the remorqueurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RemorqueurDTO> updateRemorqueur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RemorqueurDTO remorqueurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Remorqueur : {}, {}", id, remorqueurDTO);
        if (remorqueurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, remorqueurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!remorqueurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        remorqueurDTO = remorqueurService.update(remorqueurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, remorqueurDTO.getId().toString()))
            .body(remorqueurDTO);
    }

    /**
     * {@code PATCH  /remorqueurs/:id} : Partial updates given fields of an existing remorqueur, field will ignore if it is null
     *
     * @param id the id of the remorqueurDTO to save.
     * @param remorqueurDTO the remorqueurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated remorqueurDTO,
     * or with status {@code 400 (Bad Request)} if the remorqueurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the remorqueurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the remorqueurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RemorqueurDTO> partialUpdateRemorqueur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RemorqueurDTO remorqueurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Remorqueur partially : {}, {}", id, remorqueurDTO);
        if (remorqueurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, remorqueurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!remorqueurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RemorqueurDTO> result = remorqueurService.partialUpdate(remorqueurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, remorqueurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /remorqueurs} : get all the remorqueurs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of remorqueurs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RemorqueurDTO>> getAllRemorqueurs(
        RemorqueurCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Remorqueurs by criteria: {}", criteria);

        Page<RemorqueurDTO> page = remorqueurQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /remorqueurs/count} : count all the remorqueurs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRemorqueurs(RemorqueurCriteria criteria) {
        LOG.debug("REST request to count Remorqueurs by criteria: {}", criteria);
        return ResponseEntity.ok().body(remorqueurQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /remorqueurs/:id} : get the "id" remorqueur.
     *
     * @param id the id of the remorqueurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the remorqueurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RemorqueurDTO> getRemorqueur(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Remorqueur : {}", id);
        Optional<RemorqueurDTO> remorqueurDTO = remorqueurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(remorqueurDTO);
    }

    /**
     * {@code DELETE  /remorqueurs/:id} : delete the "id" remorqueur.
     *
     * @param id the id of the remorqueurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRemorqueur(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Remorqueur : {}", id);
        remorqueurService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
