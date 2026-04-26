package sn.stn.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.stn.facturation.domain.TarifAsserts.*;
import static sn.stn.facturation.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import sn.stn.facturation.domain.Tarif;
import sn.stn.facturation.repository.TarifRepository;
import sn.stn.facturation.service.dto.TarifDTO;
import sn.stn.facturation.service.mapper.TarifMapper;

/**
 * Integration tests for the {@link TarifResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TarifResourceIT {

    private static final Double DEFAULT_TRANCHE_MIN = 0D;
    private static final Double UPDATED_TRANCHE_MIN = 1D;
    private static final Double SMALLER_TRANCHE_MIN = 0D - 1D;

    private static final Double DEFAULT_TRANCHE_MAX = 0D;
    private static final Double UPDATED_TRANCHE_MAX = 1D;
    private static final Double SMALLER_TRANCHE_MAX = 0D - 1D;

    private static final Double DEFAULT_PRIX_EURO = 0D;
    private static final Double UPDATED_PRIX_EURO = 1D;
    private static final Double SMALLER_PRIX_EURO = 0D - 1D;

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tarifs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TarifRepository tarifRepository;

    @Autowired
    private TarifMapper tarifMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTarifMockMvc;

    private Tarif tarif;

    private Tarif insertedTarif;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarif createEntity() {
        return new Tarif()
            .trancheMin(DEFAULT_TRANCHE_MIN)
            .trancheMax(DEFAULT_TRANCHE_MAX)
            .prixEuro(DEFAULT_PRIX_EURO)
            .actif(DEFAULT_ACTIF)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarif createUpdatedEntity() {
        return new Tarif()
            .trancheMin(UPDATED_TRANCHE_MIN)
            .trancheMax(UPDATED_TRANCHE_MAX)
            .prixEuro(UPDATED_PRIX_EURO)
            .actif(UPDATED_ACTIF)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        tarif = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTarif != null) {
            tarifRepository.delete(insertedTarif);
            insertedTarif = null;
        }
    }

    @Test
    @Transactional
    void createTarif() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tarif
        TarifDTO tarifDTO = tarifMapper.toDto(tarif);
        var returnedTarifDTO = om.readValue(
            restTarifMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TarifDTO.class
        );

        // Validate the Tarif in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTarif = tarifMapper.toEntity(returnedTarifDTO);
        assertTarifUpdatableFieldsEquals(returnedTarif, getPersistedTarif(returnedTarif));

        insertedTarif = returnedTarif;
    }

    @Test
    @Transactional
    void createTarifWithExistingId() throws Exception {
        // Create the Tarif with an existing ID
        tarif.setId(1L);
        TarifDTO tarifDTO = tarifMapper.toDto(tarif);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrancheMinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tarif.setTrancheMin(null);

        // Create the Tarif, which fails.
        TarifDTO tarifDTO = tarifMapper.toDto(tarif);

        restTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrixEuroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tarif.setPrixEuro(null);

        // Create the Tarif, which fails.
        TarifDTO tarifDTO = tarifMapper.toDto(tarif);

        restTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tarif.setActif(null);

        // Create the Tarif, which fails.
        TarifDTO tarifDTO = tarifMapper.toDto(tarif);

        restTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tarif.setDateDebut(null);

        // Create the Tarif, which fails.
        TarifDTO tarifDTO = tarifMapper.toDto(tarif);

        restTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTarifs() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList
        restTarifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarif.getId().intValue())))
            .andExpect(jsonPath("$.[*].trancheMin").value(hasItem(DEFAULT_TRANCHE_MIN)))
            .andExpect(jsonPath("$.[*].trancheMax").value(hasItem(DEFAULT_TRANCHE_MAX)))
            .andExpect(jsonPath("$.[*].prixEuro").value(hasItem(DEFAULT_PRIX_EURO)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getTarif() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get the tarif
        restTarifMockMvc
            .perform(get(ENTITY_API_URL_ID, tarif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tarif.getId().intValue()))
            .andExpect(jsonPath("$.trancheMin").value(DEFAULT_TRANCHE_MIN))
            .andExpect(jsonPath("$.trancheMax").value(DEFAULT_TRANCHE_MAX))
            .andExpect(jsonPath("$.prixEuro").value(DEFAULT_PRIX_EURO))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getTarifsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        Long id = tarif.getId();

        defaultTarifFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTarifFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTarifFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMin equals to
        defaultTarifFiltering("trancheMin.equals=" + DEFAULT_TRANCHE_MIN, "trancheMin.equals=" + UPDATED_TRANCHE_MIN);
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMin in
        defaultTarifFiltering("trancheMin.in=" + DEFAULT_TRANCHE_MIN + "," + UPDATED_TRANCHE_MIN, "trancheMin.in=" + UPDATED_TRANCHE_MIN);
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMin is not null
        defaultTarifFiltering("trancheMin.specified=true", "trancheMin.specified=false");
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMin is greater than or equal to
        defaultTarifFiltering(
            "trancheMin.greaterThanOrEqual=" + DEFAULT_TRANCHE_MIN,
            "trancheMin.greaterThanOrEqual=" + UPDATED_TRANCHE_MIN
        );
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMin is less than or equal to
        defaultTarifFiltering("trancheMin.lessThanOrEqual=" + DEFAULT_TRANCHE_MIN, "trancheMin.lessThanOrEqual=" + SMALLER_TRANCHE_MIN);
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMinIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMin is less than
        defaultTarifFiltering("trancheMin.lessThan=" + UPDATED_TRANCHE_MIN, "trancheMin.lessThan=" + DEFAULT_TRANCHE_MIN);
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMin is greater than
        defaultTarifFiltering("trancheMin.greaterThan=" + SMALLER_TRANCHE_MIN, "trancheMin.greaterThan=" + DEFAULT_TRANCHE_MIN);
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMax equals to
        defaultTarifFiltering("trancheMax.equals=" + DEFAULT_TRANCHE_MAX, "trancheMax.equals=" + UPDATED_TRANCHE_MAX);
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMaxIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMax in
        defaultTarifFiltering("trancheMax.in=" + DEFAULT_TRANCHE_MAX + "," + UPDATED_TRANCHE_MAX, "trancheMax.in=" + UPDATED_TRANCHE_MAX);
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMax is not null
        defaultTarifFiltering("trancheMax.specified=true", "trancheMax.specified=false");
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMax is greater than or equal to
        defaultTarifFiltering(
            "trancheMax.greaterThanOrEqual=" + DEFAULT_TRANCHE_MAX,
            "trancheMax.greaterThanOrEqual=" + UPDATED_TRANCHE_MAX
        );
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMax is less than or equal to
        defaultTarifFiltering("trancheMax.lessThanOrEqual=" + DEFAULT_TRANCHE_MAX, "trancheMax.lessThanOrEqual=" + SMALLER_TRANCHE_MAX);
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMaxIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMax is less than
        defaultTarifFiltering("trancheMax.lessThan=" + UPDATED_TRANCHE_MAX, "trancheMax.lessThan=" + DEFAULT_TRANCHE_MAX);
    }

    @Test
    @Transactional
    void getAllTarifsByTrancheMaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where trancheMax is greater than
        defaultTarifFiltering("trancheMax.greaterThan=" + SMALLER_TRANCHE_MAX, "trancheMax.greaterThan=" + DEFAULT_TRANCHE_MAX);
    }

    @Test
    @Transactional
    void getAllTarifsByPrixEuroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where prixEuro equals to
        defaultTarifFiltering("prixEuro.equals=" + DEFAULT_PRIX_EURO, "prixEuro.equals=" + UPDATED_PRIX_EURO);
    }

    @Test
    @Transactional
    void getAllTarifsByPrixEuroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where prixEuro in
        defaultTarifFiltering("prixEuro.in=" + DEFAULT_PRIX_EURO + "," + UPDATED_PRIX_EURO, "prixEuro.in=" + UPDATED_PRIX_EURO);
    }

    @Test
    @Transactional
    void getAllTarifsByPrixEuroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where prixEuro is not null
        defaultTarifFiltering("prixEuro.specified=true", "prixEuro.specified=false");
    }

    @Test
    @Transactional
    void getAllTarifsByPrixEuroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where prixEuro is greater than or equal to
        defaultTarifFiltering("prixEuro.greaterThanOrEqual=" + DEFAULT_PRIX_EURO, "prixEuro.greaterThanOrEqual=" + UPDATED_PRIX_EURO);
    }

    @Test
    @Transactional
    void getAllTarifsByPrixEuroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where prixEuro is less than or equal to
        defaultTarifFiltering("prixEuro.lessThanOrEqual=" + DEFAULT_PRIX_EURO, "prixEuro.lessThanOrEqual=" + SMALLER_PRIX_EURO);
    }

    @Test
    @Transactional
    void getAllTarifsByPrixEuroIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where prixEuro is less than
        defaultTarifFiltering("prixEuro.lessThan=" + UPDATED_PRIX_EURO, "prixEuro.lessThan=" + DEFAULT_PRIX_EURO);
    }

    @Test
    @Transactional
    void getAllTarifsByPrixEuroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where prixEuro is greater than
        defaultTarifFiltering("prixEuro.greaterThan=" + SMALLER_PRIX_EURO, "prixEuro.greaterThan=" + DEFAULT_PRIX_EURO);
    }

    @Test
    @Transactional
    void getAllTarifsByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where actif equals to
        defaultTarifFiltering("actif.equals=" + DEFAULT_ACTIF, "actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllTarifsByActifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where actif in
        defaultTarifFiltering("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF, "actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllTarifsByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where actif is not null
        defaultTarifFiltering("actif.specified=true", "actif.specified=false");
    }

    @Test
    @Transactional
    void getAllTarifsByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateDebut equals to
        defaultTarifFiltering("dateDebut.equals=" + DEFAULT_DATE_DEBUT, "dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllTarifsByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateDebut in
        defaultTarifFiltering("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT, "dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllTarifsByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateDebut is not null
        defaultTarifFiltering("dateDebut.specified=true", "dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllTarifsByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateDebut is greater than or equal to
        defaultTarifFiltering("dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT, "dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllTarifsByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateDebut is less than or equal to
        defaultTarifFiltering("dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT, "dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllTarifsByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateDebut is less than
        defaultTarifFiltering("dateDebut.lessThan=" + UPDATED_DATE_DEBUT, "dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllTarifsByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateDebut is greater than
        defaultTarifFiltering("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT, "dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllTarifsByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateFin equals to
        defaultTarifFiltering("dateFin.equals=" + DEFAULT_DATE_FIN, "dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTarifsByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateFin in
        defaultTarifFiltering("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN, "dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTarifsByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateFin is not null
        defaultTarifFiltering("dateFin.specified=true", "dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllTarifsByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateFin is greater than or equal to
        defaultTarifFiltering("dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN, "dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTarifsByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateFin is less than or equal to
        defaultTarifFiltering("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN, "dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTarifsByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateFin is less than
        defaultTarifFiltering("dateFin.lessThan=" + UPDATED_DATE_FIN, "dateFin.lessThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTarifsByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where dateFin is greater than
        defaultTarifFiltering("dateFin.greaterThan=" + SMALLER_DATE_FIN, "dateFin.greaterThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTarifsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where description equals to
        defaultTarifFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTarifsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where description in
        defaultTarifFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTarifsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where description is not null
        defaultTarifFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTarifsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where description contains
        defaultTarifFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTarifsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where description does not contain
        defaultTarifFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    private void defaultTarifFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTarifShouldBeFound(shouldBeFound);
        defaultTarifShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTarifShouldBeFound(String filter) throws Exception {
        restTarifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarif.getId().intValue())))
            .andExpect(jsonPath("$.[*].trancheMin").value(hasItem(DEFAULT_TRANCHE_MIN)))
            .andExpect(jsonPath("$.[*].trancheMax").value(hasItem(DEFAULT_TRANCHE_MAX)))
            .andExpect(jsonPath("$.[*].prixEuro").value(hasItem(DEFAULT_PRIX_EURO)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restTarifMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTarifShouldNotBeFound(String filter) throws Exception {
        restTarifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTarifMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTarif() throws Exception {
        // Get the tarif
        restTarifMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTarif() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarif
        Tarif updatedTarif = tarifRepository.findById(tarif.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTarif are not directly saved in db
        em.detach(updatedTarif);
        updatedTarif
            .trancheMin(UPDATED_TRANCHE_MIN)
            .trancheMax(UPDATED_TRANCHE_MAX)
            .prixEuro(UPDATED_PRIX_EURO)
            .actif(UPDATED_ACTIF)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .description(UPDATED_DESCRIPTION);
        TarifDTO tarifDTO = tarifMapper.toDto(updatedTarif);

        restTarifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarifDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTarifToMatchAllProperties(updatedTarif);
    }

    @Test
    @Transactional
    void putNonExistingTarif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarif.setId(longCount.incrementAndGet());

        // Create the Tarif
        TarifDTO tarifDTO = tarifMapper.toDto(tarif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarifDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTarif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarif.setId(longCount.incrementAndGet());

        // Create the Tarif
        TarifDTO tarifDTO = tarifMapper.toDto(tarif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tarifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTarif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarif.setId(longCount.incrementAndGet());

        // Create the Tarif
        TarifDTO tarifDTO = tarifMapper.toDto(tarif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTarifWithPatch() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarif using partial update
        Tarif partialUpdatedTarif = new Tarif();
        partialUpdatedTarif.setId(tarif.getId());

        partialUpdatedTarif.trancheMax(UPDATED_TRANCHE_MAX).prixEuro(UPDATED_PRIX_EURO).description(UPDATED_DESCRIPTION);

        restTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTarif))
            )
            .andExpect(status().isOk());

        // Validate the Tarif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTarifUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTarif, tarif), getPersistedTarif(tarif));
    }

    @Test
    @Transactional
    void fullUpdateTarifWithPatch() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarif using partial update
        Tarif partialUpdatedTarif = new Tarif();
        partialUpdatedTarif.setId(tarif.getId());

        partialUpdatedTarif
            .trancheMin(UPDATED_TRANCHE_MIN)
            .trancheMax(UPDATED_TRANCHE_MAX)
            .prixEuro(UPDATED_PRIX_EURO)
            .actif(UPDATED_ACTIF)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .description(UPDATED_DESCRIPTION);

        restTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTarif))
            )
            .andExpect(status().isOk());

        // Validate the Tarif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTarifUpdatableFieldsEquals(partialUpdatedTarif, getPersistedTarif(partialUpdatedTarif));
    }

    @Test
    @Transactional
    void patchNonExistingTarif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarif.setId(longCount.incrementAndGet());

        // Create the Tarif
        TarifDTO tarifDTO = tarifMapper.toDto(tarif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tarifDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tarifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTarif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarif.setId(longCount.incrementAndGet());

        // Create the Tarif
        TarifDTO tarifDTO = tarifMapper.toDto(tarif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tarifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTarif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarif.setId(longCount.incrementAndGet());

        // Create the Tarif
        TarifDTO tarifDTO = tarifMapper.toDto(tarif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tarifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTarif() throws Exception {
        // Initialize the database
        insertedTarif = tarifRepository.saveAndFlush(tarif);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tarif
        restTarifMockMvc
            .perform(delete(ENTITY_API_URL_ID, tarif.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tarifRepository.count();
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

    protected Tarif getPersistedTarif(Tarif tarif) {
        return tarifRepository.findById(tarif.getId()).orElseThrow();
    }

    protected void assertPersistedTarifToMatchAllProperties(Tarif expectedTarif) {
        assertTarifAllPropertiesEquals(expectedTarif, getPersistedTarif(expectedTarif));
    }

    protected void assertPersistedTarifToMatchUpdatableProperties(Tarif expectedTarif) {
        assertTarifAllUpdatablePropertiesEquals(expectedTarif, getPersistedTarif(expectedTarif));
    }
}
