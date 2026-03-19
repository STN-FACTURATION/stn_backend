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
import sn.stn.facturation.repository.LigneFactureSupplementRepository;
import sn.stn.facturation.service.LigneFactureSupplementQueryService;
import sn.stn.facturation.service.LigneFactureSupplementService;
import sn.stn.facturation.service.criteria.LigneFactureSupplementCriteria;
import sn.stn.facturation.service.dto.LigneFactureSupplementDTO;
import sn.stn.facturation.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.stn.facturation.domain.LigneFactureSupplement}.
 */
@RestController
@RequestMapping("/api/ligne-facture-supplements")
public class LigneFactureSupplementResource {

    private static final Logger LOG = LoggerFactory.getLogger(LigneFactureSupplementResource.class);

    private static final String ENTITY_NAME = "stnBackendLigneFactureSupplement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigneFactureSupplementService ligneFactureSupplementService;

    private final LigneFactureSupplementRepository ligneFactureSupplementRepository;

    private final LigneFactureSupplementQueryService ligneFactureSupplementQueryService;

    public LigneFactureSupplementResource(
        LigneFactureSupplementService ligneFactureSupplementService,
        LigneFactureSupplementRepository ligneFactureSupplementRepository,
        LigneFactureSupplementQueryService ligneFactureSupplementQueryService
    ) {
        this.ligneFactureSupplementService = ligneFactureSupplementService;
        this.ligneFactureSupplementRepository = ligneFactureSupplementRepository;
        this.ligneFactureSupplementQueryService = ligneFactureSupplementQueryService;
    }

    /**
     * {@code POST  /ligne-facture-supplements} : Create a new ligneFactureSupplement.
     *
     * @param ligneFactureSupplementDTO the ligneFactureSupplementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ligneFactureSupplementDTO, or with status {@code 400 (Bad Request)} if the ligneFactureSupplement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LigneFactureSupplementDTO> createLigneFactureSupplement(
        @Valid @RequestBody LigneFactureSupplementDTO ligneFactureSupplementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save LigneFactureSupplement : {}", ligneFactureSupplementDTO);
        if (ligneFactureSupplementDTO.getId() != null) {
            throw new BadRequestAlertException("A new ligneFactureSupplement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ligneFactureSupplementDTO = ligneFactureSupplementService.save(ligneFactureSupplementDTO);
        return ResponseEntity.created(new URI("/api/ligne-facture-supplements/" + ligneFactureSupplementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ligneFactureSupplementDTO.getId().toString()))
            .body(ligneFactureSupplementDTO);
    }

    /**
     * {@code PUT  /ligne-facture-supplements/:id} : Updates an existing ligneFactureSupplement.
     *
     * @param id the id of the ligneFactureSupplementDTO to save.
     * @param ligneFactureSupplementDTO the ligneFactureSupplementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneFactureSupplementDTO,
     * or with status {@code 400 (Bad Request)} if the ligneFactureSupplementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ligneFactureSupplementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LigneFactureSupplementDTO> updateLigneFactureSupplement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LigneFactureSupplementDTO ligneFactureSupplementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LigneFactureSupplement : {}, {}", id, ligneFactureSupplementDTO);
        if (ligneFactureSupplementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneFactureSupplementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneFactureSupplementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ligneFactureSupplementDTO = ligneFactureSupplementService.update(ligneFactureSupplementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneFactureSupplementDTO.getId().toString()))
            .body(ligneFactureSupplementDTO);
    }

    /**
     * {@code PATCH  /ligne-facture-supplements/:id} : Partial updates given fields of an existing ligneFactureSupplement, field will ignore if it is null
     *
     * @param id the id of the ligneFactureSupplementDTO to save.
     * @param ligneFactureSupplementDTO the ligneFactureSupplementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneFactureSupplementDTO,
     * or with status {@code 400 (Bad Request)} if the ligneFactureSupplementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ligneFactureSupplementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ligneFactureSupplementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LigneFactureSupplementDTO> partialUpdateLigneFactureSupplement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LigneFactureSupplementDTO ligneFactureSupplementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LigneFactureSupplement partially : {}, {}", id, ligneFactureSupplementDTO);
        if (ligneFactureSupplementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneFactureSupplementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneFactureSupplementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LigneFactureSupplementDTO> result = ligneFactureSupplementService.partialUpdate(ligneFactureSupplementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneFactureSupplementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ligne-facture-supplements} : get all the ligneFactureSupplements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ligneFactureSupplements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LigneFactureSupplementDTO>> getAllLigneFactureSupplements(
        LigneFactureSupplementCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get LigneFactureSupplements by criteria: {}", criteria);

        Page<LigneFactureSupplementDTO> page = ligneFactureSupplementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ligne-facture-supplements/count} : count all the ligneFactureSupplements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLigneFactureSupplements(LigneFactureSupplementCriteria criteria) {
        LOG.debug("REST request to count LigneFactureSupplements by criteria: {}", criteria);
        return ResponseEntity.ok().body(ligneFactureSupplementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ligne-facture-supplements/:id} : get the "id" ligneFactureSupplement.
     *
     * @param id the id of the ligneFactureSupplementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ligneFactureSupplementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LigneFactureSupplementDTO> getLigneFactureSupplement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LigneFactureSupplement : {}", id);
        Optional<LigneFactureSupplementDTO> ligneFactureSupplementDTO = ligneFactureSupplementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ligneFactureSupplementDTO);
    }

    /**
     * {@code DELETE  /ligne-facture-supplements/:id} : delete the "id" ligneFactureSupplement.
     *
     * @param id the id of the ligneFactureSupplementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLigneFactureSupplement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LigneFactureSupplement : {}", id);
        ligneFactureSupplementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
