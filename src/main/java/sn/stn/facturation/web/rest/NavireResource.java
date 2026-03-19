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
import sn.stn.facturation.repository.NavireRepository;
import sn.stn.facturation.service.NavireQueryService;
import sn.stn.facturation.service.NavireService;
import sn.stn.facturation.service.criteria.NavireCriteria;
import sn.stn.facturation.service.dto.NavireDTO;
import sn.stn.facturation.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.stn.facturation.domain.Navire}.
 */
@RestController
@RequestMapping("/api/navires")
public class NavireResource {

    private static final Logger LOG = LoggerFactory.getLogger(NavireResource.class);

    private static final String ENTITY_NAME = "stnBackendNavire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NavireService navireService;

    private final NavireRepository navireRepository;

    private final NavireQueryService navireQueryService;

    public NavireResource(NavireService navireService, NavireRepository navireRepository, NavireQueryService navireQueryService) {
        this.navireService = navireService;
        this.navireRepository = navireRepository;
        this.navireQueryService = navireQueryService;
    }

    /**
     * {@code POST  /navires} : Create a new navire.
     *
     * @param navireDTO the navireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new navireDTO, or with status {@code 400 (Bad Request)} if the navire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NavireDTO> createNavire(@Valid @RequestBody NavireDTO navireDTO) throws URISyntaxException {
        LOG.debug("REST request to save Navire : {}", navireDTO);
        if (navireDTO.getId() != null) {
            throw new BadRequestAlertException("A new navire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        navireDTO = navireService.save(navireDTO);
        return ResponseEntity.created(new URI("/api/navires/" + navireDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, navireDTO.getId().toString()))
            .body(navireDTO);
    }

    /**
     * {@code PUT  /navires/:id} : Updates an existing navire.
     *
     * @param id the id of the navireDTO to save.
     * @param navireDTO the navireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated navireDTO,
     * or with status {@code 400 (Bad Request)} if the navireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the navireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NavireDTO> updateNavire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NavireDTO navireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Navire : {}, {}", id, navireDTO);
        if (navireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, navireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!navireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        navireDTO = navireService.update(navireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, navireDTO.getId().toString()))
            .body(navireDTO);
    }

    /**
     * {@code PATCH  /navires/:id} : Partial updates given fields of an existing navire, field will ignore if it is null
     *
     * @param id the id of the navireDTO to save.
     * @param navireDTO the navireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated navireDTO,
     * or with status {@code 400 (Bad Request)} if the navireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the navireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the navireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NavireDTO> partialUpdateNavire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NavireDTO navireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Navire partially : {}, {}", id, navireDTO);
        if (navireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, navireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!navireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NavireDTO> result = navireService.partialUpdate(navireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, navireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /navires} : get all the navires.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of navires in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NavireDTO>> getAllNavires(
        NavireCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Navires by criteria: {}", criteria);

        Page<NavireDTO> page = navireQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /navires/count} : count all the navires.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNavires(NavireCriteria criteria) {
        LOG.debug("REST request to count Navires by criteria: {}", criteria);
        return ResponseEntity.ok().body(navireQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /navires/:id} : get the "id" navire.
     *
     * @param id the id of the navireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the navireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NavireDTO> getNavire(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Navire : {}", id);
        Optional<NavireDTO> navireDTO = navireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(navireDTO);
    }

    /**
     * {@code DELETE  /navires/:id} : delete the "id" navire.
     *
     * @param id the id of the navireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNavire(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Navire : {}", id);
        navireService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
