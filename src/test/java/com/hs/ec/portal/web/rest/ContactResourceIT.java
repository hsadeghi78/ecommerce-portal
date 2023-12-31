package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Contact;
import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.repository.ContactRepository;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.PartyRepository;
import com.hs.ec.portal.service.ContactService;
import com.hs.ec.portal.service.dto.ContactDTO;
import com.hs.ec.portal.service.mapper.ContactMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link ContactResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ContactResourceIT {

    private static final String DEFAULT_CONTACT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_VALUE = "BBBBBBBBBB";

    private static final Long DEFAULT_TYPE_CLASS_ID = 1L;
    private static final Long UPDATED_TYPE_CLASS_ID = 2L;
    private static final Long SMALLER_TYPE_CLASS_ID = 1L - 1L;

    private static final String DEFAULT_PREFIX = "AAAAAAA";
    private static final String UPDATED_PREFIX = "BBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contacts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactRepository contactRepository;

    @Mock
    private ContactRepository contactRepositoryMock;

    @Autowired
    private ContactMapper contactMapper;

    @Mock
    private ContactService contactServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Contact contact;

    @Autowired
    private PartyRepository partyRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contact createEntity(EntityManager em) {
        Contact contact = new Contact()
            .contactValue(DEFAULT_CONTACT_VALUE)
            .typeClassId(DEFAULT_TYPE_CLASS_ID)
            .prefix(DEFAULT_PREFIX)
            .description(DEFAULT_DESCRIPTION);
        return contact;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contact createUpdatedEntity(EntityManager em) {
        Contact contact = new Contact()
            .contactValue(UPDATED_CONTACT_VALUE)
            .typeClassId(UPDATED_TYPE_CLASS_ID)
            .prefix(UPDATED_PREFIX)
            .description(UPDATED_DESCRIPTION);
        return contact;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Contact.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        contact = createEntity(em);
    }

    @Test
    void createContact() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().collectList().block().size();
        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate + 1);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getContactValue()).isEqualTo(DEFAULT_CONTACT_VALUE);
        assertThat(testContact.getTypeClassId()).isEqualTo(DEFAULT_TYPE_CLASS_ID);
        assertThat(testContact.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testContact.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createContactWithExistingId() throws Exception {
        // Create the Contact with an existing ID
        contact.setId(1L);
        ContactDTO contactDTO = contactMapper.toDto(contact);

        int databaseSizeBeforeCreate = contactRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkContactValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().collectList().block().size();
        // set the field null
        contact.setContactValue(null);

        // Create the Contact, which fails.
        ContactDTO contactDTO = contactMapper.toDto(contact);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().collectList().block().size();
        // set the field null
        contact.setTypeClassId(null);

        // Create the Contact, which fails.
        ContactDTO contactDTO = contactMapper.toDto(contact);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllContacts() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(contact.getId().intValue()))
            .jsonPath("$.[*].contactValue")
            .value(hasItem(DEFAULT_CONTACT_VALUE))
            .jsonPath("$.[*].typeClassId")
            .value(hasItem(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.[*].prefix")
            .value(hasItem(DEFAULT_PREFIX))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContactsWithEagerRelationshipsIsEnabled() {
        when(contactServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(contactServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContactsWithEagerRelationshipsIsNotEnabled() {
        when(contactServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(contactRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getContact() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get the contact
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, contact.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(contact.getId().intValue()))
            .jsonPath("$.contactValue")
            .value(is(DEFAULT_CONTACT_VALUE))
            .jsonPath("$.typeClassId")
            .value(is(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.prefix")
            .value(is(DEFAULT_PREFIX))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getContactsByIdFiltering() {
        // Initialize the database
        contactRepository.save(contact).block();

        Long id = contact.getId();

        defaultContactShouldBeFound("id.equals=" + id);
        defaultContactShouldNotBeFound("id.notEquals=" + id);

        defaultContactShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContactShouldNotBeFound("id.greaterThan=" + id);

        defaultContactShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContactShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllContactsByContactValueIsEqualToSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where contactValue equals to DEFAULT_CONTACT_VALUE
        defaultContactShouldBeFound("contactValue.equals=" + DEFAULT_CONTACT_VALUE);

        // Get all the contactList where contactValue equals to UPDATED_CONTACT_VALUE
        defaultContactShouldNotBeFound("contactValue.equals=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    void getAllContactsByContactValueIsInShouldWork() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where contactValue in DEFAULT_CONTACT_VALUE or UPDATED_CONTACT_VALUE
        defaultContactShouldBeFound("contactValue.in=" + DEFAULT_CONTACT_VALUE + "," + UPDATED_CONTACT_VALUE);

        // Get all the contactList where contactValue equals to UPDATED_CONTACT_VALUE
        defaultContactShouldNotBeFound("contactValue.in=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    void getAllContactsByContactValueIsNullOrNotNull() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where contactValue is not null
        defaultContactShouldBeFound("contactValue.specified=true");

        // Get all the contactList where contactValue is null
        defaultContactShouldNotBeFound("contactValue.specified=false");
    }

    @Test
    void getAllContactsByContactValueContainsSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where contactValue contains DEFAULT_CONTACT_VALUE
        defaultContactShouldBeFound("contactValue.contains=" + DEFAULT_CONTACT_VALUE);

        // Get all the contactList where contactValue contains UPDATED_CONTACT_VALUE
        defaultContactShouldNotBeFound("contactValue.contains=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    void getAllContactsByContactValueNotContainsSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where contactValue does not contain DEFAULT_CONTACT_VALUE
        defaultContactShouldNotBeFound("contactValue.doesNotContain=" + DEFAULT_CONTACT_VALUE);

        // Get all the contactList where contactValue does not contain UPDATED_CONTACT_VALUE
        defaultContactShouldBeFound("contactValue.doesNotContain=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    void getAllContactsByTypeClassIdIsEqualToSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where typeClassId equals to DEFAULT_TYPE_CLASS_ID
        defaultContactShouldBeFound("typeClassId.equals=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the contactList where typeClassId equals to UPDATED_TYPE_CLASS_ID
        defaultContactShouldNotBeFound("typeClassId.equals=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllContactsByTypeClassIdIsInShouldWork() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where typeClassId in DEFAULT_TYPE_CLASS_ID or UPDATED_TYPE_CLASS_ID
        defaultContactShouldBeFound("typeClassId.in=" + DEFAULT_TYPE_CLASS_ID + "," + UPDATED_TYPE_CLASS_ID);

        // Get all the contactList where typeClassId equals to UPDATED_TYPE_CLASS_ID
        defaultContactShouldNotBeFound("typeClassId.in=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllContactsByTypeClassIdIsNullOrNotNull() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where typeClassId is not null
        defaultContactShouldBeFound("typeClassId.specified=true");

        // Get all the contactList where typeClassId is null
        defaultContactShouldNotBeFound("typeClassId.specified=false");
    }

    @Test
    void getAllContactsByTypeClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where typeClassId is greater than or equal to DEFAULT_TYPE_CLASS_ID
        defaultContactShouldBeFound("typeClassId.greaterThanOrEqual=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the contactList where typeClassId is greater than or equal to UPDATED_TYPE_CLASS_ID
        defaultContactShouldNotBeFound("typeClassId.greaterThanOrEqual=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllContactsByTypeClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where typeClassId is less than or equal to DEFAULT_TYPE_CLASS_ID
        defaultContactShouldBeFound("typeClassId.lessThanOrEqual=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the contactList where typeClassId is less than or equal to SMALLER_TYPE_CLASS_ID
        defaultContactShouldNotBeFound("typeClassId.lessThanOrEqual=" + SMALLER_TYPE_CLASS_ID);
    }

    @Test
    void getAllContactsByTypeClassIdIsLessThanSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where typeClassId is less than DEFAULT_TYPE_CLASS_ID
        defaultContactShouldNotBeFound("typeClassId.lessThan=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the contactList where typeClassId is less than UPDATED_TYPE_CLASS_ID
        defaultContactShouldBeFound("typeClassId.lessThan=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllContactsByTypeClassIdIsGreaterThanSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where typeClassId is greater than DEFAULT_TYPE_CLASS_ID
        defaultContactShouldNotBeFound("typeClassId.greaterThan=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the contactList where typeClassId is greater than SMALLER_TYPE_CLASS_ID
        defaultContactShouldBeFound("typeClassId.greaterThan=" + SMALLER_TYPE_CLASS_ID);
    }

    @Test
    void getAllContactsByPrefixIsEqualToSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where prefix equals to DEFAULT_PREFIX
        defaultContactShouldBeFound("prefix.equals=" + DEFAULT_PREFIX);

        // Get all the contactList where prefix equals to UPDATED_PREFIX
        defaultContactShouldNotBeFound("prefix.equals=" + UPDATED_PREFIX);
    }

    @Test
    void getAllContactsByPrefixIsInShouldWork() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where prefix in DEFAULT_PREFIX or UPDATED_PREFIX
        defaultContactShouldBeFound("prefix.in=" + DEFAULT_PREFIX + "," + UPDATED_PREFIX);

        // Get all the contactList where prefix equals to UPDATED_PREFIX
        defaultContactShouldNotBeFound("prefix.in=" + UPDATED_PREFIX);
    }

    @Test
    void getAllContactsByPrefixIsNullOrNotNull() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where prefix is not null
        defaultContactShouldBeFound("prefix.specified=true");

        // Get all the contactList where prefix is null
        defaultContactShouldNotBeFound("prefix.specified=false");
    }

    @Test
    void getAllContactsByPrefixContainsSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where prefix contains DEFAULT_PREFIX
        defaultContactShouldBeFound("prefix.contains=" + DEFAULT_PREFIX);

        // Get all the contactList where prefix contains UPDATED_PREFIX
        defaultContactShouldNotBeFound("prefix.contains=" + UPDATED_PREFIX);
    }

    @Test
    void getAllContactsByPrefixNotContainsSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where prefix does not contain DEFAULT_PREFIX
        defaultContactShouldNotBeFound("prefix.doesNotContain=" + DEFAULT_PREFIX);

        // Get all the contactList where prefix does not contain UPDATED_PREFIX
        defaultContactShouldBeFound("prefix.doesNotContain=" + UPDATED_PREFIX);
    }

    @Test
    void getAllContactsByDescriptionIsEqualToSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where description equals to DEFAULT_DESCRIPTION
        defaultContactShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the contactList where description equals to UPDATED_DESCRIPTION
        defaultContactShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllContactsByDescriptionIsInShouldWork() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultContactShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the contactList where description equals to UPDATED_DESCRIPTION
        defaultContactShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllContactsByDescriptionIsNullOrNotNull() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where description is not null
        defaultContactShouldBeFound("description.specified=true");

        // Get all the contactList where description is null
        defaultContactShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllContactsByDescriptionContainsSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where description contains DEFAULT_DESCRIPTION
        defaultContactShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the contactList where description contains UPDATED_DESCRIPTION
        defaultContactShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllContactsByDescriptionNotContainsSomething() {
        // Initialize the database
        contactRepository.save(contact).block();

        // Get all the contactList where description does not contain DEFAULT_DESCRIPTION
        defaultContactShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the contactList where description does not contain UPDATED_DESCRIPTION
        defaultContactShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllContactsByPartyIsEqualToSomething() {
        Party party = PartyResourceIT.createEntity(em);
        partyRepository.save(party).block();
        Long partyId = party.getId();
        contact.setPartyId(partyId);
        contactRepository.save(contact).block();
        // Get all the contactList where party equals to partyId
        defaultContactShouldBeFound("partyId.equals=" + partyId);

        // Get all the contactList where party equals to (partyId + 1)
        defaultContactShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactShouldBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(contact.getId().intValue()))
            .jsonPath("$.[*].contactValue")
            .value(hasItem(DEFAULT_CONTACT_VALUE))
            .jsonPath("$.[*].typeClassId")
            .value(hasItem(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.[*].prefix")
            .value(hasItem(DEFAULT_PREFIX))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));

        // Check, that the count call also returns 1
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(1));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactShouldNotBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .isArray()
            .jsonPath("$")
            .isEmpty();

        // Check, that the count call also returns 0
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(0));
    }

    @Test
    void getNonExistingContact() {
        // Get the contact
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingContact() throws Exception {
        // Initialize the database
        contactRepository.save(contact).block();

        int databaseSizeBeforeUpdate = contactRepository.findAll().collectList().block().size();

        // Update the contact
        Contact updatedContact = contactRepository.findById(contact.getId()).block();
        updatedContact
            .contactValue(UPDATED_CONTACT_VALUE)
            .typeClassId(UPDATED_TYPE_CLASS_ID)
            .prefix(UPDATED_PREFIX)
            .description(UPDATED_DESCRIPTION);
        ContactDTO contactDTO = contactMapper.toDto(updatedContact);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, contactDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getContactValue()).isEqualTo(UPDATED_CONTACT_VALUE);
        assertThat(testContact.getTypeClassId()).isEqualTo(UPDATED_TYPE_CLASS_ID);
        assertThat(testContact.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testContact.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().collectList().block().size();
        contact.setId(longCount.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, contactDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().collectList().block().size();
        contact.setId(longCount.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().collectList().block().size();
        contact.setId(longCount.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateContactWithPatch() throws Exception {
        // Initialize the database
        contactRepository.save(contact).block();

        int databaseSizeBeforeUpdate = contactRepository.findAll().collectList().block().size();

        // Update the contact using partial update
        Contact partialUpdatedContact = new Contact();
        partialUpdatedContact.setId(contact.getId());

        partialUpdatedContact.prefix(UPDATED_PREFIX).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedContact.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedContact))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getContactValue()).isEqualTo(DEFAULT_CONTACT_VALUE);
        assertThat(testContact.getTypeClassId()).isEqualTo(DEFAULT_TYPE_CLASS_ID);
        assertThat(testContact.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testContact.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateContactWithPatch() throws Exception {
        // Initialize the database
        contactRepository.save(contact).block();

        int databaseSizeBeforeUpdate = contactRepository.findAll().collectList().block().size();

        // Update the contact using partial update
        Contact partialUpdatedContact = new Contact();
        partialUpdatedContact.setId(contact.getId());

        partialUpdatedContact
            .contactValue(UPDATED_CONTACT_VALUE)
            .typeClassId(UPDATED_TYPE_CLASS_ID)
            .prefix(UPDATED_PREFIX)
            .description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedContact.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedContact))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getContactValue()).isEqualTo(UPDATED_CONTACT_VALUE);
        assertThat(testContact.getTypeClassId()).isEqualTo(UPDATED_TYPE_CLASS_ID);
        assertThat(testContact.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testContact.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().collectList().block().size();
        contact.setId(longCount.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, contactDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().collectList().block().size();
        contact.setId(longCount.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().collectList().block().size();
        contact.setId(longCount.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteContact() {
        // Initialize the database
        contactRepository.save(contact).block();

        int databaseSizeBeforeDelete = contactRepository.findAll().collectList().block().size();

        // Delete the contact
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, contact.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Contact> contactList = contactRepository.findAll().collectList().block();
        assertThat(contactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
