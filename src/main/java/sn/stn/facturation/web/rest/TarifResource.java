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
import sn.stn.facturation.repository.TarifRepository;
import sn.stn.facturation.service.TarifQueryService;
import sn.stn.facturation.service.TarifService;
import sn.stn.facturation.service.criteria.TarifCriteria;
import sn.stn.facturation.service.dto.TarifDTO;
import sn.stn.facturation.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.stn.facturation.domain.Tarif}.
 */
@RestController
@RequestMapping("/api/tarifs")
public class TarifResource {

    private static final Logger LOG = LoggerFactory.getLogger(TarifResource.class);

    private static final String ENTITY_NAME = "stnBackendTarif";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TarifService tarifService;

    private final TarifRepository tarifRepository;

    private final TarifQueryService tarifQueryService;

    public TarifResource(TarifService tarifService, TarifRepository tarifRepository, TarifQueryService tarifQueryService) {
        this.tarifService = tarifService;
        this.tarifRepository = tarifRepository;
        this.tarifQueryService = tarifQueryService;
    }

    /**
     * {@code POST  /tarifs} : Create a new tarif.
     *
     * @param tarifDTO the tarifDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tarifDTO, or with status {@code 400 (Bad Request)} if the tarif has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TarifDTO> createTarif(@Valid @RequestBody TarifDTO tarifDTO) throws URISyntaxException {
        LOG.debug("REST request to save Tarif : {}", tarifDTO);
        if (tarifDTO.getId() != null) {
            throw new BadRequestAlertException("A new tarif cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tarifDTO = tarifService.save(tarifDTO);
        return ResponseEntity.created(new URI("/api/tarifs/" + tarifDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tarifDTO.getId().toString()))
            .body(tarifDTO);
    }

    /**
     * {@code PUT  /tarifs/:id} : Updates an existing tarif.
     *
     * @param id the id of the tarifDTO to save.
     * @param tarifDTO the tarifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarifDTO,
     * or with status {@code 400 (Bad Request)} if the tarifDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tarifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TarifDTO> updateTarif(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TarifDTO tarifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Tarif : {}, {}", id, tarifDTO);
        if (tarifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tarifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tarifDTO = tarifService.update(tarifDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tarifDTO.getId().toString()))
            .body(tarifDTO);
    }

    /**
     * {@code PATCH  /tarifs/:id} : Partial updates given fields of an existing tarif, field will ignore if it is null
     *
     * @param id the id of the tarifDTO to save.
     * @param tarifDTO the tarifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarifDTO,
     * or with status {@code 400 (Bad Request)} if the tarifDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tarifDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tarifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TarifDTO> partialUpdateTarif(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TarifDTO tarifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Tarif partially : {}, {}", id, tarifDTO);
        if (tarifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tarifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TarifDTO> result = tarifService.partialUpdate(tarifDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tarifDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tarifs} : get all the tarifs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tarifs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TarifDTO>> getAllTarifs(
        TarifCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Tarifs by criteria: {}", criteria);

        Page<TarifDTO> page = tarifQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tarifs/count} : count all the tarifs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTarifs(TarifCriteria criteria) {
        LOG.debug("REST request to count Tarifs by criteria: {}", criteria);
        return ResponseEntity.ok().body(tarifQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tarifs/:id} : get the "id" tarif.
     *
     * @param id the id of the tarifDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tarifDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TarifDTO> getTarif(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Tarif : {}", id);
        Optional<TarifDTO> tarifDTO = tarifService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tarifDTO);
    }

    /**
     * {@code DELETE  /tarifs/:id} : delete the "id" tarif.
     *
     * @param id the id of the tarifDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarif(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Tarif : {}", id);
        tarifService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
