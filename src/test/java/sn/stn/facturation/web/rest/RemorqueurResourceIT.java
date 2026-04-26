package sn.stn.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.stn.facturation.domain.RemorqueurAsserts.*;
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
import sn.stn.facturation.domain.Remorqueur;
import sn.stn.facturation.repository.RemorqueurRepository;
import sn.stn.facturation.service.dto.RemorqueurDTO;
import sn.stn.facturation.service.mapper.RemorqueurMapper;

/**
 * Integration tests for the {@link RemorqueurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RemorqueurResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_STATUT = "AAAAAAAAAA";
    private static final String UPDATED_STATUT = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/remorqueurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RemorqueurRepository remorqueurRepository;

    @Autowired
    private RemorqueurMapper remorqueurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRemorqueurMockMvc;

    private Remorqueur remorqueur;

    private Remorqueur insertedRemorqueur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Remorqueur createEntity() {
        return new Remorqueur().code(DEFAULT_CODE).nom(DEFAULT_NOM).statut(DEFAULT_STATUT).observation(DEFAULT_OBSERVATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Remorqueur createUpdatedEntity() {
        return new Remorqueur().code(UPDATED_CODE).nom(UPDATED_NOM).statut(UPDATED_STATUT).observation(UPDATED_OBSERVATION);
    }

    @BeforeEach
    void initTest() {
        remorqueur = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRemorqueur != null) {
            remorqueurRepository.delete(insertedRemorqueur);
            insertedRemorqueur = null;
        }
    }

    @Test
    @Transactional
    void createRemorqueur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Remorqueur
        RemorqueurDTO remorqueurDTO = remorqueurMapper.toDto(remorqueur);
        var returnedRemorqueurDTO = om.readValue(
            restRemorqueurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(remorqueurDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RemorqueurDTO.class
        );

        // Validate the Remorqueur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRemorqueur = remorqueurMapper.toEntity(returnedRemorqueurDTO);
        assertRemorqueurUpdatableFieldsEquals(returnedRemorqueur, getPersistedRemorqueur(returnedRemorqueur));

        insertedRemorqueur = returnedRemorqueur;
    }

    @Test
    @Transactional
    void createRemorqueurWithExistingId() throws Exception {
        // Create the Remorqueur with an existing ID
        remorqueur.setId(1L);
        RemorqueurDTO remorqueurDTO = remorqueurMapper.toDto(remorqueur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRemorqueurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(remorqueurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Remorqueur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        remorqueur.setCode(null);

        // Create the Remorqueur, which fails.
        RemorqueurDTO remorqueurDTO = remorqueurMapper.toDto(remorqueur);

        restRemorqueurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(remorqueurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        remorqueur.setNom(null);

        // Create the Remorqueur, which fails.
        RemorqueurDTO remorqueurDTO = remorqueurMapper.toDto(remorqueur);

        restRemorqueurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(remorqueurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRemorqueurs() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList
        restRemorqueurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(remorqueur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }

    @Test
    @Transactional
    void getRemorqueur() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get the remorqueur
        restRemorqueurMockMvc
            .perform(get(ENTITY_API_URL_ID, remorqueur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(remorqueur.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }

    @Test
    @Transactional
    void getRemorqueursByIdFiltering() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        Long id = remorqueur.getId();

        defaultRemorqueurFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRemorqueurFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRemorqueurFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRemorqueursByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where code equals to
        defaultRemorqueurFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRemorqueursByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where code in
        defaultRemorqueurFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRemorqueursByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where code is not null
        defaultRemorqueurFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllRemorqueursByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where code contains
        defaultRemorqueurFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllRemorqueursByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where code does not contain
        defaultRemorqueurFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllRemorqueursByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where nom equals to
        defaultRemorqueurFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllRemorqueursByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where nom in
        defaultRemorqueurFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllRemorqueursByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where nom is not null
        defaultRemorqueurFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllRemorqueursByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where nom contains
        defaultRemorqueurFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllRemorqueursByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where nom does not contain
        defaultRemorqueurFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllRemorqueursByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where statut equals to
        defaultRemorqueurFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllRemorqueursByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where statut in
        defaultRemorqueurFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllRemorqueursByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where statut is not null
        defaultRemorqueurFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllRemorqueursByStatutContainsSomething() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where statut contains
        defaultRemorqueurFiltering("statut.contains=" + DEFAULT_STATUT, "statut.contains=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllRemorqueursByStatutNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where statut does not contain
        defaultRemorqueurFiltering("statut.doesNotContain=" + UPDATED_STATUT, "statut.doesNotContain=" + DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void getAllRemorqueursByObservationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where observation equals to
        defaultRemorqueurFiltering("observation.equals=" + DEFAULT_OBSERVATION, "observation.equals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllRemorqueursByObservationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where observation in
        defaultRemorqueurFiltering(
            "observation.in=" + DEFAULT_OBSERVATION + "," + UPDATED_OBSERVATION,
            "observation.in=" + UPDATED_OBSERVATION
        );
    }

    @Test
    @Transactional
    void getAllRemorqueursByObservationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where observation is not null
        defaultRemorqueurFiltering("observation.specified=true", "observation.specified=false");
    }

    @Test
    @Transactional
    void getAllRemorqueursByObservationContainsSomething() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where observation contains
        defaultRemorqueurFiltering("observation.contains=" + DEFAULT_OBSERVATION, "observation.contains=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllRemorqueursByObservationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        // Get all the remorqueurList where observation does not contain
        defaultRemorqueurFiltering(
            "observation.doesNotContain=" + UPDATED_OBSERVATION,
            "observation.doesNotContain=" + DEFAULT_OBSERVATION
        );
    }

    private void defaultRemorqueurFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRemorqueurShouldBeFound(shouldBeFound);
        defaultRemorqueurShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRemorqueurShouldBeFound(String filter) throws Exception {
        restRemorqueurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(remorqueur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));

        // Check, that the count call also returns 1
        restRemorqueurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRemorqueurShouldNotBeFound(String filter) throws Exception {
        restRemorqueurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRemorqueurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRemorqueur() throws Exception {
        // Get the remorqueur
        restRemorqueurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRemorqueur() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the remorqueur
        Remorqueur updatedRemorqueur = remorqueurRepository.findById(remorqueur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRemorqueur are not directly saved in db
        em.detach(updatedRemorqueur);
        updatedRemorqueur.code(UPDATED_CODE).nom(UPDATED_NOM).statut(UPDATED_STATUT).observation(UPDATED_OBSERVATION);
        RemorqueurDTO remorqueurDTO = remorqueurMapper.toDto(updatedRemorqueur);

        restRemorqueurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, remorqueurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(remorqueurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Remorqueur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRemorqueurToMatchAllProperties(updatedRemorqueur);
    }

    @Test
    @Transactional
    void putNonExistingRemorqueur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        remorqueur.setId(longCount.incrementAndGet());

        // Create the Remorqueur
        RemorqueurDTO remorqueurDTO = remorqueurMapper.toDto(remorqueur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRemorqueurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, remorqueurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(remorqueurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Remorqueur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRemorqueur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        remorqueur.setId(longCount.incrementAndGet());

        // Create the Remorqueur
        RemorqueurDTO remorqueurDTO = remorqueurMapper.toDto(remorqueur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRemorqueurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(remorqueurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Remorqueur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRemorqueur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        remorqueur.setId(longCount.incrementAndGet());

        // Create the Remorqueur
        RemorqueurDTO remorqueurDTO = remorqueurMapper.toDto(remorqueur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRemorqueurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(remorqueurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Remorqueur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRemorqueurWithPatch() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the remorqueur using partial update
        Remorqueur partialUpdatedRemorqueur = new Remorqueur();
        partialUpdatedRemorqueur.setId(remorqueur.getId());

        restRemorqueurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRemorqueur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRemorqueur))
            )
            .andExpect(status().isOk());

        // Validate the Remorqueur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRemorqueurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRemorqueur, remorqueur),
            getPersistedRemorqueur(remorqueur)
        );
    }

    @Test
    @Transactional
    void fullUpdateRemorqueurWithPatch() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the remorqueur using partial update
        Remorqueur partialUpdatedRemorqueur = new Remorqueur();
        partialUpdatedRemorqueur.setId(remorqueur.getId());

        partialUpdatedRemorqueur.code(UPDATED_CODE).nom(UPDATED_NOM).statut(UPDATED_STATUT).observation(UPDATED_OBSERVATION);

        restRemorqueurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRemorqueur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRemorqueur))
            )
            .andExpect(status().isOk());

        // Validate the Remorqueur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRemorqueurUpdatableFieldsEquals(partialUpdatedRemorqueur, getPersistedRemorqueur(partialUpdatedRemorqueur));
    }

    @Test
    @Transactional
    void patchNonExistingRemorqueur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        remorqueur.setId(longCount.incrementAndGet());

        // Create the Remorqueur
        RemorqueurDTO remorqueurDTO = remorqueurMapper.toDto(remorqueur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRemorqueurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, remorqueurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(remorqueurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Remorqueur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRemorqueur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        remorqueur.setId(longCount.incrementAndGet());

        // Create the Remorqueur
        RemorqueurDTO remorqueurDTO = remorqueurMapper.toDto(remorqueur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRemorqueurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(remorqueurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Remorqueur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRemorqueur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        remorqueur.setId(longCount.incrementAndGet());

        // Create the Remorqueur
        RemorqueurDTO remorqueurDTO = remorqueurMapper.toDto(remorqueur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRemorqueurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(remorqueurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Remorqueur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRemorqueur() throws Exception {
        // Initialize the database
        insertedRemorqueur = remorqueurRepository.saveAndFlush(remorqueur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the remorqueur
        restRemorqueurMockMvc
            .perform(delete(ENTITY_API_URL_ID, remorqueur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return remorqueurRepository.count();
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

    protected Remorqueur getPersistedRemorqueur(Remorqueur remorqueur) {
        return remorqueurRepository.findById(remorqueur.getId()).orElseThrow();
    }

    protected void assertPersistedRemorqueurToMatchAllProperties(Remorqueur expectedRemorqueur) {
        assertRemorqueurAllPropertiesEquals(expectedRemorqueur, getPersistedRemorqueur(expectedRemorqueur));
    }

    protected void assertPersistedRemorqueurToMatchUpdatableProperties(Remorqueur expectedRemorqueur) {
        assertRemorqueurAllUpdatablePropertiesEquals(expectedRemorqueur, getPersistedRemorqueur(expectedRemorqueur));
    }
}
