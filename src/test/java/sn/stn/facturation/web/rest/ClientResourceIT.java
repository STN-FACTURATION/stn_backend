package sn.stn.facturation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.stn.facturation.domain.ClientAsserts.*;
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
import sn.stn.facturation.domain.Client;
import sn.stn.facturation.repository.ClientRepository;
import sn.stn.facturation.service.dto.ClientDTO;
import sn.stn.facturation.service.mapper.ClientMapper;

/**
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "J!Hl@{2=.oKX{as";
    private static final String UPDATED_EMAIL = "bxN@=.Md";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private Client client;

    private Client insertedClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity() {
        return new Client()
            .numero(DEFAULT_NUMERO)
            .nom(DEFAULT_NOM)
            .adresse(DEFAULT_ADRESSE)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .ville(DEFAULT_VILLE)
            .pays(DEFAULT_PAYS)
            .actif(DEFAULT_ACTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity() {
        return new Client()
            .numero(UPDATED_NUMERO)
            .nom(UPDATED_NOM)
            .adresse(UPDATED_ADRESSE)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        client = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedClient != null) {
            clientRepository.delete(insertedClient);
            insertedClient = null;
        }
    }

    @Test
    @Transactional
    void createClient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);
        var returnedClientDTO = om.readValue(
            restClientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientDTO.class
        );

        // Validate the Client in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClient = clientMapper.toEntity(returnedClientDTO);
        assertClientUpdatableFieldsEquals(returnedClient, getPersistedClient(returnedClient));

        insertedClient = returnedClient;
    }

    @Test
    @Transactional
    void createClientWithExistingId() throws Exception {
        // Create the Client with an existing ID
        client.setId(1L);
        ClientDTO clientDTO = clientMapper.toDto(client);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setNumero(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setNom(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        client.setActif(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClients() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @Test
    @Transactional
    void getClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc
            .perform(get(ENTITY_API_URL_ID, client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getClientsByIdFiltering() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        Long id = client.getId();

        defaultClientFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultClientFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultClientFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClientsByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numero equals to
        defaultClientFiltering("numero.equals=" + DEFAULT_NUMERO, "numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllClientsByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numero in
        defaultClientFiltering("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO, "numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllClientsByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numero is not null
        defaultClientFiltering("numero.specified=true", "numero.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNumeroContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numero contains
        defaultClientFiltering("numero.contains=" + DEFAULT_NUMERO, "numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllClientsByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where numero does not contain
        defaultClientFiltering("numero.doesNotContain=" + UPDATED_NUMERO, "numero.doesNotContain=" + DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void getAllClientsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nom equals to
        defaultClientFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllClientsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nom in
        defaultClientFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllClientsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nom is not null
        defaultClientFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nom contains
        defaultClientFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllClientsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nom does not contain
        defaultClientFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllClientsByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where adresse equals to
        defaultClientFiltering("adresse.equals=" + DEFAULT_ADRESSE, "adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllClientsByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where adresse in
        defaultClientFiltering("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE, "adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllClientsByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where adresse is not null
        defaultClientFiltering("adresse.specified=true", "adresse.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByAdresseContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where adresse contains
        defaultClientFiltering("adresse.contains=" + DEFAULT_ADRESSE, "adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllClientsByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where adresse does not contain
        defaultClientFiltering("adresse.doesNotContain=" + UPDATED_ADRESSE, "adresse.doesNotContain=" + DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    void getAllClientsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email equals to
        defaultClientFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email in
        defaultClientFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email is not null
        defaultClientFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email contains
        defaultClientFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where email does not contain
        defaultClientFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where telephone equals to
        defaultClientFiltering("telephone.equals=" + DEFAULT_TELEPHONE, "telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllClientsByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where telephone in
        defaultClientFiltering("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE, "telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllClientsByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where telephone is not null
        defaultClientFiltering("telephone.specified=true", "telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where telephone contains
        defaultClientFiltering("telephone.contains=" + DEFAULT_TELEPHONE, "telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllClientsByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where telephone does not contain
        defaultClientFiltering("telephone.doesNotContain=" + UPDATED_TELEPHONE, "telephone.doesNotContain=" + DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllClientsByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where ville equals to
        defaultClientFiltering("ville.equals=" + DEFAULT_VILLE, "ville.equals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllClientsByVilleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where ville in
        defaultClientFiltering("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE, "ville.in=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllClientsByVilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where ville is not null
        defaultClientFiltering("ville.specified=true", "ville.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByVilleContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where ville contains
        defaultClientFiltering("ville.contains=" + DEFAULT_VILLE, "ville.contains=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    void getAllClientsByVilleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where ville does not contain
        defaultClientFiltering("ville.doesNotContain=" + UPDATED_VILLE, "ville.doesNotContain=" + DEFAULT_VILLE);
    }

    @Test
    @Transactional
    void getAllClientsByPaysIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where pays equals to
        defaultClientFiltering("pays.equals=" + DEFAULT_PAYS, "pays.equals=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    void getAllClientsByPaysIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where pays in
        defaultClientFiltering("pays.in=" + DEFAULT_PAYS + "," + UPDATED_PAYS, "pays.in=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    void getAllClientsByPaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where pays is not null
        defaultClientFiltering("pays.specified=true", "pays.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByPaysContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where pays contains
        defaultClientFiltering("pays.contains=" + DEFAULT_PAYS, "pays.contains=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    void getAllClientsByPaysNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where pays does not contain
        defaultClientFiltering("pays.doesNotContain=" + UPDATED_PAYS, "pays.doesNotContain=" + DEFAULT_PAYS);
    }

    @Test
    @Transactional
    void getAllClientsByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where actif equals to
        defaultClientFiltering("actif.equals=" + DEFAULT_ACTIF, "actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllClientsByActifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where actif in
        defaultClientFiltering("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF, "actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllClientsByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where actif is not null
        defaultClientFiltering("actif.specified=true", "actif.specified=false");
    }

    private void defaultClientFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClientShouldBeFound(shouldBeFound);
        defaultClientShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientShouldBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));

        // Check, that the count call also returns 1
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientShouldNotBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .numero(UPDATED_NUMERO)
            .nom(UPDATED_NOM)
            .adresse(UPDATED_ADRESSE)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .actif(UPDATED_ACTIF);
        ClientDTO clientDTO = clientMapper.toDto(updatedClient);

        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClientToMatchAllProperties(updatedClient);
    }

    @Test
    @Transactional
    void putNonExistingClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .numero(UPDATED_NUMERO)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .pays(UPDATED_PAYS)
            .actif(UPDATED_ACTIF);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedClient, client), getPersistedClient(client));
    }

    @Test
    @Transactional
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .numero(UPDATED_NUMERO)
            .nom(UPDATED_NOM)
            .adresse(UPDATED_ADRESSE)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .actif(UPDATED_ACTIF);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientUpdatableFieldsEquals(partialUpdatedClient, getPersistedClient(partialUpdatedClient));
    }

    @Test
    @Transactional
    void patchNonExistingClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the client
        restClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, client.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clientRepository.count();
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

    protected Client getPersistedClient(Client client) {
        return clientRepository.findById(client.getId()).orElseThrow();
    }

    protected void assertPersistedClientToMatchAllProperties(Client expectedClient) {
        assertClientAllPropertiesEquals(expectedClient, getPersistedClient(expectedClient));
    }

    protected void assertPersistedClientToMatchUpdatableProperties(Client expectedClient) {
        assertClientAllUpdatablePropertiesEquals(expectedClient, getPersistedClient(expectedClient));
    }
}
