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
import sn.stn.facturation.domain.enumeration.StatutFacture;
import sn.stn.facturation.repository.FactureRepository;
import sn.stn.facturation.service.FactureQueryService;
import sn.stn.facturation.service.FactureService;
import sn.stn.facturation.service.criteria.FactureCriteria;
import sn.stn.facturation.service.dto.FactureDTO;
import sn.stn.facturation.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.stn.facturation.domain.Facture}.
 */
@RestController
@RequestMapping("/api/factures")
public class FactureResource {

    private static final Logger LOG = LoggerFactory.getLogger(FactureResource.class);

    private static final String ENTITY_NAME = "facture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactureService factureService;

    private final FactureRepository factureRepository;

    private final FactureQueryService factureQueryService;

    public FactureResource(FactureService factureService, FactureRepository factureRepository,
            FactureQueryService factureQueryService) {
        this.factureService = factureService;
        this.factureRepository = factureRepository;
        this.factureQueryService = factureQueryService;
    }

    /**
     * {@code POST  /factures} : Create a new facture.
     *
     * @param factureDTO the factureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new factureDTO, or with status {@code 400 (Bad Request)} if
     *         the facture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FactureDTO> createFacture(@Valid @RequestBody FactureDTO factureDTO)
            throws URISyntaxException {
        LOG.debug("REST request to save Facture : {}", factureDTO);
        if (factureDTO.getId() != null) {
            throw new BadRequestAlertException("A new facture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        factureDTO = factureService.save(factureDTO);
        return ResponseEntity.created(new URI("/api/factures/" + factureDTO.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("stnGatewayApp", true, ENTITY_NAME,
                        factureDTO.getId().toString()))
                .body(factureDTO);
    }

    /**
     * {@code PUT  /factures/:id} : Updates an existing facture.
     *
     * @param id         the id of the factureDTO to save.
     * @param factureDTO the factureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated factureDTO,
     *         or with status {@code 400 (Bad Request)} if the factureDTO is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the factureDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FactureDTO> updateFacture(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody FactureDTO factureDTO) throws URISyntaxException {
        LOG.debug("REST request to update Facture : {}, {}", id, factureDTO);
        if (factureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        factureDTO = factureService.update(factureDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("stnGatewayApp", true, ENTITY_NAME,
                        factureDTO.getId().toString()))
                .body(factureDTO);
    }

    /**
     * {@code PATCH  /factures/:id} : Partial updates given fields of an existing
     * facture, field will ignore if it is null
     *
     * @param id         the id of the factureDTO to save.
     * @param factureDTO the factureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated factureDTO,
     *         or with status {@code 400 (Bad Request)} if the factureDTO is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the factureDTO is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the factureDTO
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FactureDTO> partialUpdateFacture(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody FactureDTO factureDTO) throws URISyntaxException {
        LOG.debug("REST request to partial update Facture partially : {}, {}", id, factureDTO);
        if (factureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactureDTO> result = factureService.partialUpdate(factureDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert("stnGatewayApp", true, ENTITY_NAME, factureDTO.getId().toString()));
    }

    /**
     * {@code PUT  /factures/:id/statut} : Updates the status of a given facture.
     *
     * @param id        the id of the facture to update.
     * @param statutStr the new status to apply.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated factureDTO,
     *         or with status {@code 404 (Not Found)}.
     */
    @PutMapping("/{id}/statut")
    public ResponseEntity<FactureDTO> updateFactureStatut(
            @PathVariable(value = "id", required = true) final Long id,
            @RequestBody String statutStr) {
        sn.stn.facturation.domain.enumeration.StatutFacture statut = StatutFacture
                .valueOf(statutStr.replace("\"", ""));
        LOG.debug("REST request to update Facture statut : {} to {}", id, statut);
        Optional<FactureDTO> factureDTO = factureService.updateStatut(id, statut);
        return ResponseUtil.wrapOrNotFound(factureDTO);
    }

    /**
     * {@code GET  /factures} : get all the factures.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of factures in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FactureDTO>> getAllFactures(
            FactureCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get Factures by criteria: {}", criteria);

        Page<FactureDTO> page = factureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /factures/count} : count all the factures.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFactures(FactureCriteria criteria) {
        LOG.debug("REST request to count Factures by criteria: {}", criteria);
        return ResponseEntity.ok().body(factureQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /factures/:id} : get the "id" facture.
     *
     * @param id the id of the factureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the factureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FactureDTO> getFacture(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Facture : {}", id);
        Optional<FactureDTO> factureDTO = factureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factureDTO);
    }

    /**
     * {@code DELETE  /factures/:id} : delete the "id" facture.
     *
     * @param id the id of the factureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Facture : {}", id);
        factureService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert("stnGatewayApp", true, ENTITY_NAME, id.toString()))
                .build();
    }
}
