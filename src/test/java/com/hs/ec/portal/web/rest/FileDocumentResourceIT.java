package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.FileDocument;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.FileDocumentRepository;
import com.hs.ec.portal.service.dto.FileDocumentDTO;
import com.hs.ec.portal.service.mapper.FileDocumentMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FileDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FileDocumentResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/file-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileDocumentRepository fileDocumentRepository;

    @Autowired
    private FileDocumentMapper fileDocumentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FileDocument fileDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileDocument createEntity(EntityManager em) {
        FileDocument fileDocument = new FileDocument()
            .fileName(DEFAULT_FILE_NAME)
            .fileContent(DEFAULT_FILE_CONTENT)
            .fileContentContentType(DEFAULT_FILE_CONTENT_CONTENT_TYPE)
            .filePath(DEFAULT_FILE_PATH)
            .description(DEFAULT_DESCRIPTION);
        return fileDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileDocument createUpdatedEntity(EntityManager em) {
        FileDocument fileDocument = new FileDocument()
            .fileName(UPDATED_FILE_NAME)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE)
            .filePath(UPDATED_FILE_PATH)
            .description(UPDATED_DESCRIPTION);
        return fileDocument;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FileDocument.class).block();
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
        fileDocument = createEntity(em);
    }

    @Test
    void createFileDocument() throws Exception {
        int databaseSizeBeforeCreate = fileDocumentRepository.findAll().collectList().block().size();
        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocument);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FileDocument in the database
        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        FileDocument testFileDocument = fileDocumentList.get(fileDocumentList.size() - 1);
        assertThat(testFileDocument.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testFileDocument.getFileContent()).isEqualTo(DEFAULT_FILE_CONTENT);
        assertThat(testFileDocument.getFileContentContentType()).isEqualTo(DEFAULT_FILE_CONTENT_CONTENT_TYPE);
        assertThat(testFileDocument.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testFileDocument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createFileDocumentWithExistingId() throws Exception {
        // Create the FileDocument with an existing ID
        fileDocument.setId(1L);
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocument);

        int databaseSizeBeforeCreate = fileDocumentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FileDocument in the database
        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileDocumentRepository.findAll().collectList().block().size();
        // set the field null
        fileDocument.setFileName(null);

        // Create the FileDocument, which fails.
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileDocumentRepository.findAll().collectList().block().size();
        // set the field null
        fileDocument.setDescription(null);

        // Create the FileDocument, which fails.
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFileDocuments() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList
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
            .value(hasItem(fileDocument.getId().intValue()))
            .jsonPath("$.[*].fileName")
            .value(hasItem(DEFAULT_FILE_NAME))
            .jsonPath("$.[*].fileContentContentType")
            .value(hasItem(DEFAULT_FILE_CONTENT_CONTENT_TYPE))
            .jsonPath("$.[*].fileContent")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT)))
            .jsonPath("$.[*].filePath")
            .value(hasItem(DEFAULT_FILE_PATH))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getFileDocument() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get the fileDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, fileDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(fileDocument.getId().intValue()))
            .jsonPath("$.fileName")
            .value(is(DEFAULT_FILE_NAME))
            .jsonPath("$.fileContentContentType")
            .value(is(DEFAULT_FILE_CONTENT_CONTENT_TYPE))
            .jsonPath("$.fileContent")
            .value(is(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT)))
            .jsonPath("$.filePath")
            .value(is(DEFAULT_FILE_PATH))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getFileDocumentsByIdFiltering() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        Long id = fileDocument.getId();

        defaultFileDocumentShouldBeFound("id.equals=" + id);
        defaultFileDocumentShouldNotBeFound("id.notEquals=" + id);

        defaultFileDocumentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFileDocumentShouldNotBeFound("id.greaterThan=" + id);

        defaultFileDocumentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFileDocumentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllFileDocumentsByFileNameIsEqualToSomething() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where fileName equals to DEFAULT_FILE_NAME
        defaultFileDocumentShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the fileDocumentList where fileName equals to UPDATED_FILE_NAME
        defaultFileDocumentShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    void getAllFileDocumentsByFileNameIsInShouldWork() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultFileDocumentShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the fileDocumentList where fileName equals to UPDATED_FILE_NAME
        defaultFileDocumentShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    void getAllFileDocumentsByFileNameIsNullOrNotNull() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where fileName is not null
        defaultFileDocumentShouldBeFound("fileName.specified=true");

        // Get all the fileDocumentList where fileName is null
        defaultFileDocumentShouldNotBeFound("fileName.specified=false");
    }

    @Test
    void getAllFileDocumentsByFileNameContainsSomething() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where fileName contains DEFAULT_FILE_NAME
        defaultFileDocumentShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the fileDocumentList where fileName contains UPDATED_FILE_NAME
        defaultFileDocumentShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    void getAllFileDocumentsByFileNameNotContainsSomething() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where fileName does not contain DEFAULT_FILE_NAME
        defaultFileDocumentShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the fileDocumentList where fileName does not contain UPDATED_FILE_NAME
        defaultFileDocumentShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    @Test
    void getAllFileDocumentsByFilePathIsEqualToSomething() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where filePath equals to DEFAULT_FILE_PATH
        defaultFileDocumentShouldBeFound("filePath.equals=" + DEFAULT_FILE_PATH);

        // Get all the fileDocumentList where filePath equals to UPDATED_FILE_PATH
        defaultFileDocumentShouldNotBeFound("filePath.equals=" + UPDATED_FILE_PATH);
    }

    @Test
    void getAllFileDocumentsByFilePathIsInShouldWork() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where filePath in DEFAULT_FILE_PATH or UPDATED_FILE_PATH
        defaultFileDocumentShouldBeFound("filePath.in=" + DEFAULT_FILE_PATH + "," + UPDATED_FILE_PATH);

        // Get all the fileDocumentList where filePath equals to UPDATED_FILE_PATH
        defaultFileDocumentShouldNotBeFound("filePath.in=" + UPDATED_FILE_PATH);
    }

    @Test
    void getAllFileDocumentsByFilePathIsNullOrNotNull() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where filePath is not null
        defaultFileDocumentShouldBeFound("filePath.specified=true");

        // Get all the fileDocumentList where filePath is null
        defaultFileDocumentShouldNotBeFound("filePath.specified=false");
    }

    @Test
    void getAllFileDocumentsByFilePathContainsSomething() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where filePath contains DEFAULT_FILE_PATH
        defaultFileDocumentShouldBeFound("filePath.contains=" + DEFAULT_FILE_PATH);

        // Get all the fileDocumentList where filePath contains UPDATED_FILE_PATH
        defaultFileDocumentShouldNotBeFound("filePath.contains=" + UPDATED_FILE_PATH);
    }

    @Test
    void getAllFileDocumentsByFilePathNotContainsSomething() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where filePath does not contain DEFAULT_FILE_PATH
        defaultFileDocumentShouldNotBeFound("filePath.doesNotContain=" + DEFAULT_FILE_PATH);

        // Get all the fileDocumentList where filePath does not contain UPDATED_FILE_PATH
        defaultFileDocumentShouldBeFound("filePath.doesNotContain=" + UPDATED_FILE_PATH);
    }

    @Test
    void getAllFileDocumentsByDescriptionIsEqualToSomething() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where description equals to DEFAULT_DESCRIPTION
        defaultFileDocumentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the fileDocumentList where description equals to UPDATED_DESCRIPTION
        defaultFileDocumentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllFileDocumentsByDescriptionIsInShouldWork() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFileDocumentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the fileDocumentList where description equals to UPDATED_DESCRIPTION
        defaultFileDocumentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllFileDocumentsByDescriptionIsNullOrNotNull() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where description is not null
        defaultFileDocumentShouldBeFound("description.specified=true");

        // Get all the fileDocumentList where description is null
        defaultFileDocumentShouldNotBeFound("description.specified=false");
    }

    @Test
    void getAllFileDocumentsByDescriptionContainsSomething() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where description contains DEFAULT_DESCRIPTION
        defaultFileDocumentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the fileDocumentList where description contains UPDATED_DESCRIPTION
        defaultFileDocumentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    void getAllFileDocumentsByDescriptionNotContainsSomething() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        // Get all the fileDocumentList where description does not contain DEFAULT_DESCRIPTION
        defaultFileDocumentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the fileDocumentList where description does not contain UPDATED_DESCRIPTION
        defaultFileDocumentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFileDocumentShouldBeFound(String filter) {
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
            .value(hasItem(fileDocument.getId().intValue()))
            .jsonPath("$.[*].fileName")
            .value(hasItem(DEFAULT_FILE_NAME))
            .jsonPath("$.[*].fileContentContentType")
            .value(hasItem(DEFAULT_FILE_CONTENT_CONTENT_TYPE))
            .jsonPath("$.[*].fileContent")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT)))
            .jsonPath("$.[*].filePath")
            .value(hasItem(DEFAULT_FILE_PATH))
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
    private void defaultFileDocumentShouldNotBeFound(String filter) {
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
    void getNonExistingFileDocument() {
        // Get the fileDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFileDocument() throws Exception {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().collectList().block().size();

        // Update the fileDocument
        FileDocument updatedFileDocument = fileDocumentRepository.findById(fileDocument.getId()).block();
        updatedFileDocument
            .fileName(UPDATED_FILE_NAME)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE)
            .filePath(UPDATED_FILE_PATH)
            .description(UPDATED_DESCRIPTION);
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(updatedFileDocument);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, fileDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FileDocument in the database
        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
        FileDocument testFileDocument = fileDocumentList.get(fileDocumentList.size() - 1);
        assertThat(testFileDocument.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testFileDocument.getFileContent()).isEqualTo(UPDATED_FILE_CONTENT);
        assertThat(testFileDocument.getFileContentContentType()).isEqualTo(UPDATED_FILE_CONTENT_CONTENT_TYPE);
        assertThat(testFileDocument.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testFileDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingFileDocument() throws Exception {
        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().collectList().block().size();
        fileDocument.setId(longCount.incrementAndGet());

        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, fileDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FileDocument in the database
        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFileDocument() throws Exception {
        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().collectList().block().size();
        fileDocument.setId(longCount.incrementAndGet());

        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FileDocument in the database
        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFileDocument() throws Exception {
        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().collectList().block().size();
        fileDocument.setId(longCount.incrementAndGet());

        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FileDocument in the database
        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFileDocumentWithPatch() throws Exception {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().collectList().block().size();

        // Update the fileDocument using partial update
        FileDocument partialUpdatedFileDocument = new FileDocument();
        partialUpdatedFileDocument.setId(fileDocument.getId());

        partialUpdatedFileDocument.filePath(UPDATED_FILE_PATH).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFileDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFileDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FileDocument in the database
        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
        FileDocument testFileDocument = fileDocumentList.get(fileDocumentList.size() - 1);
        assertThat(testFileDocument.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testFileDocument.getFileContent()).isEqualTo(DEFAULT_FILE_CONTENT);
        assertThat(testFileDocument.getFileContentContentType()).isEqualTo(DEFAULT_FILE_CONTENT_CONTENT_TYPE);
        assertThat(testFileDocument.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testFileDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateFileDocumentWithPatch() throws Exception {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().collectList().block().size();

        // Update the fileDocument using partial update
        FileDocument partialUpdatedFileDocument = new FileDocument();
        partialUpdatedFileDocument.setId(fileDocument.getId());

        partialUpdatedFileDocument
            .fileName(UPDATED_FILE_NAME)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE)
            .filePath(UPDATED_FILE_PATH)
            .description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFileDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFileDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FileDocument in the database
        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
        FileDocument testFileDocument = fileDocumentList.get(fileDocumentList.size() - 1);
        assertThat(testFileDocument.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testFileDocument.getFileContent()).isEqualTo(UPDATED_FILE_CONTENT);
        assertThat(testFileDocument.getFileContentContentType()).isEqualTo(UPDATED_FILE_CONTENT_CONTENT_TYPE);
        assertThat(testFileDocument.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testFileDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingFileDocument() throws Exception {
        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().collectList().block().size();
        fileDocument.setId(longCount.incrementAndGet());

        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, fileDocumentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FileDocument in the database
        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFileDocument() throws Exception {
        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().collectList().block().size();
        fileDocument.setId(longCount.incrementAndGet());

        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FileDocument in the database
        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFileDocument() throws Exception {
        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().collectList().block().size();
        fileDocument.setId(longCount.incrementAndGet());

        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FileDocument in the database
        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFileDocument() {
        // Initialize the database
        fileDocumentRepository.save(fileDocument).block();

        int databaseSizeBeforeDelete = fileDocumentRepository.findAll().collectList().block().size();

        // Delete the fileDocument
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, fileDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FileDocument> fileDocumentList = fileDocumentRepository.findAll().collectList().block();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
