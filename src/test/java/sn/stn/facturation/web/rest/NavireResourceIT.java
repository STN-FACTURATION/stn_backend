package sn.stn.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.stn.facturation.domain.NavireAsserts.*;
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
import sn.stn.facturation.domain.Client;
import sn.stn.facturation.domain.Navire;
import sn.stn.facturation.repository.NavireRepository;
import sn.stn.facturation.service.NavireService;
import sn.stn.facturation.service.dto.NavireDTO;
import sn.stn.facturation.service.mapper.NavireMapper;

/**
 * Integration tests for the {@link NavireResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NavireResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_IMO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_IMO = "BBBBBBBBBB";

    private static final Double DEFAULT_JAUGE_BRUTE = 0D;
    private static final Double UPDATED_JAUGE_BRUTE = 1D;
    private static final Double SMALLER_JAUGE_BRUTE = 0D - 1D;

    private static final Double DEFAULT_LONGUEUR = 0D;
    private static final Double UPDATED_LONGUEUR = 1D;
    private static final Double SMALLER_LONGUEUR = 0D - 1D;

    private static final Double DEFAULT_LARGEUR = 0D;
    private static final Double UPDATED_LARGEUR = 1D;
    private static final Double SMALLER_LARGEUR = 0D - 1D;

    private static final Double DEFAULT_TIRANT = 0D;
    private static final Double UPDATED_TIRANT = 1D;
    private static final Double SMALLER_TIRANT = 0D - 1D;

    private static final String DEFAULT_PAVILLON = "AAAAAAAAAA";
    private static final String UPDATED_PAVILLON = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/navires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NavireRepository navireRepository;

    @Mock
    private NavireRepository navireRepositoryMock;

    @Autowired
    private NavireMapper navireMapper;

    @Mock
    private NavireService navireServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNavireMockMvc;

    private Navire navire;

    private Navire insertedNavire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Navire createEntity(EntityManager em) {
        Navire navire = new Navire()
            .nom(DEFAULT_NOM)
            .numeroImo(DEFAULT_NUMERO_IMO)
            .jaugeBrute(DEFAULT_JAUGE_BRUTE)
            .longueur(DEFAULT_LONGUEUR)
            .largeur(DEFAULT_LARGEUR)
            .tirant(DEFAULT_TIRANT)
            .pavillon(DEFAULT_PAVILLON)
            .actif(DEFAULT_ACTIF);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        navire.setClient(client);
        return navire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Navire createUpdatedEntity(EntityManager em) {
        Navire updatedNavire = new Navire()
            .nom(UPDATED_NOM)
            .numeroImo(UPDATED_NUMERO_IMO)
            .jaugeBrute(UPDATED_JAUGE_BRUTE)
            .longueur(UPDATED_LONGUEUR)
            .largeur(UPDATED_LARGEUR)
            .tirant(UPDATED_TIRANT)
            .pavillon(UPDATED_PAVILLON)
            .actif(UPDATED_ACTIF);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        updatedNavire.setClient(client);
        return updatedNavire;
    }

    @BeforeEach
    void initTest() {
        navire = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedNavire != null) {
            navireRepository.delete(insertedNavire);
            insertedNavire = null;
        }
    }

    @Test
    @Transactional
    void createNavire() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Navire
        NavireDTO navireDTO = navireMapper.toDto(navire);
        var returnedNavireDTO = om.readValue(
            restNavireMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(navireDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NavireDTO.class
        );

        // Validate the Navire in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNavire = navireMapper.toEntity(returnedNavireDTO);
        assertNavireUpdatableFieldsEquals(returnedNavire, getPersistedNavire(returnedNavire));

        insertedNavire = returnedNavire;
    }

    @Test
    @Transactional
    void createNavireWithExistingId() throws Exception {
        // Create the Navire with an existing ID
        navire.setId(1L);
        NavireDTO navireDTO = navireMapper.toDto(navire);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNavireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(navireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Navire in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        navire.setNom(null);

        // Create the Navire, which fails.
        NavireDTO navireDTO = navireMapper.toDto(navire);

        restNavireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(navireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkJaugeBruteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        navire.setJaugeBrute(null);

        // Create the Navire, which fails.
        NavireDTO navireDTO = navireMapper.toDto(navire);

        restNavireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(navireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        navire.setActif(null);

        // Create the Navire, which fails.
        NavireDTO navireDTO = navireMapper.toDto(navire);

        restNavireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(navireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNavires() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList
        restNavireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(navire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].numeroImo").value(hasItem(DEFAULT_NUMERO_IMO)))
            .andExpect(jsonPath("$.[*].jaugeBrute").value(hasItem(DEFAULT_JAUGE_BRUTE)))
            .andExpect(jsonPath("$.[*].longueur").value(hasItem(DEFAULT_LONGUEUR)))
            .andExpect(jsonPath("$.[*].largeur").value(hasItem(DEFAULT_LARGEUR)))
            .andExpect(jsonPath("$.[*].tirant").value(hasItem(DEFAULT_TIRANT)))
            .andExpect(jsonPath("$.[*].pavillon").value(hasItem(DEFAULT_PAVILLON)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNaviresWithEagerRelationshipsIsEnabled() throws Exception {
        when(navireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNavireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(navireServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNaviresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(navireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNavireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(navireRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNavire() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get the navire
        restNavireMockMvc
            .perform(get(ENTITY_API_URL_ID, navire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(navire.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.numeroImo").value(DEFAULT_NUMERO_IMO))
            .andExpect(jsonPath("$.jaugeBrute").value(DEFAULT_JAUGE_BRUTE))
            .andExpect(jsonPath("$.longueur").value(DEFAULT_LONGUEUR))
            .andExpect(jsonPath("$.largeur").value(DEFAULT_LARGEUR))
            .andExpect(jsonPath("$.tirant").value(DEFAULT_TIRANT))
            .andExpect(jsonPath("$.pavillon").value(DEFAULT_PAVILLON))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getNaviresByIdFiltering() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        Long id = navire.getId();

        defaultNavireFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNavireFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNavireFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNaviresByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where nom equals to
        defaultNavireFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllNaviresByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where nom in
        defaultNavireFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllNaviresByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where nom is not null
        defaultNavireFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllNaviresByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where nom contains
        defaultNavireFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllNaviresByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where nom does not contain
        defaultNavireFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllNaviresByNumeroImoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where numeroImo equals to
        defaultNavireFiltering("numeroImo.equals=" + DEFAULT_NUMERO_IMO, "numeroImo.equals=" + UPDATED_NUMERO_IMO);
    }

    @Test
    @Transactional
    void getAllNaviresByNumeroImoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where numeroImo in
        defaultNavireFiltering("numeroImo.in=" + DEFAULT_NUMERO_IMO + "," + UPDATED_NUMERO_IMO, "numeroImo.in=" + UPDATED_NUMERO_IMO);
    }

    @Test
    @Transactional
    void getAllNaviresByNumeroImoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where numeroImo is not null
        defaultNavireFiltering("numeroImo.specified=true", "numeroImo.specified=false");
    }

    @Test
    @Transactional
    void getAllNaviresByNumeroImoContainsSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where numeroImo contains
        defaultNavireFiltering("numeroImo.contains=" + DEFAULT_NUMERO_IMO, "numeroImo.contains=" + UPDATED_NUMERO_IMO);
    }

    @Test
    @Transactional
    void getAllNaviresByNumeroImoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where numeroImo does not contain
        defaultNavireFiltering("numeroImo.doesNotContain=" + UPDATED_NUMERO_IMO, "numeroImo.doesNotContain=" + DEFAULT_NUMERO_IMO);
    }

    @Test
    @Transactional
    void getAllNaviresByJaugeBruteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where jaugeBrute equals to
        defaultNavireFiltering("jaugeBrute.equals=" + DEFAULT_JAUGE_BRUTE, "jaugeBrute.equals=" + UPDATED_JAUGE_BRUTE);
    }

    @Test
    @Transactional
    void getAllNaviresByJaugeBruteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where jaugeBrute in
        defaultNavireFiltering("jaugeBrute.in=" + DEFAULT_JAUGE_BRUTE + "," + UPDATED_JAUGE_BRUTE, "jaugeBrute.in=" + UPDATED_JAUGE_BRUTE);
    }

    @Test
    @Transactional
    void getAllNaviresByJaugeBruteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where jaugeBrute is not null
        defaultNavireFiltering("jaugeBrute.specified=true", "jaugeBrute.specified=false");
    }

    @Test
    @Transactional
    void getAllNaviresByJaugeBruteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where jaugeBrute is greater than or equal to
        defaultNavireFiltering(
            "jaugeBrute.greaterThanOrEqual=" + DEFAULT_JAUGE_BRUTE,
            "jaugeBrute.greaterThanOrEqual=" + UPDATED_JAUGE_BRUTE
        );
    }

    @Test
    @Transactional
    void getAllNaviresByJaugeBruteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where jaugeBrute is less than or equal to
        defaultNavireFiltering("jaugeBrute.lessThanOrEqual=" + DEFAULT_JAUGE_BRUTE, "jaugeBrute.lessThanOrEqual=" + SMALLER_JAUGE_BRUTE);
    }

    @Test
    @Transactional
    void getAllNaviresByJaugeBruteIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where jaugeBrute is less than
        defaultNavireFiltering("jaugeBrute.lessThan=" + UPDATED_JAUGE_BRUTE, "jaugeBrute.lessThan=" + DEFAULT_JAUGE_BRUTE);
    }

    @Test
    @Transactional
    void getAllNaviresByJaugeBruteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where jaugeBrute is greater than
        defaultNavireFiltering("jaugeBrute.greaterThan=" + SMALLER_JAUGE_BRUTE, "jaugeBrute.greaterThan=" + DEFAULT_JAUGE_BRUTE);
    }

    @Test
    @Transactional
    void getAllNaviresByLongueurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where longueur equals to
        defaultNavireFiltering("longueur.equals=" + DEFAULT_LONGUEUR, "longueur.equals=" + UPDATED_LONGUEUR);
    }

    @Test
    @Transactional
    void getAllNaviresByLongueurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where longueur in
        defaultNavireFiltering("longueur.in=" + DEFAULT_LONGUEUR + "," + UPDATED_LONGUEUR, "longueur.in=" + UPDATED_LONGUEUR);
    }

    @Test
    @Transactional
    void getAllNaviresByLongueurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where longueur is not null
        defaultNavireFiltering("longueur.specified=true", "longueur.specified=false");
    }

    @Test
    @Transactional
    void getAllNaviresByLongueurIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where longueur is greater than or equal to
        defaultNavireFiltering("longueur.greaterThanOrEqual=" + DEFAULT_LONGUEUR, "longueur.greaterThanOrEqual=" + UPDATED_LONGUEUR);
    }

    @Test
    @Transactional
    void getAllNaviresByLongueurIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where longueur is less than or equal to
        defaultNavireFiltering("longueur.lessThanOrEqual=" + DEFAULT_LONGUEUR, "longueur.lessThanOrEqual=" + SMALLER_LONGUEUR);
    }

    @Test
    @Transactional
    void getAllNaviresByLongueurIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where longueur is less than
        defaultNavireFiltering("longueur.lessThan=" + UPDATED_LONGUEUR, "longueur.lessThan=" + DEFAULT_LONGUEUR);
    }

    @Test
    @Transactional
    void getAllNaviresByLongueurIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where longueur is greater than
        defaultNavireFiltering("longueur.greaterThan=" + SMALLER_LONGUEUR, "longueur.greaterThan=" + DEFAULT_LONGUEUR);
    }

    @Test
    @Transactional
    void getAllNaviresByLargeurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where largeur equals to
        defaultNavireFiltering("largeur.equals=" + DEFAULT_LARGEUR, "largeur.equals=" + UPDATED_LARGEUR);
    }

    @Test
    @Transactional
    void getAllNaviresByLargeurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where largeur in
        defaultNavireFiltering("largeur.in=" + DEFAULT_LARGEUR + "," + UPDATED_LARGEUR, "largeur.in=" + UPDATED_LARGEUR);
    }

    @Test
    @Transactional
    void getAllNaviresByLargeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where largeur is not null
        defaultNavireFiltering("largeur.specified=true", "largeur.specified=false");
    }

    @Test
    @Transactional
    void getAllNaviresByLargeurIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where largeur is greater than or equal to
        defaultNavireFiltering("largeur.greaterThanOrEqual=" + DEFAULT_LARGEUR, "largeur.greaterThanOrEqual=" + UPDATED_LARGEUR);
    }

    @Test
    @Transactional
    void getAllNaviresByLargeurIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where largeur is less than or equal to
        defaultNavireFiltering("largeur.lessThanOrEqual=" + DEFAULT_LARGEUR, "largeur.lessThanOrEqual=" + SMALLER_LARGEUR);
    }

    @Test
    @Transactional
    void getAllNaviresByLargeurIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where largeur is less than
        defaultNavireFiltering("largeur.lessThan=" + UPDATED_LARGEUR, "largeur.lessThan=" + DEFAULT_LARGEUR);
    }

    @Test
    @Transactional
    void getAllNaviresByLargeurIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where largeur is greater than
        defaultNavireFiltering("largeur.greaterThan=" + SMALLER_LARGEUR, "largeur.greaterThan=" + DEFAULT_LARGEUR);
    }

    @Test
    @Transactional
    void getAllNaviresByTirantIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where tirant equals to
        defaultNavireFiltering("tirant.equals=" + DEFAULT_TIRANT, "tirant.equals=" + UPDATED_TIRANT);
    }

    @Test
    @Transactional
    void getAllNaviresByTirantIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where tirant in
        defaultNavireFiltering("tirant.in=" + DEFAULT_TIRANT + "," + UPDATED_TIRANT, "tirant.in=" + UPDATED_TIRANT);
    }

    @Test
    @Transactional
    void getAllNaviresByTirantIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where tirant is not null
        defaultNavireFiltering("tirant.specified=true", "tirant.specified=false");
    }

    @Test
    @Transactional
    void getAllNaviresByTirantIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where tirant is greater than or equal to
        defaultNavireFiltering("tirant.greaterThanOrEqual=" + DEFAULT_TIRANT, "tirant.greaterThanOrEqual=" + UPDATED_TIRANT);
    }

    @Test
    @Transactional
    void getAllNaviresByTirantIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where tirant is less than or equal to
        defaultNavireFiltering("tirant.lessThanOrEqual=" + DEFAULT_TIRANT, "tirant.lessThanOrEqual=" + SMALLER_TIRANT);
    }

    @Test
    @Transactional
    void getAllNaviresByTirantIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where tirant is less than
        defaultNavireFiltering("tirant.lessThan=" + UPDATED_TIRANT, "tirant.lessThan=" + DEFAULT_TIRANT);
    }

    @Test
    @Transactional
    void getAllNaviresByTirantIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where tirant is greater than
        defaultNavireFiltering("tirant.greaterThan=" + SMALLER_TIRANT, "tirant.greaterThan=" + DEFAULT_TIRANT);
    }

    @Test
    @Transactional
    void getAllNaviresByPavillonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where pavillon equals to
        defaultNavireFiltering("pavillon.equals=" + DEFAULT_PAVILLON, "pavillon.equals=" + UPDATED_PAVILLON);
    }

    @Test
    @Transactional
    void getAllNaviresByPavillonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where pavillon in
        defaultNavireFiltering("pavillon.in=" + DEFAULT_PAVILLON + "," + UPDATED_PAVILLON, "pavillon.in=" + UPDATED_PAVILLON);
    }

    @Test
    @Transactional
    void getAllNaviresByPavillonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where pavillon is not null
        defaultNavireFiltering("pavillon.specified=true", "pavillon.specified=false");
    }

    @Test
    @Transactional
    void getAllNaviresByPavillonContainsSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where pavillon contains
        defaultNavireFiltering("pavillon.contains=" + DEFAULT_PAVILLON, "pavillon.contains=" + UPDATED_PAVILLON);
    }

    @Test
    @Transactional
    void getAllNaviresByPavillonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where pavillon does not contain
        defaultNavireFiltering("pavillon.doesNotContain=" + UPDATED_PAVILLON, "pavillon.doesNotContain=" + DEFAULT_PAVILLON);
    }

    @Test
    @Transactional
    void getAllNaviresByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where actif equals to
        defaultNavireFiltering("actif.equals=" + DEFAULT_ACTIF, "actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllNaviresByActifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where actif in
        defaultNavireFiltering("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF, "actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllNaviresByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        // Get all the navireList where actif is not null
        defaultNavireFiltering("actif.specified=true", "actif.specified=false");
    }

    @Test
    @Transactional
    void getAllNaviresByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            navireRepository.saveAndFlush(navire);
            client = ClientResourceIT.createEntity();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        navire.setClient(client);
        navireRepository.saveAndFlush(navire);
        Long clientId = client.getId();
        // Get all the navireList where client equals to clientId
        defaultNavireShouldBeFound("clientId.equals=" + clientId);

        // Get all the navireList where client equals to (clientId + 1)
        defaultNavireShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    private void defaultNavireFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNavireShouldBeFound(shouldBeFound);
        defaultNavireShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNavireShouldBeFound(String filter) throws Exception {
        restNavireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(navire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].numeroImo").value(hasItem(DEFAULT_NUMERO_IMO)))
            .andExpect(jsonPath("$.[*].jaugeBrute").value(hasItem(DEFAULT_JAUGE_BRUTE)))
            .andExpect(jsonPath("$.[*].longueur").value(hasItem(DEFAULT_LONGUEUR)))
            .andExpect(jsonPath("$.[*].largeur").value(hasItem(DEFAULT_LARGEUR)))
            .andExpect(jsonPath("$.[*].tirant").value(hasItem(DEFAULT_TIRANT)))
            .andExpect(jsonPath("$.[*].pavillon").value(hasItem(DEFAULT_PAVILLON)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));

        // Check, that the count call also returns 1
        restNavireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNavireShouldNotBeFound(String filter) throws Exception {
        restNavireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNavireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNavire() throws Exception {
        // Get the navire
        restNavireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNavire() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the navire
        Navire updatedNavire = navireRepository.findById(navire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNavire are not directly saved in db
        em.detach(updatedNavire);
        updatedNavire
            .nom(UPDATED_NOM)
            .numeroImo(UPDATED_NUMERO_IMO)
            .jaugeBrute(UPDATED_JAUGE_BRUTE)
            .longueur(UPDATED_LONGUEUR)
            .largeur(UPDATED_LARGEUR)
            .tirant(UPDATED_TIRANT)
            .pavillon(UPDATED_PAVILLON)
            .actif(UPDATED_ACTIF);
        NavireDTO navireDTO = navireMapper.toDto(updatedNavire);

        restNavireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, navireDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(navireDTO))
            )
            .andExpect(status().isOk());

        // Validate the Navire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNavireToMatchAllProperties(updatedNavire);
    }

    @Test
    @Transactional
    void putNonExistingNavire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        navire.setId(longCount.incrementAndGet());

        // Create the Navire
        NavireDTO navireDTO = navireMapper.toDto(navire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNavireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, navireDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(navireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Navire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNavire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        navire.setId(longCount.incrementAndGet());

        // Create the Navire
        NavireDTO navireDTO = navireMapper.toDto(navire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNavireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(navireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Navire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNavire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        navire.setId(longCount.incrementAndGet());

        // Create the Navire
        NavireDTO navireDTO = navireMapper.toDto(navire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNavireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(navireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Navire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNavireWithPatch() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the navire using partial update
        Navire partialUpdatedNavire = new Navire();
        partialUpdatedNavire.setId(navire.getId());

        partialUpdatedNavire.nom(UPDATED_NOM).longueur(UPDATED_LONGUEUR).tirant(UPDATED_TIRANT).pavillon(UPDATED_PAVILLON);

        restNavireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNavire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNavire))
            )
            .andExpect(status().isOk());

        // Validate the Navire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNavireUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedNavire, navire), getPersistedNavire(navire));
    }

    @Test
    @Transactional
    void fullUpdateNavireWithPatch() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the navire using partial update
        Navire partialUpdatedNavire = new Navire();
        partialUpdatedNavire.setId(navire.getId());

        partialUpdatedNavire
            .nom(UPDATED_NOM)
            .numeroImo(UPDATED_NUMERO_IMO)
            .jaugeBrute(UPDATED_JAUGE_BRUTE)
            .longueur(UPDATED_LONGUEUR)
            .largeur(UPDATED_LARGEUR)
            .tirant(UPDATED_TIRANT)
            .pavillon(UPDATED_PAVILLON)
            .actif(UPDATED_ACTIF);

        restNavireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNavire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNavire))
            )
            .andExpect(status().isOk());

        // Validate the Navire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNavireUpdatableFieldsEquals(partialUpdatedNavire, getPersistedNavire(partialUpdatedNavire));
    }

    @Test
    @Transactional
    void patchNonExistingNavire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        navire.setId(longCount.incrementAndGet());

        // Create the Navire
        NavireDTO navireDTO = navireMapper.toDto(navire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNavireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, navireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(navireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Navire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNavire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        navire.setId(longCount.incrementAndGet());

        // Create the Navire
        NavireDTO navireDTO = navireMapper.toDto(navire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNavireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(navireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Navire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNavire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        navire.setId(longCount.incrementAndGet());

        // Create the Navire
        NavireDTO navireDTO = navireMapper.toDto(navire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNavireMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(navireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Navire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNavire() throws Exception {
        // Initialize the database
        insertedNavire = navireRepository.saveAndFlush(navire);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the navire
        restNavireMockMvc
            .perform(delete(ENTITY_API_URL_ID, navire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return navireRepository.count();
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

    protected Navire getPersistedNavire(Navire navire) {
        return navireRepository.findById(navire.getId()).orElseThrow();
    }

    protected void assertPersistedNavireToMatchAllProperties(Navire expectedNavire) {
        assertNavireAllPropertiesEquals(expectedNavire, getPersistedNavire(expectedNavire));
    }

    protected void assertPersistedNavireToMatchUpdatableProperties(Navire expectedNavire) {
        assertNavireAllUpdatablePropertiesEquals(expectedNavire, getPersistedNavire(expectedNavire));
    }
}
