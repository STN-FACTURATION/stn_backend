package sn.stn.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.stn.facturation.domain.LigneFactureSupplementAsserts.*;
import static sn.stn.facturation.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.IntegrationTest;
import sn.stn.facturation.domain.Facture;
import sn.stn.facturation.domain.LigneFactureSupplement;
import sn.stn.facturation.domain.Supplement;
import sn.stn.facturation.repository.LigneFactureSupplementRepository;
import sn.stn.facturation.service.LigneFactureSupplementService;
import sn.stn.facturation.service.dto.LigneFactureSupplementDTO;
import sn.stn.facturation.service.mapper.LigneFactureSupplementMapper;

/**
 * Integration tests for the {@link LigneFactureSupplementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LigneFactureSupplementResourceIT {

    private static final Double DEFAULT_MONTANT_CALCULE = 0D;
    private static final Double UPDATED_MONTANT_CALCULE = 1D;
    private static final Double SMALLER_MONTANT_CALCULE = 0D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ligne-facture-supplements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LigneFactureSupplementRepository ligneFactureSupplementRepository;

    @Mock
    private LigneFactureSupplementRepository ligneFactureSupplementRepositoryMock;

    @Autowired
    private LigneFactureSupplementMapper ligneFactureSupplementMapper;

    @Mock
    private LigneFactureSupplementService ligneFactureSupplementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLigneFactureSupplementMockMvc;

    private LigneFactureSupplement ligneFactureSupplement;

    private LigneFactureSupplement insertedLigneFactureSupplement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneFactureSupplement createEntity(EntityManager em) {
        LigneFactureSupplement ligneFactureSupplement = new LigneFactureSupplement()
            .montantCalcule(DEFAULT_MONTANT_CALCULE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Supplement supplement;
        if (TestUtil.findAll(em, Supplement.class).isEmpty()) {
            supplement = SupplementResourceIT.createEntity();
            em.persist(supplement);
            em.flush();
        } else {
            supplement = TestUtil.findAll(em, Supplement.class).get(0);
        }
        ligneFactureSupplement.setSupplement(supplement);
        // Add required entity
        Facture facture;
        if (TestUtil.findAll(em, Facture.class).isEmpty()) {
            facture = FactureResourceIT.createEntity(em);
            em.persist(facture);
            em.flush();
        } else {
            facture = TestUtil.findAll(em, Facture.class).get(0);
        }
        ligneFactureSupplement.setFacture(facture);
        return ligneFactureSupplement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneFactureSupplement createUpdatedEntity(EntityManager em) {
        LigneFactureSupplement updatedLigneFactureSupplement = new LigneFactureSupplement()
            .montantCalcule(UPDATED_MONTANT_CALCULE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Supplement supplement;
        if (TestUtil.findAll(em, Supplement.class).isEmpty()) {
            supplement = SupplementResourceIT.createUpdatedEntity();
            em.persist(supplement);
            em.flush();
        } else {
            supplement = TestUtil.findAll(em, Supplement.class).get(0);
        }
        updatedLigneFactureSupplement.setSupplement(supplement);
        // Add required entity
        Facture facture;
        if (TestUtil.findAll(em, Facture.class).isEmpty()) {
            facture = FactureResourceIT.createUpdatedEntity(em);
            em.persist(facture);
            em.flush();
        } else {
            facture = TestUtil.findAll(em, Facture.class).get(0);
        }
        updatedLigneFactureSupplement.setFacture(facture);
        return updatedLigneFactureSupplement;
    }

    @BeforeEach
    void initTest() {
        ligneFactureSupplement = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedLigneFactureSupplement != null) {
            ligneFactureSupplementRepository.delete(insertedLigneFactureSupplement);
            insertedLigneFactureSupplement = null;
        }
    }

    @Test
    @Transactional
    void createLigneFactureSupplement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LigneFactureSupplement
        LigneFactureSupplementDTO ligneFactureSupplementDTO = ligneFactureSupplementMapper.toDto(ligneFactureSupplement);
        var returnedLigneFactureSupplementDTO = om.readValue(
            restLigneFactureSupplementMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneFactureSupplementDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LigneFactureSupplementDTO.class
        );

        // Validate the LigneFactureSupplement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLigneFactureSupplement = ligneFactureSupplementMapper.toEntity(returnedLigneFactureSupplementDTO);
        assertLigneFactureSupplementUpdatableFieldsEquals(
            returnedLigneFactureSupplement,
            getPersistedLigneFactureSupplement(returnedLigneFactureSupplement)
        );

        insertedLigneFactureSupplement = returnedLigneFactureSupplement;
    }

    @Test
    @Transactional
    void createLigneFactureSupplementWithExistingId() throws Exception {
        // Create the LigneFactureSupplement with an existing ID
        ligneFactureSupplement.setId(1L);
        LigneFactureSupplementDTO ligneFactureSupplementDTO = ligneFactureSupplementMapper.toDto(ligneFactureSupplement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneFactureSupplementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneFactureSupplementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LigneFactureSupplement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMontantCalculeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ligneFactureSupplement.setMontantCalcule(null);

        // Create the LigneFactureSupplement, which fails.
        LigneFactureSupplementDTO ligneFactureSupplementDTO = ligneFactureSupplementMapper.toDto(ligneFactureSupplement);

        restLigneFactureSupplementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneFactureSupplementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplements() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get all the ligneFactureSupplementList
        restLigneFactureSupplementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneFactureSupplement.getId().intValue())))
            .andExpect(jsonPath("$.[*].montantCalcule").value(hasItem(DEFAULT_MONTANT_CALCULE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLigneFactureSupplementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(ligneFactureSupplementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLigneFactureSupplementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ligneFactureSupplementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLigneFactureSupplementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ligneFactureSupplementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLigneFactureSupplementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ligneFactureSupplementRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLigneFactureSupplement() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get the ligneFactureSupplement
        restLigneFactureSupplementMockMvc
            .perform(get(ENTITY_API_URL_ID, ligneFactureSupplement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ligneFactureSupplement.getId().intValue()))
            .andExpect(jsonPath("$.montantCalcule").value(DEFAULT_MONTANT_CALCULE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getLigneFactureSupplementsByIdFiltering() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        Long id = ligneFactureSupplement.getId();

        defaultLigneFactureSupplementFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLigneFactureSupplementFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLigneFactureSupplementFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsByMontantCalculeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get all the ligneFactureSupplementList where montantCalcule equals to
        defaultLigneFactureSupplementFiltering(
            "montantCalcule.equals=" + DEFAULT_MONTANT_CALCULE,
            "montantCalcule.equals=" + UPDATED_MONTANT_CALCULE
        );
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsByMontantCalculeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get all the ligneFactureSupplementList where montantCalcule in
        defaultLigneFactureSupplementFiltering(
            "montantCalcule.in=" + DEFAULT_MONTANT_CALCULE + "," + UPDATED_MONTANT_CALCULE,
            "montantCalcule.in=" + UPDATED_MONTANT_CALCULE
        );
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsByMontantCalculeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get all the ligneFactureSupplementList where montantCalcule is not null
        defaultLigneFactureSupplementFiltering("montantCalcule.specified=true", "montantCalcule.specified=false");
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsByMontantCalculeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get all the ligneFactureSupplementList where montantCalcule is greater than or equal to
        defaultLigneFactureSupplementFiltering(
            "montantCalcule.greaterThanOrEqual=" + DEFAULT_MONTANT_CALCULE,
            "montantCalcule.greaterThanOrEqual=" + UPDATED_MONTANT_CALCULE
        );
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsByMontantCalculeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get all the ligneFactureSupplementList where montantCalcule is less than or equal to
        defaultLigneFactureSupplementFiltering(
            "montantCalcule.lessThanOrEqual=" + DEFAULT_MONTANT_CALCULE,
            "montantCalcule.lessThanOrEqual=" + SMALLER_MONTANT_CALCULE
        );
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsByMontantCalculeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get all the ligneFactureSupplementList where montantCalcule is less than
        defaultLigneFactureSupplementFiltering(
            "montantCalcule.lessThan=" + UPDATED_MONTANT_CALCULE,
            "montantCalcule.lessThan=" + DEFAULT_MONTANT_CALCULE
        );
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsByMontantCalculeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get all the ligneFactureSupplementList where montantCalcule is greater than
        defaultLigneFactureSupplementFiltering(
            "montantCalcule.greaterThan=" + SMALLER_MONTANT_CALCULE,
            "montantCalcule.greaterThan=" + DEFAULT_MONTANT_CALCULE
        );
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get all the ligneFactureSupplementList where description equals to
        defaultLigneFactureSupplementFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get all the ligneFactureSupplementList where description in
        defaultLigneFactureSupplementFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get all the ligneFactureSupplementList where description is not null
        defaultLigneFactureSupplementFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get all the ligneFactureSupplementList where description contains
        defaultLigneFactureSupplementFiltering(
            "description.contains=" + DEFAULT_DESCRIPTION,
            "description.contains=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        // Get all the ligneFactureSupplementList where description does not contain
        defaultLigneFactureSupplementFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsBySupplementIsEqualToSomething() throws Exception {
        Supplement supplement;
        if (TestUtil.findAll(em, Supplement.class).isEmpty()) {
            ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);
            supplement = SupplementResourceIT.createEntity();
        } else {
            supplement = TestUtil.findAll(em, Supplement.class).get(0);
        }
        em.persist(supplement);
        em.flush();
        ligneFactureSupplement.setSupplement(supplement);
        ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);
        Long supplementId = supplement.getId();
        // Get all the ligneFactureSupplementList where supplement equals to supplementId
        defaultLigneFactureSupplementShouldBeFound("supplementId.equals=" + supplementId);

        // Get all the ligneFactureSupplementList where supplement equals to (supplementId + 1)
        defaultLigneFactureSupplementShouldNotBeFound("supplementId.equals=" + (supplementId + 1));
    }

    @Test
    @Transactional
    void getAllLigneFactureSupplementsByFactureIsEqualToSomething() throws Exception {
        Facture facture;
        if (TestUtil.findAll(em, Facture.class).isEmpty()) {
            ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);
            facture = FactureResourceIT.createEntity(em);
        } else {
            facture = TestUtil.findAll(em, Facture.class).get(0);
        }
        em.persist(facture);
        em.flush();
        ligneFactureSupplement.setFacture(facture);
        ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);
        Long factureId = facture.getId();
        // Get all the ligneFactureSupplementList where facture equals to factureId
        defaultLigneFactureSupplementShouldBeFound("factureId.equals=" + factureId);

        // Get all the ligneFactureSupplementList where facture equals to (factureId + 1)
        defaultLigneFactureSupplementShouldNotBeFound("factureId.equals=" + (factureId + 1));
    }

    private void defaultLigneFactureSupplementFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLigneFactureSupplementShouldBeFound(shouldBeFound);
        defaultLigneFactureSupplementShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLigneFactureSupplementShouldBeFound(String filter) throws Exception {
        restLigneFactureSupplementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneFactureSupplement.getId().intValue())))
            .andExpect(jsonPath("$.[*].montantCalcule").value(hasItem(DEFAULT_MONTANT_CALCULE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restLigneFactureSupplementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLigneFactureSupplementShouldNotBeFound(String filter) throws Exception {
        restLigneFactureSupplementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLigneFactureSupplementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLigneFactureSupplement() throws Exception {
        // Get the ligneFactureSupplement
        restLigneFactureSupplementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLigneFactureSupplement() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ligneFactureSupplement
        LigneFactureSupplement updatedLigneFactureSupplement = ligneFactureSupplementRepository
            .findById(ligneFactureSupplement.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedLigneFactureSupplement are not directly saved in db
        em.detach(updatedLigneFactureSupplement);
        updatedLigneFactureSupplement.montantCalcule(UPDATED_MONTANT_CALCULE).description(UPDATED_DESCRIPTION);
        LigneFactureSupplementDTO ligneFactureSupplementDTO = ligneFactureSupplementMapper.toDto(updatedLigneFactureSupplement);

        restLigneFactureSupplementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ligneFactureSupplementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ligneFactureSupplementDTO))
            )
            .andExpect(status().isOk());

        // Validate the LigneFactureSupplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLigneFactureSupplementToMatchAllProperties(updatedLigneFactureSupplement);
    }

    @Test
    @Transactional
    void putNonExistingLigneFactureSupplement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligneFactureSupplement.setId(longCount.incrementAndGet());

        // Create the LigneFactureSupplement
        LigneFactureSupplementDTO ligneFactureSupplementDTO = ligneFactureSupplementMapper.toDto(ligneFactureSupplement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneFactureSupplementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ligneFactureSupplementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ligneFactureSupplementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneFactureSupplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLigneFactureSupplement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligneFactureSupplement.setId(longCount.incrementAndGet());

        // Create the LigneFactureSupplement
        LigneFactureSupplementDTO ligneFactureSupplementDTO = ligneFactureSupplementMapper.toDto(ligneFactureSupplement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneFactureSupplementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ligneFactureSupplementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneFactureSupplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLigneFactureSupplement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligneFactureSupplement.setId(longCount.incrementAndGet());

        // Create the LigneFactureSupplement
        LigneFactureSupplementDTO ligneFactureSupplementDTO = ligneFactureSupplementMapper.toDto(ligneFactureSupplement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneFactureSupplementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneFactureSupplementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigneFactureSupplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLigneFactureSupplementWithPatch() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ligneFactureSupplement using partial update
        LigneFactureSupplement partialUpdatedLigneFactureSupplement = new LigneFactureSupplement();
        partialUpdatedLigneFactureSupplement.setId(ligneFactureSupplement.getId());

        restLigneFactureSupplementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigneFactureSupplement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLigneFactureSupplement))
            )
            .andExpect(status().isOk());

        // Validate the LigneFactureSupplement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLigneFactureSupplementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLigneFactureSupplement, ligneFactureSupplement),
            getPersistedLigneFactureSupplement(ligneFactureSupplement)
        );
    }

    @Test
    @Transactional
    void fullUpdateLigneFactureSupplementWithPatch() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ligneFactureSupplement using partial update
        LigneFactureSupplement partialUpdatedLigneFactureSupplement = new LigneFactureSupplement();
        partialUpdatedLigneFactureSupplement.setId(ligneFactureSupplement.getId());

        partialUpdatedLigneFactureSupplement.montantCalcule(UPDATED_MONTANT_CALCULE).description(UPDATED_DESCRIPTION);

        restLigneFactureSupplementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigneFactureSupplement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLigneFactureSupplement))
            )
            .andExpect(status().isOk());

        // Validate the LigneFactureSupplement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLigneFactureSupplementUpdatableFieldsEquals(
            partialUpdatedLigneFactureSupplement,
            getPersistedLigneFactureSupplement(partialUpdatedLigneFactureSupplement)
        );
    }

    @Test
    @Transactional
    void patchNonExistingLigneFactureSupplement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligneFactureSupplement.setId(longCount.incrementAndGet());

        // Create the LigneFactureSupplement
        LigneFactureSupplementDTO ligneFactureSupplementDTO = ligneFactureSupplementMapper.toDto(ligneFactureSupplement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneFactureSupplementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ligneFactureSupplementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ligneFactureSupplementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneFactureSupplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLigneFactureSupplement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligneFactureSupplement.setId(longCount.incrementAndGet());

        // Create the LigneFactureSupplement
        LigneFactureSupplementDTO ligneFactureSupplementDTO = ligneFactureSupplementMapper.toDto(ligneFactureSupplement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneFactureSupplementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ligneFactureSupplementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneFactureSupplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLigneFactureSupplement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligneFactureSupplement.setId(longCount.incrementAndGet());

        // Create the LigneFactureSupplement
        LigneFactureSupplementDTO ligneFactureSupplementDTO = ligneFactureSupplementMapper.toDto(ligneFactureSupplement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneFactureSupplementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ligneFactureSupplementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigneFactureSupplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLigneFactureSupplement() throws Exception {
        // Initialize the database
        insertedLigneFactureSupplement = ligneFactureSupplementRepository.saveAndFlush(ligneFactureSupplement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ligneFactureSupplement
        restLigneFactureSupplementMockMvc
            .perform(delete(ENTITY_API_URL_ID, ligneFactureSupplement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ligneFactureSupplementRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected LigneFactureSupplement getPersistedLigneFactureSupplement(LigneFactureSupplement ligneFactureSupplement) {
        return ligneFactureSupplementRepository.findById(ligneFactureSupplement.getId()).orElseThrow();
    }

    protected void assertPersistedLigneFactureSupplementToMatchAllProperties(LigneFactureSupplement expectedLigneFactureSupplement) {
        assertLigneFactureSupplementAllPropertiesEquals(
            expectedLigneFactureSupplement,
            getPersistedLigneFactureSupplement(expectedLigneFactureSupplement)
        );
    }

    protected void assertPersistedLigneFactureSupplementToMatchUpdatableProperties(LigneFactureSupplement expectedLigneFactureSupplement) {
        assertLigneFactureSupplementAllUpdatablePropertiesEquals(
            expectedLigneFactureSupplement,
            getPersistedLigneFactureSupplement(expectedLigneFactureSupplement)
        );
    }
}
