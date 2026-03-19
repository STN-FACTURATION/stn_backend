package sn.stn.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.stn.facturation.domain.FactureAsserts.*;
import static sn.stn.facturation.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import sn.stn.facturation.domain.Facture;
import sn.stn.facturation.domain.Navire;
import sn.stn.facturation.domain.enumeration.DeviseFacture;
import sn.stn.facturation.domain.enumeration.StatutFacture;
import sn.stn.facturation.repository.FactureRepository;
import sn.stn.facturation.service.FactureService;
import sn.stn.facturation.service.dto.FactureDTO;
import sn.stn.facturation.service.mapper.FactureMapper;

/**
 * Integration tests for the {@link FactureResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FactureResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_EMISSION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EMISSION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_EMISSION = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_PAIEMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PAIEMENT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_PAIEMENT = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_VOLUME_M_3 = 0D;
    private static final Double UPDATED_VOLUME_M_3 = 1D;
    private static final Double SMALLER_VOLUME_M_3 = 0D - 1D;

    private static final Double DEFAULT_MONTANT_BASE_HT = 0D;
    private static final Double UPDATED_MONTANT_BASE_HT = 1D;
    private static final Double SMALLER_MONTANT_BASE_HT = 0D - 1D;

    private static final Double DEFAULT_MONTANT_SUPPLEMENTS_HT = 0D;
    private static final Double UPDATED_MONTANT_SUPPLEMENTS_HT = 1D;
    private static final Double SMALLER_MONTANT_SUPPLEMENTS_HT = 0D - 1D;

    private static final Double DEFAULT_MONTANT_HT = 0D;
    private static final Double UPDATED_MONTANT_HT = 1D;
    private static final Double SMALLER_MONTANT_HT = 0D - 1D;

    private static final Double DEFAULT_TAUX_TVA = 0D;
    private static final Double UPDATED_TAUX_TVA = 1D;
    private static final Double SMALLER_TAUX_TVA = 0D - 1D;

    private static final Double DEFAULT_MONTANT_TVA = 0D;
    private static final Double UPDATED_MONTANT_TVA = 1D;
    private static final Double SMALLER_MONTANT_TVA = 0D - 1D;

    private static final Double DEFAULT_MONTANT_TTC = 0D;
    private static final Double UPDATED_MONTANT_TTC = 1D;
    private static final Double SMALLER_MONTANT_TTC = 0D - 1D;

    private static final DeviseFacture DEFAULT_DEVISE = DeviseFacture.EUR;
    private static final DeviseFacture UPDATED_DEVISE = DeviseFacture.XOF;

    private static final Double DEFAULT_TAUX_CHANGE_CFA = 0D;
    private static final Double UPDATED_TAUX_CHANGE_CFA = 1D;
    private static final Double SMALLER_TAUX_CHANGE_CFA = 0D - 1D;

    private static final StatutFacture DEFAULT_STATUT = StatutFacture.BROUILLON;
    private static final StatutFacture UPDATED_STATUT = StatutFacture.VALIDEE;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_CHEMIN_PDF = "AAAAAAAAAA";
    private static final String UPDATED_CHEMIN_PDF = "BBBBBBBBBB";

    private static final String DEFAULT_CREE_PAR_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_CREE_PAR_LOGIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/factures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FactureRepository factureRepository;

    @Mock
    private FactureRepository factureRepositoryMock;

    @Autowired
    private FactureMapper factureMapper;

    @Mock
    private FactureService factureServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactureMockMvc;

    private Facture facture;

    private Facture insertedFacture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createEntity(EntityManager em) {
        Facture facture = new Facture()
            .numero(DEFAULT_NUMERO)
            .dateEmission(DEFAULT_DATE_EMISSION)
            .datePaiement(DEFAULT_DATE_PAIEMENT)
            .volumeM3(DEFAULT_VOLUME_M_3)
            .montantBaseHt(DEFAULT_MONTANT_BASE_HT)
            .montantSupplementsHt(DEFAULT_MONTANT_SUPPLEMENTS_HT)
            .montantHt(DEFAULT_MONTANT_HT)
            .tauxTva(DEFAULT_TAUX_TVA)
            .montantTva(DEFAULT_MONTANT_TVA)
            .montantTtc(DEFAULT_MONTANT_TTC)
            .devise(DEFAULT_DEVISE)
            .tauxChangeCfa(DEFAULT_TAUX_CHANGE_CFA)
            .statut(DEFAULT_STATUT)
            .notes(DEFAULT_NOTES)
            .cheminPdf(DEFAULT_CHEMIN_PDF)
            .creeParLogin(DEFAULT_CREE_PAR_LOGIN);
        // Add required entity
        Navire navire;
        if (TestUtil.findAll(em, Navire.class).isEmpty()) {
            navire = NavireResourceIT.createEntity(em);
            em.persist(navire);
            em.flush();
        } else {
            navire = TestUtil.findAll(em, Navire.class).get(0);
        }
        facture.setNavire(navire);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        facture.setClient(client);
        return facture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createUpdatedEntity(EntityManager em) {
        Facture updatedFacture = new Facture()
            .numero(UPDATED_NUMERO)
            .dateEmission(UPDATED_DATE_EMISSION)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .volumeM3(UPDATED_VOLUME_M_3)
            .montantBaseHt(UPDATED_MONTANT_BASE_HT)
            .montantSupplementsHt(UPDATED_MONTANT_SUPPLEMENTS_HT)
            .montantHt(UPDATED_MONTANT_HT)
            .tauxTva(UPDATED_TAUX_TVA)
            .montantTva(UPDATED_MONTANT_TVA)
            .montantTtc(UPDATED_MONTANT_TTC)
            .devise(UPDATED_DEVISE)
            .tauxChangeCfa(UPDATED_TAUX_CHANGE_CFA)
            .statut(UPDATED_STATUT)
            .notes(UPDATED_NOTES)
            .cheminPdf(UPDATED_CHEMIN_PDF)
            .creeParLogin(UPDATED_CREE_PAR_LOGIN);
        // Add required entity
        Navire navire;
        if (TestUtil.findAll(em, Navire.class).isEmpty()) {
            navire = NavireResourceIT.createUpdatedEntity(em);
            em.persist(navire);
            em.flush();
        } else {
            navire = TestUtil.findAll(em, Navire.class).get(0);
        }
        updatedFacture.setNavire(navire);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        updatedFacture.setClient(client);
        return updatedFacture;
    }

    @BeforeEach
    void initTest() {
        facture = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFacture != null) {
            factureRepository.delete(insertedFacture);
            insertedFacture = null;
        }
    }

    @Test
    @Transactional
    void createFacture() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);
        var returnedFactureDTO = om.readValue(
            restFactureMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FactureDTO.class
        );

        // Validate the Facture in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFacture = factureMapper.toEntity(returnedFactureDTO);
        assertFactureUpdatableFieldsEquals(returnedFacture, getPersistedFacture(returnedFacture));

        insertedFacture = returnedFacture;
    }

    @Test
    @Transactional
    void createFactureWithExistingId() throws Exception {
        // Create the Facture with an existing ID
        facture.setId(1L);
        FactureDTO factureDTO = factureMapper.toDto(facture);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setNumero(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateEmissionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setDateEmission(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVolumeM3IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setVolumeM3(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantBaseHtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setMontantBaseHt(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantSupplementsHtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setMontantSupplementsHt(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantHtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setMontantHt(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTauxTvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setTauxTva(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantTvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setMontantTva(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantTtcIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setMontantTtc(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeviseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setDevise(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setStatut(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFactures() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dateEmission").value(hasItem(DEFAULT_DATE_EMISSION.toString())))
            .andExpect(jsonPath("$.[*].datePaiement").value(hasItem(DEFAULT_DATE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].volumeM3").value(hasItem(DEFAULT_VOLUME_M_3)))
            .andExpect(jsonPath("$.[*].montantBaseHt").value(hasItem(DEFAULT_MONTANT_BASE_HT)))
            .andExpect(jsonPath("$.[*].montantSupplementsHt").value(hasItem(DEFAULT_MONTANT_SUPPLEMENTS_HT)))
            .andExpect(jsonPath("$.[*].montantHt").value(hasItem(DEFAULT_MONTANT_HT)))
            .andExpect(jsonPath("$.[*].tauxTva").value(hasItem(DEFAULT_TAUX_TVA)))
            .andExpect(jsonPath("$.[*].montantTva").value(hasItem(DEFAULT_MONTANT_TVA)))
            .andExpect(jsonPath("$.[*].montantTtc").value(hasItem(DEFAULT_MONTANT_TTC)))
            .andExpect(jsonPath("$.[*].devise").value(hasItem(DEFAULT_DEVISE.toString())))
            .andExpect(jsonPath("$.[*].tauxChangeCfa").value(hasItem(DEFAULT_TAUX_CHANGE_CFA)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].cheminPdf").value(hasItem(DEFAULT_CHEMIN_PDF)))
            .andExpect(jsonPath("$.[*].creeParLogin").value(hasItem(DEFAULT_CREE_PAR_LOGIN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacturesWithEagerRelationshipsIsEnabled() throws Exception {
        when(factureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFactureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(factureServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacturesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(factureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFactureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(factureRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFacture() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get the facture
        restFactureMockMvc
            .perform(get(ENTITY_API_URL_ID, facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facture.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.dateEmission").value(DEFAULT_DATE_EMISSION.toString()))
            .andExpect(jsonPath("$.datePaiement").value(DEFAULT_DATE_PAIEMENT.toString()))
            .andExpect(jsonPath("$.volumeM3").value(DEFAULT_VOLUME_M_3))
            .andExpect(jsonPath("$.montantBaseHt").value(DEFAULT_MONTANT_BASE_HT))
            .andExpect(jsonPath("$.montantSupplementsHt").value(DEFAULT_MONTANT_SUPPLEMENTS_HT))
            .andExpect(jsonPath("$.montantHt").value(DEFAULT_MONTANT_HT))
            .andExpect(jsonPath("$.tauxTva").value(DEFAULT_TAUX_TVA))
            .andExpect(jsonPath("$.montantTva").value(DEFAULT_MONTANT_TVA))
            .andExpect(jsonPath("$.montantTtc").value(DEFAULT_MONTANT_TTC))
            .andExpect(jsonPath("$.devise").value(DEFAULT_DEVISE.toString()))
            .andExpect(jsonPath("$.tauxChangeCfa").value(DEFAULT_TAUX_CHANGE_CFA))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.cheminPdf").value(DEFAULT_CHEMIN_PDF))
            .andExpect(jsonPath("$.creeParLogin").value(DEFAULT_CREE_PAR_LOGIN));
    }

    @Test
    @Transactional
    void getFacturesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        Long id = facture.getId();

        defaultFactureFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFactureFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFactureFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacturesByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero equals to
        defaultFactureFiltering("numero.equals=" + DEFAULT_NUMERO, "numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllFacturesByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero in
        defaultFactureFiltering("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO, "numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllFacturesByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero is not null
        defaultFactureFiltering("numero.specified=true", "numero.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByNumeroContainsSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero contains
        defaultFactureFiltering("numero.contains=" + DEFAULT_NUMERO, "numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllFacturesByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero does not contain
        defaultFactureFiltering("numero.doesNotContain=" + UPDATED_NUMERO, "numero.doesNotContain=" + DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void getAllFacturesByDateEmissionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateEmission equals to
        defaultFactureFiltering("dateEmission.equals=" + DEFAULT_DATE_EMISSION, "dateEmission.equals=" + UPDATED_DATE_EMISSION);
    }

    @Test
    @Transactional
    void getAllFacturesByDateEmissionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateEmission in
        defaultFactureFiltering(
            "dateEmission.in=" + DEFAULT_DATE_EMISSION + "," + UPDATED_DATE_EMISSION,
            "dateEmission.in=" + UPDATED_DATE_EMISSION
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDateEmissionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateEmission is not null
        defaultFactureFiltering("dateEmission.specified=true", "dateEmission.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByDateEmissionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateEmission is greater than or equal to
        defaultFactureFiltering(
            "dateEmission.greaterThanOrEqual=" + DEFAULT_DATE_EMISSION,
            "dateEmission.greaterThanOrEqual=" + UPDATED_DATE_EMISSION
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDateEmissionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateEmission is less than or equal to
        defaultFactureFiltering(
            "dateEmission.lessThanOrEqual=" + DEFAULT_DATE_EMISSION,
            "dateEmission.lessThanOrEqual=" + SMALLER_DATE_EMISSION
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDateEmissionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateEmission is less than
        defaultFactureFiltering("dateEmission.lessThan=" + UPDATED_DATE_EMISSION, "dateEmission.lessThan=" + DEFAULT_DATE_EMISSION);
    }

    @Test
    @Transactional
    void getAllFacturesByDateEmissionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateEmission is greater than
        defaultFactureFiltering("dateEmission.greaterThan=" + SMALLER_DATE_EMISSION, "dateEmission.greaterThan=" + DEFAULT_DATE_EMISSION);
    }

    @Test
    @Transactional
    void getAllFacturesByDatePaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where datePaiement equals to
        defaultFactureFiltering("datePaiement.equals=" + DEFAULT_DATE_PAIEMENT, "datePaiement.equals=" + UPDATED_DATE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllFacturesByDatePaiementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where datePaiement in
        defaultFactureFiltering(
            "datePaiement.in=" + DEFAULT_DATE_PAIEMENT + "," + UPDATED_DATE_PAIEMENT,
            "datePaiement.in=" + UPDATED_DATE_PAIEMENT
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDatePaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where datePaiement is not null
        defaultFactureFiltering("datePaiement.specified=true", "datePaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByDatePaiementIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where datePaiement is greater than or equal to
        defaultFactureFiltering(
            "datePaiement.greaterThanOrEqual=" + DEFAULT_DATE_PAIEMENT,
            "datePaiement.greaterThanOrEqual=" + UPDATED_DATE_PAIEMENT
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDatePaiementIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where datePaiement is less than or equal to
        defaultFactureFiltering(
            "datePaiement.lessThanOrEqual=" + DEFAULT_DATE_PAIEMENT,
            "datePaiement.lessThanOrEqual=" + SMALLER_DATE_PAIEMENT
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDatePaiementIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where datePaiement is less than
        defaultFactureFiltering("datePaiement.lessThan=" + UPDATED_DATE_PAIEMENT, "datePaiement.lessThan=" + DEFAULT_DATE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllFacturesByDatePaiementIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where datePaiement is greater than
        defaultFactureFiltering("datePaiement.greaterThan=" + SMALLER_DATE_PAIEMENT, "datePaiement.greaterThan=" + DEFAULT_DATE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllFacturesByVolumeM3IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where volumeM3 equals to
        defaultFactureFiltering("volumeM3.equals=" + DEFAULT_VOLUME_M_3, "volumeM3.equals=" + UPDATED_VOLUME_M_3);
    }

    @Test
    @Transactional
    void getAllFacturesByVolumeM3IsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where volumeM3 in
        defaultFactureFiltering("volumeM3.in=" + DEFAULT_VOLUME_M_3 + "," + UPDATED_VOLUME_M_3, "volumeM3.in=" + UPDATED_VOLUME_M_3);
    }

    @Test
    @Transactional
    void getAllFacturesByVolumeM3IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where volumeM3 is not null
        defaultFactureFiltering("volumeM3.specified=true", "volumeM3.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByVolumeM3IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where volumeM3 is greater than or equal to
        defaultFactureFiltering("volumeM3.greaterThanOrEqual=" + DEFAULT_VOLUME_M_3, "volumeM3.greaterThanOrEqual=" + UPDATED_VOLUME_M_3);
    }

    @Test
    @Transactional
    void getAllFacturesByVolumeM3IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where volumeM3 is less than or equal to
        defaultFactureFiltering("volumeM3.lessThanOrEqual=" + DEFAULT_VOLUME_M_3, "volumeM3.lessThanOrEqual=" + SMALLER_VOLUME_M_3);
    }

    @Test
    @Transactional
    void getAllFacturesByVolumeM3IsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where volumeM3 is less than
        defaultFactureFiltering("volumeM3.lessThan=" + UPDATED_VOLUME_M_3, "volumeM3.lessThan=" + DEFAULT_VOLUME_M_3);
    }

    @Test
    @Transactional
    void getAllFacturesByVolumeM3IsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where volumeM3 is greater than
        defaultFactureFiltering("volumeM3.greaterThan=" + SMALLER_VOLUME_M_3, "volumeM3.greaterThan=" + DEFAULT_VOLUME_M_3);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantBaseHtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantBaseHt equals to
        defaultFactureFiltering("montantBaseHt.equals=" + DEFAULT_MONTANT_BASE_HT, "montantBaseHt.equals=" + UPDATED_MONTANT_BASE_HT);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantBaseHtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantBaseHt in
        defaultFactureFiltering(
            "montantBaseHt.in=" + DEFAULT_MONTANT_BASE_HT + "," + UPDATED_MONTANT_BASE_HT,
            "montantBaseHt.in=" + UPDATED_MONTANT_BASE_HT
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantBaseHtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantBaseHt is not null
        defaultFactureFiltering("montantBaseHt.specified=true", "montantBaseHt.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByMontantBaseHtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantBaseHt is greater than or equal to
        defaultFactureFiltering(
            "montantBaseHt.greaterThanOrEqual=" + DEFAULT_MONTANT_BASE_HT,
            "montantBaseHt.greaterThanOrEqual=" + UPDATED_MONTANT_BASE_HT
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantBaseHtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantBaseHt is less than or equal to
        defaultFactureFiltering(
            "montantBaseHt.lessThanOrEqual=" + DEFAULT_MONTANT_BASE_HT,
            "montantBaseHt.lessThanOrEqual=" + SMALLER_MONTANT_BASE_HT
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantBaseHtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantBaseHt is less than
        defaultFactureFiltering("montantBaseHt.lessThan=" + UPDATED_MONTANT_BASE_HT, "montantBaseHt.lessThan=" + DEFAULT_MONTANT_BASE_HT);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantBaseHtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantBaseHt is greater than
        defaultFactureFiltering(
            "montantBaseHt.greaterThan=" + SMALLER_MONTANT_BASE_HT,
            "montantBaseHt.greaterThan=" + DEFAULT_MONTANT_BASE_HT
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantSupplementsHtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantSupplementsHt equals to
        defaultFactureFiltering(
            "montantSupplementsHt.equals=" + DEFAULT_MONTANT_SUPPLEMENTS_HT,
            "montantSupplementsHt.equals=" + UPDATED_MONTANT_SUPPLEMENTS_HT
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantSupplementsHtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantSupplementsHt in
        defaultFactureFiltering(
            "montantSupplementsHt.in=" + DEFAULT_MONTANT_SUPPLEMENTS_HT + "," + UPDATED_MONTANT_SUPPLEMENTS_HT,
            "montantSupplementsHt.in=" + UPDATED_MONTANT_SUPPLEMENTS_HT
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantSupplementsHtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantSupplementsHt is not null
        defaultFactureFiltering("montantSupplementsHt.specified=true", "montantSupplementsHt.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByMontantSupplementsHtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantSupplementsHt is greater than or equal to
        defaultFactureFiltering(
            "montantSupplementsHt.greaterThanOrEqual=" + DEFAULT_MONTANT_SUPPLEMENTS_HT,
            "montantSupplementsHt.greaterThanOrEqual=" + UPDATED_MONTANT_SUPPLEMENTS_HT
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantSupplementsHtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantSupplementsHt is less than or equal to
        defaultFactureFiltering(
            "montantSupplementsHt.lessThanOrEqual=" + DEFAULT_MONTANT_SUPPLEMENTS_HT,
            "montantSupplementsHt.lessThanOrEqual=" + SMALLER_MONTANT_SUPPLEMENTS_HT
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantSupplementsHtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantSupplementsHt is less than
        defaultFactureFiltering(
            "montantSupplementsHt.lessThan=" + UPDATED_MONTANT_SUPPLEMENTS_HT,
            "montantSupplementsHt.lessThan=" + DEFAULT_MONTANT_SUPPLEMENTS_HT
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantSupplementsHtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantSupplementsHt is greater than
        defaultFactureFiltering(
            "montantSupplementsHt.greaterThan=" + SMALLER_MONTANT_SUPPLEMENTS_HT,
            "montantSupplementsHt.greaterThan=" + DEFAULT_MONTANT_SUPPLEMENTS_HT
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantHtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantHt equals to
        defaultFactureFiltering("montantHt.equals=" + DEFAULT_MONTANT_HT, "montantHt.equals=" + UPDATED_MONTANT_HT);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantHtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantHt in
        defaultFactureFiltering("montantHt.in=" + DEFAULT_MONTANT_HT + "," + UPDATED_MONTANT_HT, "montantHt.in=" + UPDATED_MONTANT_HT);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantHtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantHt is not null
        defaultFactureFiltering("montantHt.specified=true", "montantHt.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByMontantHtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantHt is greater than or equal to
        defaultFactureFiltering("montantHt.greaterThanOrEqual=" + DEFAULT_MONTANT_HT, "montantHt.greaterThanOrEqual=" + UPDATED_MONTANT_HT);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantHtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantHt is less than or equal to
        defaultFactureFiltering("montantHt.lessThanOrEqual=" + DEFAULT_MONTANT_HT, "montantHt.lessThanOrEqual=" + SMALLER_MONTANT_HT);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantHtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantHt is less than
        defaultFactureFiltering("montantHt.lessThan=" + UPDATED_MONTANT_HT, "montantHt.lessThan=" + DEFAULT_MONTANT_HT);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantHtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantHt is greater than
        defaultFactureFiltering("montantHt.greaterThan=" + SMALLER_MONTANT_HT, "montantHt.greaterThan=" + DEFAULT_MONTANT_HT);
    }

    @Test
    @Transactional
    void getAllFacturesByTauxTvaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxTva equals to
        defaultFactureFiltering("tauxTva.equals=" + DEFAULT_TAUX_TVA, "tauxTva.equals=" + UPDATED_TAUX_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByTauxTvaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxTva in
        defaultFactureFiltering("tauxTva.in=" + DEFAULT_TAUX_TVA + "," + UPDATED_TAUX_TVA, "tauxTva.in=" + UPDATED_TAUX_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByTauxTvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxTva is not null
        defaultFactureFiltering("tauxTva.specified=true", "tauxTva.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByTauxTvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxTva is greater than or equal to
        defaultFactureFiltering("tauxTva.greaterThanOrEqual=" + DEFAULT_TAUX_TVA, "tauxTva.greaterThanOrEqual=" + (DEFAULT_TAUX_TVA + 1));
    }

    @Test
    @Transactional
    void getAllFacturesByTauxTvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxTva is less than or equal to
        defaultFactureFiltering("tauxTva.lessThanOrEqual=" + DEFAULT_TAUX_TVA, "tauxTva.lessThanOrEqual=" + SMALLER_TAUX_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByTauxTvaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxTva is less than
        defaultFactureFiltering("tauxTva.lessThan=" + (DEFAULT_TAUX_TVA + 1), "tauxTva.lessThan=" + DEFAULT_TAUX_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByTauxTvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxTva is greater than
        defaultFactureFiltering("tauxTva.greaterThan=" + SMALLER_TAUX_TVA, "tauxTva.greaterThan=" + DEFAULT_TAUX_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTvaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTva equals to
        defaultFactureFiltering("montantTva.equals=" + DEFAULT_MONTANT_TVA, "montantTva.equals=" + UPDATED_MONTANT_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTvaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTva in
        defaultFactureFiltering("montantTva.in=" + DEFAULT_MONTANT_TVA + "," + UPDATED_MONTANT_TVA, "montantTva.in=" + UPDATED_MONTANT_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTva is not null
        defaultFactureFiltering("montantTva.specified=true", "montantTva.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTva is greater than or equal to
        defaultFactureFiltering(
            "montantTva.greaterThanOrEqual=" + DEFAULT_MONTANT_TVA,
            "montantTva.greaterThanOrEqual=" + UPDATED_MONTANT_TVA
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTva is less than or equal to
        defaultFactureFiltering("montantTva.lessThanOrEqual=" + DEFAULT_MONTANT_TVA, "montantTva.lessThanOrEqual=" + SMALLER_MONTANT_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTvaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTva is less than
        defaultFactureFiltering("montantTva.lessThan=" + UPDATED_MONTANT_TVA, "montantTva.lessThan=" + DEFAULT_MONTANT_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTva is greater than
        defaultFactureFiltering("montantTva.greaterThan=" + SMALLER_MONTANT_TVA, "montantTva.greaterThan=" + DEFAULT_MONTANT_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTtcIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTtc equals to
        defaultFactureFiltering("montantTtc.equals=" + DEFAULT_MONTANT_TTC, "montantTtc.equals=" + UPDATED_MONTANT_TTC);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTtcIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTtc in
        defaultFactureFiltering("montantTtc.in=" + DEFAULT_MONTANT_TTC + "," + UPDATED_MONTANT_TTC, "montantTtc.in=" + UPDATED_MONTANT_TTC);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTtcIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTtc is not null
        defaultFactureFiltering("montantTtc.specified=true", "montantTtc.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTtcIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTtc is greater than or equal to
        defaultFactureFiltering(
            "montantTtc.greaterThanOrEqual=" + DEFAULT_MONTANT_TTC,
            "montantTtc.greaterThanOrEqual=" + UPDATED_MONTANT_TTC
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTtcIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTtc is less than or equal to
        defaultFactureFiltering("montantTtc.lessThanOrEqual=" + DEFAULT_MONTANT_TTC, "montantTtc.lessThanOrEqual=" + SMALLER_MONTANT_TTC);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTtcIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTtc is less than
        defaultFactureFiltering("montantTtc.lessThan=" + UPDATED_MONTANT_TTC, "montantTtc.lessThan=" + DEFAULT_MONTANT_TTC);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTtcIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where montantTtc is greater than
        defaultFactureFiltering("montantTtc.greaterThan=" + SMALLER_MONTANT_TTC, "montantTtc.greaterThan=" + DEFAULT_MONTANT_TTC);
    }

    @Test
    @Transactional
    void getAllFacturesByDeviseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where devise equals to
        defaultFactureFiltering("devise.equals=" + DEFAULT_DEVISE, "devise.equals=" + UPDATED_DEVISE);
    }

    @Test
    @Transactional
    void getAllFacturesByDeviseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where devise in
        defaultFactureFiltering("devise.in=" + DEFAULT_DEVISE + "," + UPDATED_DEVISE, "devise.in=" + UPDATED_DEVISE);
    }

    @Test
    @Transactional
    void getAllFacturesByDeviseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where devise is not null
        defaultFactureFiltering("devise.specified=true", "devise.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByTauxChangeCfaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxChangeCfa equals to
        defaultFactureFiltering("tauxChangeCfa.equals=" + DEFAULT_TAUX_CHANGE_CFA, "tauxChangeCfa.equals=" + UPDATED_TAUX_CHANGE_CFA);
    }

    @Test
    @Transactional
    void getAllFacturesByTauxChangeCfaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxChangeCfa in
        defaultFactureFiltering(
            "tauxChangeCfa.in=" + DEFAULT_TAUX_CHANGE_CFA + "," + UPDATED_TAUX_CHANGE_CFA,
            "tauxChangeCfa.in=" + UPDATED_TAUX_CHANGE_CFA
        );
    }

    @Test
    @Transactional
    void getAllFacturesByTauxChangeCfaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxChangeCfa is not null
        defaultFactureFiltering("tauxChangeCfa.specified=true", "tauxChangeCfa.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByTauxChangeCfaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxChangeCfa is greater than or equal to
        defaultFactureFiltering(
            "tauxChangeCfa.greaterThanOrEqual=" + DEFAULT_TAUX_CHANGE_CFA,
            "tauxChangeCfa.greaterThanOrEqual=" + UPDATED_TAUX_CHANGE_CFA
        );
    }

    @Test
    @Transactional
    void getAllFacturesByTauxChangeCfaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxChangeCfa is less than or equal to
        defaultFactureFiltering(
            "tauxChangeCfa.lessThanOrEqual=" + DEFAULT_TAUX_CHANGE_CFA,
            "tauxChangeCfa.lessThanOrEqual=" + SMALLER_TAUX_CHANGE_CFA
        );
    }

    @Test
    @Transactional
    void getAllFacturesByTauxChangeCfaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxChangeCfa is less than
        defaultFactureFiltering("tauxChangeCfa.lessThan=" + UPDATED_TAUX_CHANGE_CFA, "tauxChangeCfa.lessThan=" + DEFAULT_TAUX_CHANGE_CFA);
    }

    @Test
    @Transactional
    void getAllFacturesByTauxChangeCfaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where tauxChangeCfa is greater than
        defaultFactureFiltering(
            "tauxChangeCfa.greaterThan=" + SMALLER_TAUX_CHANGE_CFA,
            "tauxChangeCfa.greaterThan=" + DEFAULT_TAUX_CHANGE_CFA
        );
    }

    @Test
    @Transactional
    void getAllFacturesByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where statut equals to
        defaultFactureFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllFacturesByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where statut in
        defaultFactureFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllFacturesByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where statut is not null
        defaultFactureFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where notes equals to
        defaultFactureFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllFacturesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where notes in
        defaultFactureFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllFacturesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where notes is not null
        defaultFactureFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where notes contains
        defaultFactureFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllFacturesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where notes does not contain
        defaultFactureFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllFacturesByCheminPdfIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where cheminPdf equals to
        defaultFactureFiltering("cheminPdf.equals=" + DEFAULT_CHEMIN_PDF, "cheminPdf.equals=" + UPDATED_CHEMIN_PDF);
    }

    @Test
    @Transactional
    void getAllFacturesByCheminPdfIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where cheminPdf in
        defaultFactureFiltering("cheminPdf.in=" + DEFAULT_CHEMIN_PDF + "," + UPDATED_CHEMIN_PDF, "cheminPdf.in=" + UPDATED_CHEMIN_PDF);
    }

    @Test
    @Transactional
    void getAllFacturesByCheminPdfIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where cheminPdf is not null
        defaultFactureFiltering("cheminPdf.specified=true", "cheminPdf.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByCheminPdfContainsSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where cheminPdf contains
        defaultFactureFiltering("cheminPdf.contains=" + DEFAULT_CHEMIN_PDF, "cheminPdf.contains=" + UPDATED_CHEMIN_PDF);
    }

    @Test
    @Transactional
    void getAllFacturesByCheminPdfNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where cheminPdf does not contain
        defaultFactureFiltering("cheminPdf.doesNotContain=" + UPDATED_CHEMIN_PDF, "cheminPdf.doesNotContain=" + DEFAULT_CHEMIN_PDF);
    }

    @Test
    @Transactional
    void getAllFacturesByCreeParLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where creeParLogin equals to
        defaultFactureFiltering("creeParLogin.equals=" + DEFAULT_CREE_PAR_LOGIN, "creeParLogin.equals=" + UPDATED_CREE_PAR_LOGIN);
    }

    @Test
    @Transactional
    void getAllFacturesByCreeParLoginIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where creeParLogin in
        defaultFactureFiltering(
            "creeParLogin.in=" + DEFAULT_CREE_PAR_LOGIN + "," + UPDATED_CREE_PAR_LOGIN,
            "creeParLogin.in=" + UPDATED_CREE_PAR_LOGIN
        );
    }

    @Test
    @Transactional
    void getAllFacturesByCreeParLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where creeParLogin is not null
        defaultFactureFiltering("creeParLogin.specified=true", "creeParLogin.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByCreeParLoginContainsSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where creeParLogin contains
        defaultFactureFiltering("creeParLogin.contains=" + DEFAULT_CREE_PAR_LOGIN, "creeParLogin.contains=" + UPDATED_CREE_PAR_LOGIN);
    }

    @Test
    @Transactional
    void getAllFacturesByCreeParLoginNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList where creeParLogin does not contain
        defaultFactureFiltering(
            "creeParLogin.doesNotContain=" + UPDATED_CREE_PAR_LOGIN,
            "creeParLogin.doesNotContain=" + DEFAULT_CREE_PAR_LOGIN
        );
    }

    @Test
    @Transactional
    void getAllFacturesByNavireIsEqualToSomething() throws Exception {
        Navire navire;
        if (TestUtil.findAll(em, Navire.class).isEmpty()) {
            factureRepository.saveAndFlush(facture);
            navire = NavireResourceIT.createEntity(em);
        } else {
            navire = TestUtil.findAll(em, Navire.class).get(0);
        }
        em.persist(navire);
        em.flush();
        facture.setNavire(navire);
        factureRepository.saveAndFlush(facture);
        Long navireId = navire.getId();
        // Get all the factureList where navire equals to navireId
        defaultFactureShouldBeFound("navireId.equals=" + navireId);

        // Get all the factureList where navire equals to (navireId + 1)
        defaultFactureShouldNotBeFound("navireId.equals=" + (navireId + 1));
    }

    @Test
    @Transactional
    void getAllFacturesByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            factureRepository.saveAndFlush(facture);
            client = ClientResourceIT.createEntity();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        facture.setClient(client);
        factureRepository.saveAndFlush(facture);
        Long clientId = client.getId();
        // Get all the factureList where client equals to clientId
        defaultFactureShouldBeFound("clientId.equals=" + clientId);

        // Get all the factureList where client equals to (clientId + 1)
        defaultFactureShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    private void defaultFactureFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFactureShouldBeFound(shouldBeFound);
        defaultFactureShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFactureShouldBeFound(String filter) throws Exception {
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dateEmission").value(hasItem(DEFAULT_DATE_EMISSION.toString())))
            .andExpect(jsonPath("$.[*].datePaiement").value(hasItem(DEFAULT_DATE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].volumeM3").value(hasItem(DEFAULT_VOLUME_M_3)))
            .andExpect(jsonPath("$.[*].montantBaseHt").value(hasItem(DEFAULT_MONTANT_BASE_HT)))
            .andExpect(jsonPath("$.[*].montantSupplementsHt").value(hasItem(DEFAULT_MONTANT_SUPPLEMENTS_HT)))
            .andExpect(jsonPath("$.[*].montantHt").value(hasItem(DEFAULT_MONTANT_HT)))
            .andExpect(jsonPath("$.[*].tauxTva").value(hasItem(DEFAULT_TAUX_TVA)))
            .andExpect(jsonPath("$.[*].montantTva").value(hasItem(DEFAULT_MONTANT_TVA)))
            .andExpect(jsonPath("$.[*].montantTtc").value(hasItem(DEFAULT_MONTANT_TTC)))
            .andExpect(jsonPath("$.[*].devise").value(hasItem(DEFAULT_DEVISE.toString())))
            .andExpect(jsonPath("$.[*].tauxChangeCfa").value(hasItem(DEFAULT_TAUX_CHANGE_CFA)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].cheminPdf").value(hasItem(DEFAULT_CHEMIN_PDF)))
            .andExpect(jsonPath("$.[*].creeParLogin").value(hasItem(DEFAULT_CREE_PAR_LOGIN)));

        // Check, that the count call also returns 1
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFactureShouldNotBeFound(String filter) throws Exception {
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacture() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facture
        Facture updatedFacture = factureRepository.findById(facture.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFacture are not directly saved in db
        em.detach(updatedFacture);
        updatedFacture
            .numero(UPDATED_NUMERO)
            .dateEmission(UPDATED_DATE_EMISSION)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .volumeM3(UPDATED_VOLUME_M_3)
            .montantBaseHt(UPDATED_MONTANT_BASE_HT)
            .montantSupplementsHt(UPDATED_MONTANT_SUPPLEMENTS_HT)
            .montantHt(UPDATED_MONTANT_HT)
            .tauxTva(UPDATED_TAUX_TVA)
            .montantTva(UPDATED_MONTANT_TVA)
            .montantTtc(UPDATED_MONTANT_TTC)
            .devise(UPDATED_DEVISE)
            .tauxChangeCfa(UPDATED_TAUX_CHANGE_CFA)
            .statut(UPDATED_STATUT)
            .notes(UPDATED_NOTES)
            .cheminPdf(UPDATED_CHEMIN_PDF)
            .creeParLogin(UPDATED_CREE_PAR_LOGIN);
        FactureDTO factureDTO = factureMapper.toDto(updatedFacture);

        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factureDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFactureToMatchAllProperties(updatedFacture);
    }

    @Test
    @Transactional
    void putNonExistingFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(longCount.incrementAndGet());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factureDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(longCount.incrementAndGet());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(longCount.incrementAndGet());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFactureWithPatch() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facture using partial update
        Facture partialUpdatedFacture = new Facture();
        partialUpdatedFacture.setId(facture.getId());

        partialUpdatedFacture
            .numero(UPDATED_NUMERO)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .montantHt(UPDATED_MONTANT_HT)
            .montantTva(UPDATED_MONTANT_TVA)
            .devise(UPDATED_DEVISE)
            .tauxChangeCfa(UPDATED_TAUX_CHANGE_CFA)
            .cheminPdf(UPDATED_CHEMIN_PDF);

        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacture))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactureUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFacture, facture), getPersistedFacture(facture));
    }

    @Test
    @Transactional
    void fullUpdateFactureWithPatch() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facture using partial update
        Facture partialUpdatedFacture = new Facture();
        partialUpdatedFacture.setId(facture.getId());

        partialUpdatedFacture
            .numero(UPDATED_NUMERO)
            .dateEmission(UPDATED_DATE_EMISSION)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .volumeM3(UPDATED_VOLUME_M_3)
            .montantBaseHt(UPDATED_MONTANT_BASE_HT)
            .montantSupplementsHt(UPDATED_MONTANT_SUPPLEMENTS_HT)
            .montantHt(UPDATED_MONTANT_HT)
            .tauxTva(UPDATED_TAUX_TVA)
            .montantTva(UPDATED_MONTANT_TVA)
            .montantTtc(UPDATED_MONTANT_TTC)
            .devise(UPDATED_DEVISE)
            .tauxChangeCfa(UPDATED_TAUX_CHANGE_CFA)
            .statut(UPDATED_STATUT)
            .notes(UPDATED_NOTES)
            .cheminPdf(UPDATED_CHEMIN_PDF)
            .creeParLogin(UPDATED_CREE_PAR_LOGIN);

        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacture))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactureUpdatableFieldsEquals(partialUpdatedFacture, getPersistedFacture(partialUpdatedFacture));
    }

    @Test
    @Transactional
    void patchNonExistingFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(longCount.incrementAndGet());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(longCount.incrementAndGet());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(longCount.incrementAndGet());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacture() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facture
        restFactureMockMvc
            .perform(delete(ENTITY_API_URL_ID, facture.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return factureRepository.count();
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

    protected Facture getPersistedFacture(Facture facture) {
        return factureRepository.findById(facture.getId()).orElseThrow();
    }

    protected void assertPersistedFactureToMatchAllProperties(Facture expectedFacture) {
        assertFactureAllPropertiesEquals(expectedFacture, getPersistedFacture(expectedFacture));
    }

    protected void assertPersistedFactureToMatchUpdatableProperties(Facture expectedFacture) {
        assertFactureAllUpdatablePropertiesEquals(expectedFacture, getPersistedFacture(expectedFacture));
    }
}
