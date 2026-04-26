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
import sn.stn.facturation.repository.HistoriqueTarifRepository;
import sn.stn.facturation.service.HistoriqueTarifQueryService;
import sn.stn.facturation.service.HistoriqueTarifService;
import sn.stn.facturation.service.criteria.HistoriqueTarifCriteria;
import sn.stn.facturation.service.dto.HistoriqueTarifDTO;
import sn.stn.facturation.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.stn.facturation.domain.HistoriqueTarif}.
 */
@RestController
@RequestMapping("/api/historique-tarifs")
public class HistoriqueTarifResource {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueTarifResource.class);

    private static final String ENTITY_NAME = "historiqueTarif";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriqueTarifService historiqueTarifService;

    private final HistoriqueTarifRepository historiqueTarifRepository;

    private final HistoriqueTarifQueryService historiqueTarifQueryService;

    public HistoriqueTarifResource(
        HistoriqueTarifService historiqueTarifService,
        HistoriqueTarifRepository historiqueTarifRepository,
        HistoriqueTarifQueryService historiqueTarifQueryService
    ) {
        this.historiqueTarifService = historiqueTarifService;
        this.historiqueTarifRepository = historiqueTarifRepository;
        this.historiqueTarifQueryService = historiqueTarifQueryService;
    }

    /**
     * {@code POST  /historique-tarifs} : Create a new historiqueTarif.
     *
     * @param historiqueTarifDTO the historiqueTarifDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historiqueTarifDTO, or with status {@code 400 (Bad Request)} if the historiqueTarif has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HistoriqueTarifDTO> createHistoriqueTarif(@Valid @RequestBody HistoriqueTarifDTO historiqueTarifDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HistoriqueTarif : {}", historiqueTarifDTO);
        if (historiqueTarifDTO.getId() != null) {
            throw new BadRequestAlertException("A new historiqueTarif cannot already have an ID", ENTITY_NAME, "idexists");
        }
        historiqueTarifDTO = historiqueTarifService.save(historiqueTarifDTO);
        return ResponseEntity.created(new URI("/api/historique-tarifs/" + historiqueTarifDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, historiqueTarifDTO.getId().toString()))
            .body(historiqueTarifDTO);
    }

    /**
     * {@code PUT  /historique-tarifs/:id} : Updates an existing historiqueTarif.
     *
     * @param id the id of the historiqueTarifDTO to save.
     * @param historiqueTarifDTO the historiqueTarifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueTarifDTO,
     * or with status {@code 400 (Bad Request)} if the historiqueTarifDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historiqueTarifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HistoriqueTarifDTO> updateHistoriqueTarif(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistoriqueTarifDTO historiqueTarifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HistoriqueTarif : {}, {}", id, historiqueTarifDTO);
        if (historiqueTarifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiqueTarifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiqueTarifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        historiqueTarifDTO = historiqueTarifService.update(historiqueTarifDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiqueTarifDTO.getId().toString()))
            .body(historiqueTarifDTO);
    }

    /**
     * {@code PATCH  /historique-tarifs/:id} : Partial updates given fields of an existing historiqueTarif, field will ignore if it is null
     *
     * @param id the id of the historiqueTarifDTO to save.
     * @param historiqueTarifDTO the historiqueTarifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueTarifDTO,
     * or with status {@code 400 (Bad Request)} if the historiqueTarifDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historiqueTarifDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historiqueTarifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HistoriqueTarifDTO> partialUpdateHistoriqueTarif(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistoriqueTarifDTO historiqueTarifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HistoriqueTarif partially : {}, {}", id, historiqueTarifDTO);
        if (historiqueTarifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiqueTarifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiqueTarifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoriqueTarifDTO> result = historiqueTarifService.partialUpdate(historiqueTarifDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiqueTarifDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /historique-tarifs} : get all the historiqueTarifs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiqueTarifs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HistoriqueTarifDTO>> getAllHistoriqueTarifs(
        HistoriqueTarifCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HistoriqueTarifs by criteria: {}", criteria);

        Page<HistoriqueTarifDTO> page = historiqueTarifQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historique-tarifs/count} : count all the historiqueTarifs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHistoriqueTarifs(HistoriqueTarifCriteria criteria) {
        LOG.debug("REST request to count HistoriqueTarifs by criteria: {}", criteria);
        return ResponseEntity.ok().body(historiqueTarifQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /historique-tarifs/:id} : get the "id" historiqueTarif.
     *
     * @param id the id of the historiqueTarifDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiqueTarifDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueTarifDTO> getHistoriqueTarif(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HistoriqueTarif : {}", id);
        Optional<HistoriqueTarifDTO> historiqueTarifDTO = historiqueTarifService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historiqueTarifDTO);
    }

    /**
     * {@code DELETE  /historique-tarifs/:id} : delete the "id" historiqueTarif.
     *
     * @param id the id of the historiqueTarifDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoriqueTarif(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HistoriqueTarif : {}", id);
        historiqueTarifService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
