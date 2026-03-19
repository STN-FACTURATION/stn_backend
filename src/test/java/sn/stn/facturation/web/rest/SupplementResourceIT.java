package sn.stn.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.stn.facturation.domain.SupplementAsserts.*;
import static sn.stn.facturation.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.stn.facturation.IntegrationTest;
import sn.stn.facturation.domain.Supplement;
import sn.stn.facturation.domain.enumeration.TypeSupplement;
import sn.stn.facturation.repository.SupplementRepository;
import sn.stn.facturation.service.dto.SupplementDTO;
import sn.stn.facturation.service.mapper.SupplementMapper;

/**
 * Integration tests for the {@link SupplementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SupplementResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final TypeSupplement DEFAULT_TYPE = TypeSupplement.S1_DUREE_SUP_2H;
    private static final TypeSupplement UPDATED_TYPE = TypeSupplement.S2_SANS_MACHINE;

    private static final Double DEFAULT_TAUX_POURCENTAGE = 0D;
    private static final Double UPDATED_TAUX_POURCENTAGE = 1D;
    private static final Double SMALLER_TAUX_POURCENTAGE = 0D - 1D;

    private static final Double DEFAULT_MONTANT_FIXE = 0D;
    private static final Double UPDATED_MONTANT_FIXE = 1D;
    private static final Double SMALLER_MONTANT_FIXE = 0D - 1D;

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/supplements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplementRepository supplementRepository;

    @Autowired
    private SupplementMapper supplementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplementMockMvc;

    private Supplement supplement;

    private Supplement insertedSupplement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplement createEntity() {
        return new Supplement()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .type(DEFAULT_TYPE)
            .tauxPourcentage(DEFAULT_TAUX_POURCENTAGE)
            .montantFixe(DEFAULT_MONTANT_FIXE)
            .actif(DEFAULT_ACTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplement createUpdatedEntity() {
        return new Supplement()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .type(UPDATED_TYPE)
            .tauxPourcentage(UPDATED_TAUX_POURCENTAGE)
            .montantFixe(UPDATED_MONTANT_FIXE)
            .actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        supplement = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSupplement != null) {
            supplementRepository.delete(insertedSupplement);
            insertedSupplement = null;
        }
    }

    @Test
    @Transactional
    void createSupplement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Supplement
        SupplementDTO supplementDTO = supplementMapper.toDto(supplement);
        var returnedSupplementDTO = om.readValue(
            restSupplementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupplementDTO.class
        );

        // Validate the Supplement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSupplement = supplementMapper.toEntity(returnedSupplementDTO);
        assertSupplementUpdatableFieldsEquals(returnedSupplement, getPersistedSupplement(returnedSupplement));

        insertedSupplement = returnedSupplement;
    }

    @Test
    @Transactional
    void createSupplementWithExistingId() throws Exception {
        // Create the Supplement with an existing ID
        supplement.setId(1L);
        SupplementDTO supplementDTO = supplementMapper.toDto(supplement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Supplement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplement.setCode(null);

        // Create the Supplement, which fails.
        SupplementDTO supplementDTO = supplementMapper.toDto(supplement);

        restSupplementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplement.setLibelle(null);

        // Create the Supplement, which fails.
        SupplementDTO supplementDTO = supplementMapper.toDto(supplement);

        restSupplementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplement.setType(null);

        // Create the Supplement, which fails.
        SupplementDTO supplementDTO = supplementMapper.toDto(supplement);

        restSupplementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplement.setActif(null);

        // Create the Supplement, which fails.
        SupplementDTO supplementDTO = supplementMapper.toDto(supplement);

        restSupplementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupplements() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList
        restSupplementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplement.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].tauxPourcentage").value(hasItem(DEFAULT_TAUX_POURCENTAGE)))
            .andExpect(jsonPath("$.[*].montantFixe").value(hasItem(DEFAULT_MONTANT_FIXE)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @Test
    @Transactional
    void getSupplement() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get the supplement
        restSupplementMockMvc
            .perform(get(ENTITY_API_URL_ID, supplement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplement.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.tauxPourcentage").value(DEFAULT_TAUX_POURCENTAGE))
            .andExpect(jsonPath("$.montantFixe").value(DEFAULT_MONTANT_FIXE))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getSupplementsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        Long id = supplement.getId();

        defaultSupplementFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSupplementFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSupplementFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSupplementsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where code equals to
        defaultSupplementFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSupplementsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where code in
        defaultSupplementFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSupplementsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where code is not null
        defaultSupplementFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplementsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where code contains
        defaultSupplementFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSupplementsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where code does not contain
        defaultSupplementFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllSupplementsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where libelle equals to
        defaultSupplementFiltering("libelle.equals=" + DEFAULT_LIBELLE, "libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSupplementsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where libelle in
        defaultSupplementFiltering("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE, "libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSupplementsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where libelle is not null
        defaultSupplementFiltering("libelle.specified=true", "libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplementsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where libelle contains
        defaultSupplementFiltering("libelle.contains=" + DEFAULT_LIBELLE, "libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSupplementsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where libelle does not contain
        defaultSupplementFiltering("libelle.doesNotContain=" + UPDATED_LIBELLE, "libelle.doesNotContain=" + DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSupplementsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where type equals to
        defaultSupplementFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSupplementsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where type in
        defaultSupplementFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSupplementsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where type is not null
        defaultSupplementFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplementsByTauxPourcentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where tauxPourcentage equals to
        defaultSupplementFiltering(
            "tauxPourcentage.equals=" + DEFAULT_TAUX_POURCENTAGE,
            "tauxPourcentage.equals=" + UPDATED_TAUX_POURCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllSupplementsByTauxPourcentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where tauxPourcentage in
        defaultSupplementFiltering(
            "tauxPourcentage.in=" + DEFAULT_TAUX_POURCENTAGE + "," + UPDATED_TAUX_POURCENTAGE,
            "tauxPourcentage.in=" + UPDATED_TAUX_POURCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllSupplementsByTauxPourcentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where tauxPourcentage is not null
        defaultSupplementFiltering("tauxPourcentage.specified=true", "tauxPourcentage.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplementsByTauxPourcentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where tauxPourcentage is greater than or equal to
        defaultSupplementFiltering(
            "tauxPourcentage.greaterThanOrEqual=" + DEFAULT_TAUX_POURCENTAGE,
            "tauxPourcentage.greaterThanOrEqual=" + (DEFAULT_TAUX_POURCENTAGE + 1)
        );
    }

    @Test
    @Transactional
    void getAllSupplementsByTauxPourcentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where tauxPourcentage is less than or equal to
        defaultSupplementFiltering(
            "tauxPourcentage.lessThanOrEqual=" + DEFAULT_TAUX_POURCENTAGE,
            "tauxPourcentage.lessThanOrEqual=" + SMALLER_TAUX_POURCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllSupplementsByTauxPourcentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where tauxPourcentage is less than
        defaultSupplementFiltering(
            "tauxPourcentage.lessThan=" + (DEFAULT_TAUX_POURCENTAGE + 1),
            "tauxPourcentage.lessThan=" + DEFAULT_TAUX_POURCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllSupplementsByTauxPourcentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where tauxPourcentage is greater than
        defaultSupplementFiltering(
            "tauxPourcentage.greaterThan=" + SMALLER_TAUX_POURCENTAGE,
            "tauxPourcentage.greaterThan=" + DEFAULT_TAUX_POURCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllSupplementsByMontantFixeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where montantFixe equals to
        defaultSupplementFiltering("montantFixe.equals=" + DEFAULT_MONTANT_FIXE, "montantFixe.equals=" + UPDATED_MONTANT_FIXE);
    }

    @Test
    @Transactional
    void getAllSupplementsByMontantFixeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where montantFixe in
        defaultSupplementFiltering(
            "montantFixe.in=" + DEFAULT_MONTANT_FIXE + "," + UPDATED_MONTANT_FIXE,
            "montantFixe.in=" + UPDATED_MONTANT_FIXE
        );
    }

    @Test
    @Transactional
    void getAllSupplementsByMontantFixeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where montantFixe is not null
        defaultSupplementFiltering("montantFixe.specified=true", "montantFixe.specified=false");
    }

    @Test
    @Transactional
    void getAllSupplementsByMontantFixeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where montantFixe is greater than or equal to
        defaultSupplementFiltering(
            "montantFixe.greaterThanOrEqual=" + DEFAULT_MONTANT_FIXE,
            "montantFixe.greaterThanOrEqual=" + UPDATED_MONTANT_FIXE
        );
    }

    @Test
    @Transactional
    void getAllSupplementsByMontantFixeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where montantFixe is less than or equal to
        defaultSupplementFiltering(
            "montantFixe.lessThanOrEqual=" + DEFAULT_MONTANT_FIXE,
            "montantFixe.lessThanOrEqual=" + SMALLER_MONTANT_FIXE
        );
    }

    @Test
    @Transactional
    void getAllSupplementsByMontantFixeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where montantFixe is less than
        defaultSupplementFiltering("montantFixe.lessThan=" + UPDATED_MONTANT_FIXE, "montantFixe.lessThan=" + DEFAULT_MONTANT_FIXE);
    }

    @Test
    @Transactional
    void getAllSupplementsByMontantFixeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where montantFixe is greater than
        defaultSupplementFiltering("montantFixe.greaterThan=" + SMALLER_MONTANT_FIXE, "montantFixe.greaterThan=" + DEFAULT_MONTANT_FIXE);
    }

    @Test
    @Transactional
    void getAllSupplementsByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where actif equals to
        defaultSupplementFiltering("actif.equals=" + DEFAULT_ACTIF, "actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllSupplementsByActifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where actif in
        defaultSupplementFiltering("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF, "actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllSupplementsByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        // Get all the supplementList where actif is not null
        defaultSupplementFiltering("actif.specified=true", "actif.specified=false");
    }

    private void defaultSupplementFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSupplementShouldBeFound(shouldBeFound);
        defaultSupplementShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplementShouldBeFound(String filter) throws Exception {
        restSupplementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplement.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].tauxPourcentage").value(hasItem(DEFAULT_TAUX_POURCENTAGE)))
            .andExpect(jsonPath("$.[*].montantFixe").value(hasItem(DEFAULT_MONTANT_FIXE)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));

        // Check, that the count call also returns 1
        restSupplementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplementShouldNotBeFound(String filter) throws Exception {
        restSupplementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplement() throws Exception {
        // Get the supplement
        restSupplementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplement() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplement
        Supplement updatedSupplement = supplementRepository.findById(supplement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplement are not directly saved in db
        em.detach(updatedSupplement);
        updatedSupplement
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .type(UPDATED_TYPE)
            .tauxPourcentage(UPDATED_TAUX_POURCENTAGE)
            .montantFixe(UPDATED_MONTANT_FIXE)
            .actif(UPDATED_ACTIF);
        SupplementDTO supplementDTO = supplementMapper.toDto(updatedSupplement);

        restSupplementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Supplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplementToMatchAllProperties(updatedSupplement);
    }

    @Test
    @Transactional
    void putNonExistingSupplement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplement.setId(longCount.incrementAndGet());

        // Create the Supplement
        SupplementDTO supplementDTO = supplementMapper.toDto(supplement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplement.setId(longCount.incrementAndGet());

        // Create the Supplement
        SupplementDTO supplementDTO = supplementMapper.toDto(supplement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplement.setId(longCount.incrementAndGet());

        // Create the Supplement
        SupplementDTO supplementDTO = supplementMapper.toDto(supplement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Supplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplementWithPatch() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplement using partial update
        Supplement partialUpdatedSupplement = new Supplement();
        partialUpdatedSupplement.setId(supplement.getId());

        partialUpdatedSupplement.code(UPDATED_CODE).montantFixe(UPDATED_MONTANT_FIXE);

        restSupplementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplement))
            )
            .andExpect(status().isOk());

        // Validate the Supplement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupplement, supplement),
            getPersistedSupplement(supplement)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupplementWithPatch() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplement using partial update
        Supplement partialUpdatedSupplement = new Supplement();
        partialUpdatedSupplement.setId(supplement.getId());

        partialUpdatedSupplement
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .type(UPDATED_TYPE)
            .tauxPourcentage(UPDATED_TAUX_POURCENTAGE)
            .montantFixe(UPDATED_MONTANT_FIXE)
            .actif(UPDATED_ACTIF);

        restSupplementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplement))
            )
            .andExpect(status().isOk());

        // Validate the Supplement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplementUpdatableFieldsEquals(partialUpdatedSupplement, getPersistedSupplement(partialUpdatedSupplement));
    }

    @Test
    @Transactional
    void patchNonExistingSupplement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplement.setId(longCount.incrementAndGet());

        // Create the Supplement
        SupplementDTO supplementDTO = supplementMapper.toDto(supplement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplement.setId(longCount.incrementAndGet());

        // Create the Supplement
        SupplementDTO supplementDTO = supplementMapper.toDto(supplement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplement.setId(longCount.incrementAndGet());

        // Create the Supplement
        SupplementDTO supplementDTO = supplementMapper.toDto(supplement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Supplement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplement() throws Exception {
        // Initialize the database
        insertedSupplement = supplementRepository.saveAndFlush(supplement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplement
        restSupplementMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplementRepository.count();
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

    protected Supplement getPersistedSupplement(Supplement supplement) {
        return supplementRepository.findById(supplement.getId()).orElseThrow();
    }

    protected void assertPersistedSupplementToMatchAllProperties(Supplement expectedSupplement) {
        assertSupplementAllPropertiesEquals(expectedSupplement, getPersistedSupplement(expectedSupplement));
    }

    protected void assertPersistedSupplementToMatchUpdatableProperties(Supplement expectedSupplement) {
        assertSupplementAllUpdatablePropertiesEquals(expectedSupplement, getPersistedSupplement(expectedSupplement));
    }
}
