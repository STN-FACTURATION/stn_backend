package sn.stn.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.stn.facturation.domain.MouvementAsserts.*;
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
import sn.stn.facturation.domain.Mouvement;
import sn.stn.facturation.domain.Remorqueur;
import sn.stn.facturation.domain.enumeration.TypeOperation;
import sn.stn.facturation.repository.MouvementRepository;
import sn.stn.facturation.service.MouvementService;
import sn.stn.facturation.service.dto.MouvementDTO;
import sn.stn.facturation.service.mapper.MouvementMapper;

/**
 * Integration tests for the {@link MouvementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MouvementResourceIT {

    private static final TypeOperation DEFAULT_TYPE = TypeOperation.DEPLACEMENT_UNITAIRE;
    private static final TypeOperation UPDATED_TYPE = TypeOperation.ACCOSTAGE;

    private static final String DEFAULT_POSTE_A = "AAAAAAAAAA";
    private static final String UPDATED_POSTE_A = "BBBBBBBBBB";

    private static final String DEFAULT_POSTE_B = "AAAAAAAAAA";
    private static final String UPDATED_POSTE_B = "BBBBBBBBBB";

    private static final Double DEFAULT_DUREE = 0D;
    private static final Double UPDATED_DUREE = 1D;
    private static final Double SMALLER_DUREE = 0D - 1D;

    private static final Double DEFAULT_MONTANT_CALCULE = 0D;
    private static final Double UPDATED_MONTANT_CALCULE = 1D;
    private static final Double SMALLER_MONTANT_CALCULE = 0D - 1D;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mouvements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MouvementRepository mouvementRepository;

    @Mock
    private MouvementRepository mouvementRepositoryMock;

    @Autowired
    private MouvementMapper mouvementMapper;

    @Mock
    private MouvementService mouvementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMouvementMockMvc;

    private Mouvement mouvement;

    private Mouvement insertedMouvement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mouvement createEntity(EntityManager em) {
        Mouvement mouvement = new Mouvement()
            .type(DEFAULT_TYPE)
            .posteA(DEFAULT_POSTE_A)
            .posteB(DEFAULT_POSTE_B)
            .duree(DEFAULT_DUREE)
            .montantCalcule(DEFAULT_MONTANT_CALCULE)
            .libelle(DEFAULT_LIBELLE);
        // Add required entity
        Facture facture;
        if (TestUtil.findAll(em, Facture.class).isEmpty()) {
            facture = FactureResourceIT.createEntity(em);
            em.persist(facture);
            em.flush();
        } else {
            facture = TestUtil.findAll(em, Facture.class).get(0);
        }
        mouvement.setFacture(facture);
        return mouvement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mouvement createUpdatedEntity(EntityManager em) {
        Mouvement updatedMouvement = new Mouvement()
            .type(UPDATED_TYPE)
            .posteA(UPDATED_POSTE_A)
            .posteB(UPDATED_POSTE_B)
            .duree(UPDATED_DUREE)
            .montantCalcule(UPDATED_MONTANT_CALCULE)
            .libelle(UPDATED_LIBELLE);
        // Add required entity
        Facture facture;
        if (TestUtil.findAll(em, Facture.class).isEmpty()) {
            facture = FactureResourceIT.createUpdatedEntity(em);
            em.persist(facture);
            em.flush();
        } else {
            facture = TestUtil.findAll(em, Facture.class).get(0);
        }
        updatedMouvement.setFacture(facture);
        return updatedMouvement;
    }

    @BeforeEach
    void initTest() {
        mouvement = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMouvement != null) {
            mouvementRepository.delete(insertedMouvement);
            insertedMouvement = null;
        }
    }

    @Test
    @Transactional
    void createMouvement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Mouvement
        MouvementDTO mouvementDTO = mouvementMapper.toDto(mouvement);
        var returnedMouvementDTO = om.readValue(
            restMouvementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mouvementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MouvementDTO.class
        );

        // Validate the Mouvement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMouvement = mouvementMapper.toEntity(returnedMouvementDTO);
        assertMouvementUpdatableFieldsEquals(returnedMouvement, getPersistedMouvement(returnedMouvement));

        insertedMouvement = returnedMouvement;
    }

    @Test
    @Transactional
    void createMouvementWithExistingId() throws Exception {
        // Create the Mouvement with an existing ID
        mouvement.setId(1L);
        MouvementDTO mouvementDTO = mouvementMapper.toDto(mouvement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMouvementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mouvementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mouvement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mouvement.setType(null);

        // Create the Mouvement, which fails.
        MouvementDTO mouvementDTO = mouvementMapper.toDto(mouvement);

        restMouvementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mouvementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantCalculeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mouvement.setMontantCalcule(null);

        // Create the Mouvement, which fails.
        MouvementDTO mouvementDTO = mouvementMapper.toDto(mouvement);

        restMouvementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mouvementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMouvements() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList
        restMouvementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mouvement.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].posteA").value(hasItem(DEFAULT_POSTE_A)))
            .andExpect(jsonPath("$.[*].posteB").value(hasItem(DEFAULT_POSTE_B)))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].montantCalcule").value(hasItem(DEFAULT_MONTANT_CALCULE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMouvementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(mouvementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMouvementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mouvementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMouvementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(mouvementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMouvementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(mouvementRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMouvement() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get the mouvement
        restMouvementMockMvc
            .perform(get(ENTITY_API_URL_ID, mouvement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mouvement.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.posteA").value(DEFAULT_POSTE_A))
            .andExpect(jsonPath("$.posteB").value(DEFAULT_POSTE_B))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE))
            .andExpect(jsonPath("$.montantCalcule").value(DEFAULT_MONTANT_CALCULE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getMouvementsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        Long id = mouvement.getId();

        defaultMouvementFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMouvementFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMouvementFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMouvementsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where type equals to
        defaultMouvementFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMouvementsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where type in
        defaultMouvementFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMouvementsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where type is not null
        defaultMouvementFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllMouvementsByPosteAIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where posteA equals to
        defaultMouvementFiltering("posteA.equals=" + DEFAULT_POSTE_A, "posteA.equals=" + UPDATED_POSTE_A);
    }

    @Test
    @Transactional
    void getAllMouvementsByPosteAIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where posteA in
        defaultMouvementFiltering("posteA.in=" + DEFAULT_POSTE_A + "," + UPDATED_POSTE_A, "posteA.in=" + UPDATED_POSTE_A);
    }

    @Test
    @Transactional
    void getAllMouvementsByPosteAIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where posteA is not null
        defaultMouvementFiltering("posteA.specified=true", "posteA.specified=false");
    }

    @Test
    @Transactional
    void getAllMouvementsByPosteAContainsSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where posteA contains
        defaultMouvementFiltering("posteA.contains=" + DEFAULT_POSTE_A, "posteA.contains=" + UPDATED_POSTE_A);
    }

    @Test
    @Transactional
    void getAllMouvementsByPosteANotContainsSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where posteA does not contain
        defaultMouvementFiltering("posteA.doesNotContain=" + UPDATED_POSTE_A, "posteA.doesNotContain=" + DEFAULT_POSTE_A);
    }

    @Test
    @Transactional
    void getAllMouvementsByPosteBIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where posteB equals to
        defaultMouvementFiltering("posteB.equals=" + DEFAULT_POSTE_B, "posteB.equals=" + UPDATED_POSTE_B);
    }

    @Test
    @Transactional
    void getAllMouvementsByPosteBIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where posteB in
        defaultMouvementFiltering("posteB.in=" + DEFAULT_POSTE_B + "," + UPDATED_POSTE_B, "posteB.in=" + UPDATED_POSTE_B);
    }

    @Test
    @Transactional
    void getAllMouvementsByPosteBIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where posteB is not null
        defaultMouvementFiltering("posteB.specified=true", "posteB.specified=false");
    }

    @Test
    @Transactional
    void getAllMouvementsByPosteBContainsSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where posteB contains
        defaultMouvementFiltering("posteB.contains=" + DEFAULT_POSTE_B, "posteB.contains=" + UPDATED_POSTE_B);
    }

    @Test
    @Transactional
    void getAllMouvementsByPosteBNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where posteB does not contain
        defaultMouvementFiltering("posteB.doesNotContain=" + UPDATED_POSTE_B, "posteB.doesNotContain=" + DEFAULT_POSTE_B);
    }

    @Test
    @Transactional
    void getAllMouvementsByDureeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where duree equals to
        defaultMouvementFiltering("duree.equals=" + DEFAULT_DUREE, "duree.equals=" + UPDATED_DUREE);
    }

    @Test
    @Transactional
    void getAllMouvementsByDureeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where duree in
        defaultMouvementFiltering("duree.in=" + DEFAULT_DUREE + "," + UPDATED_DUREE, "duree.in=" + UPDATED_DUREE);
    }

    @Test
    @Transactional
    void getAllMouvementsByDureeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where duree is not null
        defaultMouvementFiltering("duree.specified=true", "duree.specified=false");
    }

    @Test
    @Transactional
    void getAllMouvementsByDureeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where duree is greater than or equal to
        defaultMouvementFiltering("duree.greaterThanOrEqual=" + DEFAULT_DUREE, "duree.greaterThanOrEqual=" + UPDATED_DUREE);
    }

    @Test
    @Transactional
    void getAllMouvementsByDureeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where duree is less than or equal to
        defaultMouvementFiltering("duree.lessThanOrEqual=" + DEFAULT_DUREE, "duree.lessThanOrEqual=" + SMALLER_DUREE);
    }

    @Test
    @Transactional
    void getAllMouvementsByDureeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where duree is less than
        defaultMouvementFiltering("duree.lessThan=" + UPDATED_DUREE, "duree.lessThan=" + DEFAULT_DUREE);
    }

    @Test
    @Transactional
    void getAllMouvementsByDureeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where duree is greater than
        defaultMouvementFiltering("duree.greaterThan=" + SMALLER_DUREE, "duree.greaterThan=" + DEFAULT_DUREE);
    }

    @Test
    @Transactional
    void getAllMouvementsByMontantCalculeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where montantCalcule equals to
        defaultMouvementFiltering("montantCalcule.equals=" + DEFAULT_MONTANT_CALCULE, "montantCalcule.equals=" + UPDATED_MONTANT_CALCULE);
    }

    @Test
    @Transactional
    void getAllMouvementsByMontantCalculeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where montantCalcule in
        defaultMouvementFiltering(
            "montantCalcule.in=" + DEFAULT_MONTANT_CALCULE + "," + UPDATED_MONTANT_CALCULE,
            "montantCalcule.in=" + UPDATED_MONTANT_CALCULE
        );
    }

    @Test
    @Transactional
    void getAllMouvementsByMontantCalculeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where montantCalcule is not null
        defaultMouvementFiltering("montantCalcule.specified=true", "montantCalcule.specified=false");
    }

    @Test
    @Transactional
    void getAllMouvementsByMontantCalculeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where montantCalcule is greater than or equal to
        defaultMouvementFiltering(
            "montantCalcule.greaterThanOrEqual=" + DEFAULT_MONTANT_CALCULE,
            "montantCalcule.greaterThanOrEqual=" + UPDATED_MONTANT_CALCULE
        );
    }

    @Test
    @Transactional
    void getAllMouvementsByMontantCalculeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where montantCalcule is less than or equal to
        defaultMouvementFiltering(
            "montantCalcule.lessThanOrEqual=" + DEFAULT_MONTANT_CALCULE,
            "montantCalcule.lessThanOrEqual=" + SMALLER_MONTANT_CALCULE
        );
    }

    @Test
    @Transactional
    void getAllMouvementsByMontantCalculeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where montantCalcule is less than
        defaultMouvementFiltering(
            "montantCalcule.lessThan=" + UPDATED_MONTANT_CALCULE,
            "montantCalcule.lessThan=" + DEFAULT_MONTANT_CALCULE
        );
    }

    @Test
    @Transactional
    void getAllMouvementsByMontantCalculeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where montantCalcule is greater than
        defaultMouvementFiltering(
            "montantCalcule.greaterThan=" + SMALLER_MONTANT_CALCULE,
            "montantCalcule.greaterThan=" + DEFAULT_MONTANT_CALCULE
        );
    }

    @Test
    @Transactional
    void getAllMouvementsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where libelle equals to
        defaultMouvementFiltering("libelle.equals=" + DEFAULT_LIBELLE, "libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllMouvementsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where libelle in
        defaultMouvementFiltering("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE, "libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllMouvementsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where libelle is not null
        defaultMouvementFiltering("libelle.specified=true", "libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllMouvementsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where libelle contains
        defaultMouvementFiltering("libelle.contains=" + DEFAULT_LIBELLE, "libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllMouvementsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        // Get all the mouvementList where libelle does not contain
        defaultMouvementFiltering("libelle.doesNotContain=" + UPDATED_LIBELLE, "libelle.doesNotContain=" + DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void getAllMouvementsByRemorqueurIsEqualToSomething() throws Exception {
        Remorqueur remorqueur;
        if (TestUtil.findAll(em, Remorqueur.class).isEmpty()) {
            mouvementRepository.saveAndFlush(mouvement);
            remorqueur = RemorqueurResourceIT.createEntity();
        } else {
            remorqueur = TestUtil.findAll(em, Remorqueur.class).get(0);
        }
        em.persist(remorqueur);
        em.flush();
        mouvement.setRemorqueur(remorqueur);
        mouvementRepository.saveAndFlush(mouvement);
        Long remorqueurId = remorqueur.getId();
        // Get all the mouvementList where remorqueur equals to remorqueurId
        defaultMouvementShouldBeFound("remorqueurId.equals=" + remorqueurId);

        // Get all the mouvementList where remorqueur equals to (remorqueurId + 1)
        defaultMouvementShouldNotBeFound("remorqueurId.equals=" + (remorqueurId + 1));
    }

    @Test
    @Transactional
    void getAllMouvementsByFactureIsEqualToSomething() throws Exception {
        Facture facture;
        if (TestUtil.findAll(em, Facture.class).isEmpty()) {
            mouvementRepository.saveAndFlush(mouvement);
            facture = FactureResourceIT.createEntity(em);
        } else {
            facture = TestUtil.findAll(em, Facture.class).get(0);
        }
        em.persist(facture);
        em.flush();
        mouvement.setFacture(facture);
        mouvementRepository.saveAndFlush(mouvement);
        Long factureId = facture.getId();
        // Get all the mouvementList where facture equals to factureId
        defaultMouvementShouldBeFound("factureId.equals=" + factureId);

        // Get all the mouvementList where facture equals to (factureId + 1)
        defaultMouvementShouldNotBeFound("factureId.equals=" + (factureId + 1));
    }

    private void defaultMouvementFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMouvementShouldBeFound(shouldBeFound);
        defaultMouvementShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMouvementShouldBeFound(String filter) throws Exception {
        restMouvementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mouvement.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].posteA").value(hasItem(DEFAULT_POSTE_A)))
            .andExpect(jsonPath("$.[*].posteB").value(hasItem(DEFAULT_POSTE_B)))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].montantCalcule").value(hasItem(DEFAULT_MONTANT_CALCULE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));

        // Check, that the count call also returns 1
        restMouvementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMouvementShouldNotBeFound(String filter) throws Exception {
        restMouvementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMouvementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMouvement() throws Exception {
        // Get the mouvement
        restMouvementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMouvement() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mouvement
        Mouvement updatedMouvement = mouvementRepository.findById(mouvement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMouvement are not directly saved in db
        em.detach(updatedMouvement);
        updatedMouvement
            .type(UPDATED_TYPE)
            .posteA(UPDATED_POSTE_A)
            .posteB(UPDATED_POSTE_B)
            .duree(UPDATED_DUREE)
            .montantCalcule(UPDATED_MONTANT_CALCULE)
            .libelle(UPDATED_LIBELLE);
        MouvementDTO mouvementDTO = mouvementMapper.toDto(updatedMouvement);

        restMouvementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mouvementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mouvementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mouvement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMouvementToMatchAllProperties(updatedMouvement);
    }

    @Test
    @Transactional
    void putNonExistingMouvement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mouvement.setId(longCount.incrementAndGet());

        // Create the Mouvement
        MouvementDTO mouvementDTO = mouvementMapper.toDto(mouvement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMouvementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mouvementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mouvementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mouvement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMouvement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mouvement.setId(longCount.incrementAndGet());

        // Create the Mouvement
        MouvementDTO mouvementDTO = mouvementMapper.toDto(mouvement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMouvementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mouvementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mouvement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMouvement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mouvement.setId(longCount.incrementAndGet());

        // Create the Mouvement
        MouvementDTO mouvementDTO = mouvementMapper.toDto(mouvement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMouvementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mouvementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mouvement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMouvementWithPatch() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mouvement using partial update
        Mouvement partialUpdatedMouvement = new Mouvement();
        partialUpdatedMouvement.setId(mouvement.getId());

        partialUpdatedMouvement.duree(UPDATED_DUREE).libelle(UPDATED_LIBELLE);

        restMouvementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMouvement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMouvement))
            )
            .andExpect(status().isOk());

        // Validate the Mouvement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMouvementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMouvement, mouvement),
            getPersistedMouvement(mouvement)
        );
    }

    @Test
    @Transactional
    void fullUpdateMouvementWithPatch() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mouvement using partial update
        Mouvement partialUpdatedMouvement = new Mouvement();
        partialUpdatedMouvement.setId(mouvement.getId());

        partialUpdatedMouvement
            .type(UPDATED_TYPE)
            .posteA(UPDATED_POSTE_A)
            .posteB(UPDATED_POSTE_B)
            .duree(UPDATED_DUREE)
            .montantCalcule(UPDATED_MONTANT_CALCULE)
            .libelle(UPDATED_LIBELLE);

        restMouvementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMouvement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMouvement))
            )
            .andExpect(status().isOk());

        // Validate the Mouvement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMouvementUpdatableFieldsEquals(partialUpdatedMouvement, getPersistedMouvement(partialUpdatedMouvement));
    }

    @Test
    @Transactional
    void patchNonExistingMouvement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mouvement.setId(longCount.incrementAndGet());

        // Create the Mouvement
        MouvementDTO mouvementDTO = mouvementMapper.toDto(mouvement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMouvementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mouvementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mouvementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mouvement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMouvement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mouvement.setId(longCount.incrementAndGet());

        // Create the Mouvement
        MouvementDTO mouvementDTO = mouvementMapper.toDto(mouvement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMouvementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mouvementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mouvement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMouvement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mouvement.setId(longCount.incrementAndGet());

        // Create the Mouvement
        MouvementDTO mouvementDTO = mouvementMapper.toDto(mouvement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMouvementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mouvementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mouvement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMouvement() throws Exception {
        // Initialize the database
        insertedMouvement = mouvementRepository.saveAndFlush(mouvement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mouvement
        restMouvementMockMvc
            .perform(delete(ENTITY_API_URL_ID, mouvement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mouvementRepository.count();
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

    protected Mouvement getPersistedMouvement(Mouvement mouvement) {
        return mouvementRepository.findById(mouvement.getId()).orElseThrow();
    }

    protected void assertPersistedMouvementToMatchAllProperties(Mouvement expectedMouvement) {
        assertMouvementAllPropertiesEquals(expectedMouvement, getPersistedMouvement(expectedMouvement));
    }

    protected void assertPersistedMouvementToMatchUpdatableProperties(Mouvement expectedMouvement) {
        assertMouvementAllUpdatablePropertiesEquals(expectedMouvement, getPersistedMouvement(expectedMouvement));
    }
}
