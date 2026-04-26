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
import sn.stn.facturation.repository.MouvementRepository;
import sn.stn.facturation.service.MouvementQueryService;
import sn.stn.facturation.service.MouvementService;
import sn.stn.facturation.service.criteria.MouvementCriteria;
import sn.stn.facturation.service.dto.MouvementDTO;
import sn.stn.facturation.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.stn.facturation.domain.Mouvement}.
 */
@RestController
@RequestMapping("/api/mouvements")
public class MouvementResource {

    private static final Logger LOG = LoggerFactory.getLogger(MouvementResource.class);

    private static final String ENTITY_NAME = "mouvement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MouvementService mouvementService;

    private final MouvementRepository mouvementRepository;

    private final MouvementQueryService mouvementQueryService;

    public MouvementResource(
        MouvementService mouvementService,
        MouvementRepository mouvementRepository,
        MouvementQueryService mouvementQueryService
    ) {
        this.mouvementService = mouvementService;
        this.mouvementRepository = mouvementRepository;
        this.mouvementQueryService = mouvementQueryService;
    }

    /**
     * {@code POST  /mouvements} : Create a new mouvement.
     *
     * @param mouvementDTO the mouvementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mouvementDTO, or with status {@code 400 (Bad Request)} if the mouvement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MouvementDTO> createMouvement(@Valid @RequestBody MouvementDTO mouvementDTO) throws URISyntaxException {
        LOG.debug("REST request to save Mouvement : {}", mouvementDTO);
        if (mouvementDTO.getId() != null) {
            throw new BadRequestAlertException("A new mouvement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mouvementDTO = mouvementService.save(mouvementDTO);
        return ResponseEntity.created(new URI("/api/mouvements/" + mouvementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, mouvementDTO.getId().toString()))
            .body(mouvementDTO);
    }

    /**
     * {@code PUT  /mouvements/:id} : Updates an existing mouvement.
     *
     * @param id the id of the mouvementDTO to save.
     * @param mouvementDTO the mouvementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mouvementDTO,
     * or with status {@code 400 (Bad Request)} if the mouvementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mouvementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MouvementDTO> updateMouvement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MouvementDTO mouvementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Mouvement : {}, {}", id, mouvementDTO);
        if (mouvementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mouvementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mouvementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mouvementDTO = mouvementService.update(mouvementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mouvementDTO.getId().toString()))
            .body(mouvementDTO);
    }

    /**
     * {@code PATCH  /mouvements/:id} : Partial updates given fields of an existing mouvement, field will ignore if it is null
     *
     * @param id the id of the mouvementDTO to save.
     * @param mouvementDTO the mouvementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mouvementDTO,
     * or with status {@code 400 (Bad Request)} if the mouvementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mouvementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mouvementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MouvementDTO> partialUpdateMouvement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MouvementDTO mouvementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Mouvement partially : {}, {}", id, mouvementDTO);
        if (mouvementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mouvementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mouvementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MouvementDTO> result = mouvementService.partialUpdate(mouvementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mouvementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mouvements} : get all the mouvements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mouvements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MouvementDTO>> getAllMouvements(
        MouvementCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Mouvements by criteria: {}", criteria);

        Page<MouvementDTO> page = mouvementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mouvements/count} : count all the mouvements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMouvements(MouvementCriteria criteria) {
        LOG.debug("REST request to count Mouvements by criteria: {}", criteria);
        return ResponseEntity.ok().body(mouvementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mouvements/:id} : get the "id" mouvement.
     *
     * @param id the id of the mouvementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mouvementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MouvementDTO> getMouvement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Mouvement : {}", id);
        Optional<MouvementDTO> mouvementDTO = mouvementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mouvementDTO);
    }

    /**
     * {@code DELETE  /mouvements/:id} : delete the "id" mouvement.
     *
     * @param id the id of the mouvementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMouvement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Mouvement : {}", id);
        mouvementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
