package sn.stn.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.stn.facturation.domain.HistoriqueTarifAsserts.*;
import static sn.stn.facturation.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import sn.stn.facturation.domain.HistoriqueTarif;
import sn.stn.facturation.domain.Tarif;
import sn.stn.facturation.repository.HistoriqueTarifRepository;
import sn.stn.facturation.service.dto.HistoriqueTarifDTO;
import sn.stn.facturation.service.mapper.HistoriqueTarifMapper;

/**
 * Integration tests for the {@link HistoriqueTarifResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoriqueTarifResourceIT {

    private static final Double DEFAULT_ANCIENNE_VALEUR = 1D;
    private static final Double UPDATED_ANCIENNE_VALEUR = 2D;
    private static final Double SMALLER_ANCIENNE_VALEUR = 1D - 1D;

    private static final Double DEFAULT_NOUVELLE_VALEUR = 1D;
    private static final Double UPDATED_NOUVELLE_VALEUR = 2D;
    private static final Double SMALLER_NOUVELLE_VALEUR = 1D - 1D;

    private static final Instant DEFAULT_DATE_MODIFICATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MODIFICATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_MODIFIE_PAR_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIE_PAR_LOGIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/historique-tarifs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistoriqueTarifRepository historiqueTarifRepository;

    @Autowired
    private HistoriqueTarifMapper historiqueTarifMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueTarifMockMvc;

    private HistoriqueTarif historiqueTarif;

    private HistoriqueTarif insertedHistoriqueTarif;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueTarif createEntity(EntityManager em) {
        HistoriqueTarif historiqueTarif = new HistoriqueTarif()
            .ancienneValeur(DEFAULT_ANCIENNE_VALEUR)
            .nouvelleValeur(DEFAULT_NOUVELLE_VALEUR)
            .dateModification(DEFAULT_DATE_MODIFICATION)
            .commentaire(DEFAULT_COMMENTAIRE)
            .modifieParLogin(DEFAULT_MODIFIE_PAR_LOGIN);
        // Add required entity
        Tarif tarif;
        if (TestUtil.findAll(em, Tarif.class).isEmpty()) {
            tarif = TarifResourceIT.createEntity();
            em.persist(tarif);
            em.flush();
        } else {
            tarif = TestUtil.findAll(em, Tarif.class).get(0);
        }
        historiqueTarif.setTarif(tarif);
        return historiqueTarif;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueTarif createUpdatedEntity(EntityManager em) {
        HistoriqueTarif updatedHistoriqueTarif = new HistoriqueTarif()
            .ancienneValeur(UPDATED_ANCIENNE_VALEUR)
            .nouvelleValeur(UPDATED_NOUVELLE_VALEUR)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .commentaire(UPDATED_COMMENTAIRE)
            .modifieParLogin(UPDATED_MODIFIE_PAR_LOGIN);
        // Add required entity
        Tarif tarif;
        if (TestUtil.findAll(em, Tarif.class).isEmpty()) {
            tarif = TarifResourceIT.createUpdatedEntity();
            em.persist(tarif);
            em.flush();
        } else {
            tarif = TestUtil.findAll(em, Tarif.class).get(0);
        }
        updatedHistoriqueTarif.setTarif(tarif);
        return updatedHistoriqueTarif;
    }

    @BeforeEach
    void initTest() {
        historiqueTarif = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedHistoriqueTarif != null) {
            historiqueTarifRepository.delete(insertedHistoriqueTarif);
            insertedHistoriqueTarif = null;
        }
    }

    @Test
    @Transactional
    void createHistoriqueTarif() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HistoriqueTarif
        HistoriqueTarifDTO historiqueTarifDTO = historiqueTarifMapper.toDto(historiqueTarif);
        var returnedHistoriqueTarifDTO = om.readValue(
            restHistoriqueTarifMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueTarifDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HistoriqueTarifDTO.class
        );

        // Validate the HistoriqueTarif in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistoriqueTarif = historiqueTarifMapper.toEntity(returnedHistoriqueTarifDTO);
        assertHistoriqueTarifUpdatableFieldsEquals(returnedHistoriqueTarif, getPersistedHistoriqueTarif(returnedHistoriqueTarif));

        insertedHistoriqueTarif = returnedHistoriqueTarif;
    }

    @Test
    @Transactional
    void createHistoriqueTarifWithExistingId() throws Exception {
        // Create the HistoriqueTarif with an existing ID
        historiqueTarif.setId(1L);
        HistoriqueTarifDTO historiqueTarifDTO = historiqueTarifMapper.toDto(historiqueTarif);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriqueTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueTarifDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueTarif in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAncienneValeurIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiqueTarif.setAncienneValeur(null);

        // Create the HistoriqueTarif, which fails.
        HistoriqueTarifDTO historiqueTarifDTO = historiqueTarifMapper.toDto(historiqueTarif);

        restHistoriqueTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueTarifDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNouvelleValeurIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiqueTarif.setNouvelleValeur(null);

        // Create the HistoriqueTarif, which fails.
        HistoriqueTarifDTO historiqueTarifDTO = historiqueTarifMapper.toDto(historiqueTarif);

        restHistoriqueTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueTarifDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModificationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiqueTarif.setDateModification(null);

        // Create the HistoriqueTarif, which fails.
        HistoriqueTarifDTO historiqueTarifDTO = historiqueTarifMapper.toDto(historiqueTarif);

        restHistoriqueTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueTarifDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifs() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList
        restHistoriqueTarifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueTarif.getId().intValue())))
            .andExpect(jsonPath("$.[*].ancienneValeur").value(hasItem(DEFAULT_ANCIENNE_VALEUR)))
            .andExpect(jsonPath("$.[*].nouvelleValeur").value(hasItem(DEFAULT_NOUVELLE_VALEUR)))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].modifieParLogin").value(hasItem(DEFAULT_MODIFIE_PAR_LOGIN)));
    }

    @Test
    @Transactional
    void getHistoriqueTarif() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get the historiqueTarif
        restHistoriqueTarifMockMvc
            .perform(get(ENTITY_API_URL_ID, historiqueTarif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiqueTarif.getId().intValue()))
            .andExpect(jsonPath("$.ancienneValeur").value(DEFAULT_ANCIENNE_VALEUR))
            .andExpect(jsonPath("$.nouvelleValeur").value(DEFAULT_NOUVELLE_VALEUR))
            .andExpect(jsonPath("$.dateModification").value(DEFAULT_DATE_MODIFICATION.toString()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.modifieParLogin").value(DEFAULT_MODIFIE_PAR_LOGIN));
    }

    @Test
    @Transactional
    void getHistoriqueTarifsByIdFiltering() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        Long id = historiqueTarif.getId();

        defaultHistoriqueTarifFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHistoriqueTarifFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHistoriqueTarifFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByAncienneValeurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where ancienneValeur equals to
        defaultHistoriqueTarifFiltering(
            "ancienneValeur.equals=" + DEFAULT_ANCIENNE_VALEUR,
            "ancienneValeur.equals=" + UPDATED_ANCIENNE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByAncienneValeurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where ancienneValeur in
        defaultHistoriqueTarifFiltering(
            "ancienneValeur.in=" + DEFAULT_ANCIENNE_VALEUR + "," + UPDATED_ANCIENNE_VALEUR,
            "ancienneValeur.in=" + UPDATED_ANCIENNE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByAncienneValeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where ancienneValeur is not null
        defaultHistoriqueTarifFiltering("ancienneValeur.specified=true", "ancienneValeur.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByAncienneValeurIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where ancienneValeur is greater than or equal to
        defaultHistoriqueTarifFiltering(
            "ancienneValeur.greaterThanOrEqual=" + DEFAULT_ANCIENNE_VALEUR,
            "ancienneValeur.greaterThanOrEqual=" + UPDATED_ANCIENNE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByAncienneValeurIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where ancienneValeur is less than or equal to
        defaultHistoriqueTarifFiltering(
            "ancienneValeur.lessThanOrEqual=" + DEFAULT_ANCIENNE_VALEUR,
            "ancienneValeur.lessThanOrEqual=" + SMALLER_ANCIENNE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByAncienneValeurIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where ancienneValeur is less than
        defaultHistoriqueTarifFiltering(
            "ancienneValeur.lessThan=" + UPDATED_ANCIENNE_VALEUR,
            "ancienneValeur.lessThan=" + DEFAULT_ANCIENNE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByAncienneValeurIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where ancienneValeur is greater than
        defaultHistoriqueTarifFiltering(
            "ancienneValeur.greaterThan=" + SMALLER_ANCIENNE_VALEUR,
            "ancienneValeur.greaterThan=" + DEFAULT_ANCIENNE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByNouvelleValeurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where nouvelleValeur equals to
        defaultHistoriqueTarifFiltering(
            "nouvelleValeur.equals=" + DEFAULT_NOUVELLE_VALEUR,
            "nouvelleValeur.equals=" + UPDATED_NOUVELLE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByNouvelleValeurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where nouvelleValeur in
        defaultHistoriqueTarifFiltering(
            "nouvelleValeur.in=" + DEFAULT_NOUVELLE_VALEUR + "," + UPDATED_NOUVELLE_VALEUR,
            "nouvelleValeur.in=" + UPDATED_NOUVELLE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByNouvelleValeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where nouvelleValeur is not null
        defaultHistoriqueTarifFiltering("nouvelleValeur.specified=true", "nouvelleValeur.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByNouvelleValeurIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where nouvelleValeur is greater than or equal to
        defaultHistoriqueTarifFiltering(
            "nouvelleValeur.greaterThanOrEqual=" + DEFAULT_NOUVELLE_VALEUR,
            "nouvelleValeur.greaterThanOrEqual=" + UPDATED_NOUVELLE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByNouvelleValeurIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where nouvelleValeur is less than or equal to
        defaultHistoriqueTarifFiltering(
            "nouvelleValeur.lessThanOrEqual=" + DEFAULT_NOUVELLE_VALEUR,
            "nouvelleValeur.lessThanOrEqual=" + SMALLER_NOUVELLE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByNouvelleValeurIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where nouvelleValeur is less than
        defaultHistoriqueTarifFiltering(
            "nouvelleValeur.lessThan=" + UPDATED_NOUVELLE_VALEUR,
            "nouvelleValeur.lessThan=" + DEFAULT_NOUVELLE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByNouvelleValeurIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where nouvelleValeur is greater than
        defaultHistoriqueTarifFiltering(
            "nouvelleValeur.greaterThan=" + SMALLER_NOUVELLE_VALEUR,
            "nouvelleValeur.greaterThan=" + DEFAULT_NOUVELLE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByDateModificationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where dateModification equals to
        defaultHistoriqueTarifFiltering(
            "dateModification.equals=" + DEFAULT_DATE_MODIFICATION,
            "dateModification.equals=" + UPDATED_DATE_MODIFICATION
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByDateModificationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where dateModification in
        defaultHistoriqueTarifFiltering(
            "dateModification.in=" + DEFAULT_DATE_MODIFICATION + "," + UPDATED_DATE_MODIFICATION,
            "dateModification.in=" + UPDATED_DATE_MODIFICATION
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByDateModificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where dateModification is not null
        defaultHistoriqueTarifFiltering("dateModification.specified=true", "dateModification.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where commentaire equals to
        defaultHistoriqueTarifFiltering("commentaire.equals=" + DEFAULT_COMMENTAIRE, "commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where commentaire in
        defaultHistoriqueTarifFiltering(
            "commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE,
            "commentaire.in=" + UPDATED_COMMENTAIRE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where commentaire is not null
        defaultHistoriqueTarifFiltering("commentaire.specified=true", "commentaire.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByCommentaireContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where commentaire contains
        defaultHistoriqueTarifFiltering("commentaire.contains=" + DEFAULT_COMMENTAIRE, "commentaire.contains=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByCommentaireNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where commentaire does not contain
        defaultHistoriqueTarifFiltering(
            "commentaire.doesNotContain=" + UPDATED_COMMENTAIRE,
            "commentaire.doesNotContain=" + DEFAULT_COMMENTAIRE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByModifieParLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where modifieParLogin equals to
        defaultHistoriqueTarifFiltering(
            "modifieParLogin.equals=" + DEFAULT_MODIFIE_PAR_LOGIN,
            "modifieParLogin.equals=" + UPDATED_MODIFIE_PAR_LOGIN
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByModifieParLoginIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where modifieParLogin in
        defaultHistoriqueTarifFiltering(
            "modifieParLogin.in=" + DEFAULT_MODIFIE_PAR_LOGIN + "," + UPDATED_MODIFIE_PAR_LOGIN,
            "modifieParLogin.in=" + UPDATED_MODIFIE_PAR_LOGIN
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByModifieParLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where modifieParLogin is not null
        defaultHistoriqueTarifFiltering("modifieParLogin.specified=true", "modifieParLogin.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByModifieParLoginContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where modifieParLogin contains
        defaultHistoriqueTarifFiltering(
            "modifieParLogin.contains=" + DEFAULT_MODIFIE_PAR_LOGIN,
            "modifieParLogin.contains=" + UPDATED_MODIFIE_PAR_LOGIN
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByModifieParLoginNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        // Get all the historiqueTarifList where modifieParLogin does not contain
        defaultHistoriqueTarifFiltering(
            "modifieParLogin.doesNotContain=" + UPDATED_MODIFIE_PAR_LOGIN,
            "modifieParLogin.doesNotContain=" + DEFAULT_MODIFIE_PAR_LOGIN
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueTarifsByTarifIsEqualToSomething() throws Exception {
        Tarif tarif;
        if (TestUtil.findAll(em, Tarif.class).isEmpty()) {
            historiqueTarifRepository.saveAndFlush(historiqueTarif);
            tarif = TarifResourceIT.createEntity();
        } else {
            tarif = TestUtil.findAll(em, Tarif.class).get(0);
        }
        em.persist(tarif);
        em.flush();
        historiqueTarif.setTarif(tarif);
        historiqueTarifRepository.saveAndFlush(historiqueTarif);
        Long tarifId = tarif.getId();
        // Get all the historiqueTarifList where tarif equals to tarifId
        defaultHistoriqueTarifShouldBeFound("tarifId.equals=" + tarifId);

        // Get all the historiqueTarifList where tarif equals to (tarifId + 1)
        defaultHistoriqueTarifShouldNotBeFound("tarifId.equals=" + (tarifId + 1));
    }

    private void defaultHistoriqueTarifFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHistoriqueTarifShouldBeFound(shouldBeFound);
        defaultHistoriqueTarifShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoriqueTarifShouldBeFound(String filter) throws Exception {
        restHistoriqueTarifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueTarif.getId().intValue())))
            .andExpect(jsonPath("$.[*].ancienneValeur").value(hasItem(DEFAULT_ANCIENNE_VALEUR)))
            .andExpect(jsonPath("$.[*].nouvelleValeur").value(hasItem(DEFAULT_NOUVELLE_VALEUR)))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].modifieParLogin").value(hasItem(DEFAULT_MODIFIE_PAR_LOGIN)));

        // Check, that the count call also returns 1
        restHistoriqueTarifMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoriqueTarifShouldNotBeFound(String filter) throws Exception {
        restHistoriqueTarifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoriqueTarifMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHistoriqueTarif() throws Exception {
        // Get the historiqueTarif
        restHistoriqueTarifMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistoriqueTarif() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueTarif
        HistoriqueTarif updatedHistoriqueTarif = historiqueTarifRepository.findById(historiqueTarif.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHistoriqueTarif are not directly saved in db
        em.detach(updatedHistoriqueTarif);
        updatedHistoriqueTarif
            .ancienneValeur(UPDATED_ANCIENNE_VALEUR)
            .nouvelleValeur(UPDATED_NOUVELLE_VALEUR)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .commentaire(UPDATED_COMMENTAIRE)
            .modifieParLogin(UPDATED_MODIFIE_PAR_LOGIN);
        HistoriqueTarifDTO historiqueTarifDTO = historiqueTarifMapper.toDto(updatedHistoriqueTarif);

        restHistoriqueTarifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueTarifDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueTarifDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueTarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistoriqueTarifToMatchAllProperties(updatedHistoriqueTarif);
    }

    @Test
    @Transactional
    void putNonExistingHistoriqueTarif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueTarif.setId(longCount.incrementAndGet());

        // Create the HistoriqueTarif
        HistoriqueTarifDTO historiqueTarifDTO = historiqueTarifMapper.toDto(historiqueTarif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueTarifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueTarifDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueTarifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueTarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoriqueTarif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueTarif.setId(longCount.incrementAndGet());

        // Create the HistoriqueTarif
        HistoriqueTarifDTO historiqueTarifDTO = historiqueTarifMapper.toDto(historiqueTarif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueTarifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueTarifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueTarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoriqueTarif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueTarif.setId(longCount.incrementAndGet());

        // Create the HistoriqueTarif
        HistoriqueTarifDTO historiqueTarifDTO = historiqueTarifMapper.toDto(historiqueTarif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueTarifMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueTarifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriqueTarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoriqueTarifWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueTarif using partial update
        HistoriqueTarif partialUpdatedHistoriqueTarif = new HistoriqueTarif();
        partialUpdatedHistoriqueTarif.setId(historiqueTarif.getId());

        partialUpdatedHistoriqueTarif.nouvelleValeur(UPDATED_NOUVELLE_VALEUR);

        restHistoriqueTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriqueTarif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriqueTarif))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueTarif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriqueTarifUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistoriqueTarif, historiqueTarif),
            getPersistedHistoriqueTarif(historiqueTarif)
        );
    }

    @Test
    @Transactional
    void fullUpdateHistoriqueTarifWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueTarif using partial update
        HistoriqueTarif partialUpdatedHistoriqueTarif = new HistoriqueTarif();
        partialUpdatedHistoriqueTarif.setId(historiqueTarif.getId());

        partialUpdatedHistoriqueTarif
            .ancienneValeur(UPDATED_ANCIENNE_VALEUR)
            .nouvelleValeur(UPDATED_NOUVELLE_VALEUR)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .commentaire(UPDATED_COMMENTAIRE)
            .modifieParLogin(UPDATED_MODIFIE_PAR_LOGIN);

        restHistoriqueTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriqueTarif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriqueTarif))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueTarif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriqueTarifUpdatableFieldsEquals(
            partialUpdatedHistoriqueTarif,
            getPersistedHistoriqueTarif(partialUpdatedHistoriqueTarif)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHistoriqueTarif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueTarif.setId(longCount.incrementAndGet());

        // Create the HistoriqueTarif
        HistoriqueTarifDTO historiqueTarifDTO = historiqueTarifMapper.toDto(historiqueTarif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historiqueTarifDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiqueTarifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueTarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoriqueTarif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueTarif.setId(longCount.incrementAndGet());

        // Create the HistoriqueTarif
        HistoriqueTarifDTO historiqueTarifDTO = historiqueTarifMapper.toDto(historiqueTarif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiqueTarifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueTarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoriqueTarif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueTarif.setId(longCount.incrementAndGet());

        // Create the HistoriqueTarif
        HistoriqueTarifDTO historiqueTarifDTO = historiqueTarifMapper.toDto(historiqueTarif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueTarifMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(historiqueTarifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriqueTarif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoriqueTarif() throws Exception {
        // Initialize the database
        insertedHistoriqueTarif = historiqueTarifRepository.saveAndFlush(historiqueTarif);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historiqueTarif
        restHistoriqueTarifMockMvc
            .perform(delete(ENTITY_API_URL_ID, historiqueTarif.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historiqueTarifRepository.count();
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

    protected HistoriqueTarif getPersistedHistoriqueTarif(HistoriqueTarif historiqueTarif) {
        return historiqueTarifRepository.findById(historiqueTarif.getId()).orElseThrow();
    }

    protected void assertPersistedHistoriqueTarifToMatchAllProperties(HistoriqueTarif expectedHistoriqueTarif) {
        assertHistoriqueTarifAllPropertiesEquals(expectedHistoriqueTarif, getPersistedHistoriqueTarif(expectedHistoriqueTarif));
    }

    protected void assertPersistedHistoriqueTarifToMatchUpdatableProperties(HistoriqueTarif expectedHistoriqueTarif) {
        assertHistoriqueTarifAllUpdatablePropertiesEquals(expectedHistoriqueTarif, getPersistedHistoriqueTarif(expectedHistoriqueTarif));
    }
}
