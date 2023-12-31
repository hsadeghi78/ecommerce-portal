package com.hs.ec.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.hs.ec.portal.IntegrationTest;
import com.hs.ec.portal.domain.Factor;
import com.hs.ec.portal.domain.GeoDivision;
import com.hs.ec.portal.domain.Location;
import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.repository.EntityManager;
import com.hs.ec.portal.repository.GeoDivisionRepository;
import com.hs.ec.portal.repository.LocationRepository;
import com.hs.ec.portal.repository.PartyRepository;
import com.hs.ec.portal.service.LocationService;
import com.hs.ec.portal.service.dto.LocationDTO;
import com.hs.ec.portal.service.mapper.LocationMapper;
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
 * Integration tests for the {@link LocationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LocationResourceIT {

    private static final Long DEFAULT_TYPE_CLASS_ID = 1L;
    private static final Long UPDATED_TYPE_CLASS_ID = 2L;
    private static final Long SMALLER_TYPE_CLASS_ID = 1L - 1L;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;
    private static final Double SMALLER_LAT = 1D - 1D;

    private static final Double DEFAULT_LON = 1D;
    private static final Double UPDATED_LON = 2D;
    private static final Double SMALLER_LON = 1D - 1D;

    private static final String DEFAULT_STREET_1 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_1 = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_2 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_2 = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_3 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_3 = "BBBBBBBBBB";

    private static final Integer DEFAULT_BUILDING_NO = 1;
    private static final Integer UPDATED_BUILDING_NO = 2;
    private static final Integer SMALLER_BUILDING_NO = 1 - 1;

    private static final String DEFAULT_BUILDING_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BUILDING_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_FLOOR = 1;
    private static final Integer UPDATED_FLOOR = 2;
    private static final Integer SMALLER_FLOOR = 1 - 1;

    private static final Integer DEFAULT_UNIT = 1;
    private static final Integer UPDATED_UNIT = 2;
    private static final Integer SMALLER_UNIT = 1 - 1;

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER = "AAAAAAAAAA";
    private static final String UPDATED_OTHER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationRepository locationRepository;

    @Mock
    private LocationRepository locationRepositoryMock;

    @Autowired
    private LocationMapper locationMapper;

    @Mock
    private LocationService locationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Location location;

    @Autowired
    private GeoDivisionRepository geoDivisionRepository;

    @Autowired
    private PartyRepository partyRepository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createEntity(EntityManager em) {
        Location location = new Location()
            .typeClassId(DEFAULT_TYPE_CLASS_ID)
            .title(DEFAULT_TITLE)
            .lat(DEFAULT_LAT)
            .lon(DEFAULT_LON)
            .street1(DEFAULT_STREET_1)
            .street2(DEFAULT_STREET_2)
            .street3(DEFAULT_STREET_3)
            .buildingNo(DEFAULT_BUILDING_NO)
            .buildingName(DEFAULT_BUILDING_NAME)
            .floor(DEFAULT_FLOOR)
            .unit(DEFAULT_UNIT)
            .postalCode(DEFAULT_POSTAL_CODE)
            .other(DEFAULT_OTHER);
        // Add required entity
        Factor factor;
        factor = em.insert(FactorResourceIT.createEntity(em)).block();
        location.setFactor(factor);
        // Add required entity
        GeoDivision geoDivision;
        geoDivision = em.insert(GeoDivisionResourceIT.createEntity(em)).block();
        location.setGeoDivision(geoDivision);
        return location;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createUpdatedEntity(EntityManager em) {
        Location location = new Location()
            .typeClassId(UPDATED_TYPE_CLASS_ID)
            .title(UPDATED_TITLE)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .street1(UPDATED_STREET_1)
            .street2(UPDATED_STREET_2)
            .street3(UPDATED_STREET_3)
            .buildingNo(UPDATED_BUILDING_NO)
            .buildingName(UPDATED_BUILDING_NAME)
            .floor(UPDATED_FLOOR)
            .unit(UPDATED_UNIT)
            .postalCode(UPDATED_POSTAL_CODE)
            .other(UPDATED_OTHER);
        // Add required entity
        Factor factor;
        factor = em.insert(FactorResourceIT.createUpdatedEntity(em)).block();
        location.setFactor(factor);
        // Add required entity
        GeoDivision geoDivision;
        geoDivision = em.insert(GeoDivisionResourceIT.createUpdatedEntity(em)).block();
        location.setGeoDivision(geoDivision);
        return location;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Location.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        FactorResourceIT.deleteEntities(em);
        GeoDivisionResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        location = createEntity(em);
    }

    @Test
    void createLocation() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().collectList().block().size();
        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate + 1);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getTypeClassId()).isEqualTo(DEFAULT_TYPE_CLASS_ID);
        assertThat(testLocation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLocation.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testLocation.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testLocation.getStreet1()).isEqualTo(DEFAULT_STREET_1);
        assertThat(testLocation.getStreet2()).isEqualTo(DEFAULT_STREET_2);
        assertThat(testLocation.getStreet3()).isEqualTo(DEFAULT_STREET_3);
        assertThat(testLocation.getBuildingNo()).isEqualTo(DEFAULT_BUILDING_NO);
        assertThat(testLocation.getBuildingName()).isEqualTo(DEFAULT_BUILDING_NAME);
        assertThat(testLocation.getFloor()).isEqualTo(DEFAULT_FLOOR);
        assertThat(testLocation.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testLocation.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testLocation.getOther()).isEqualTo(DEFAULT_OTHER);
    }

    @Test
    void createLocationWithExistingId() throws Exception {
        // Create the Location with an existing ID
        location.setId(1L);
        LocationDTO locationDTO = locationMapper.toDto(location);

        int databaseSizeBeforeCreate = locationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().collectList().block().size();
        // set the field null
        location.setTypeClassId(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().collectList().block().size();
        // set the field null
        location.setTitle(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLatIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().collectList().block().size();
        // set the field null
        location.setLat(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLonIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().collectList().block().size();
        // set the field null
        location.setLon(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkBuildingNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().collectList().block().size();
        // set the field null
        location.setBuildingNo(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().collectList().block().size();
        // set the field null
        location.setUnit(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().collectList().block().size();
        // set the field null
        location.setPostalCode(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllLocations() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList
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
            .value(hasItem(location.getId().intValue()))
            .jsonPath("$.[*].typeClassId")
            .value(hasItem(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].lat")
            .value(hasItem(DEFAULT_LAT.doubleValue()))
            .jsonPath("$.[*].lon")
            .value(hasItem(DEFAULT_LON.doubleValue()))
            .jsonPath("$.[*].street1")
            .value(hasItem(DEFAULT_STREET_1))
            .jsonPath("$.[*].street2")
            .value(hasItem(DEFAULT_STREET_2))
            .jsonPath("$.[*].street3")
            .value(hasItem(DEFAULT_STREET_3))
            .jsonPath("$.[*].buildingNo")
            .value(hasItem(DEFAULT_BUILDING_NO))
            .jsonPath("$.[*].buildingName")
            .value(hasItem(DEFAULT_BUILDING_NAME))
            .jsonPath("$.[*].floor")
            .value(hasItem(DEFAULT_FLOOR))
            .jsonPath("$.[*].unit")
            .value(hasItem(DEFAULT_UNIT))
            .jsonPath("$.[*].postalCode")
            .value(hasItem(DEFAULT_POSTAL_CODE))
            .jsonPath("$.[*].other")
            .value(hasItem(DEFAULT_OTHER));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocationsWithEagerRelationshipsIsEnabled() {
        when(locationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(locationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocationsWithEagerRelationshipsIsNotEnabled() {
        when(locationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(locationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getLocation() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get the location
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, location.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(location.getId().intValue()))
            .jsonPath("$.typeClassId")
            .value(is(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.lat")
            .value(is(DEFAULT_LAT.doubleValue()))
            .jsonPath("$.lon")
            .value(is(DEFAULT_LON.doubleValue()))
            .jsonPath("$.street1")
            .value(is(DEFAULT_STREET_1))
            .jsonPath("$.street2")
            .value(is(DEFAULT_STREET_2))
            .jsonPath("$.street3")
            .value(is(DEFAULT_STREET_3))
            .jsonPath("$.buildingNo")
            .value(is(DEFAULT_BUILDING_NO))
            .jsonPath("$.buildingName")
            .value(is(DEFAULT_BUILDING_NAME))
            .jsonPath("$.floor")
            .value(is(DEFAULT_FLOOR))
            .jsonPath("$.unit")
            .value(is(DEFAULT_UNIT))
            .jsonPath("$.postalCode")
            .value(is(DEFAULT_POSTAL_CODE))
            .jsonPath("$.other")
            .value(is(DEFAULT_OTHER));
    }

    @Test
    void getLocationsByIdFiltering() {
        // Initialize the database
        locationRepository.save(location).block();

        Long id = location.getId();

        defaultLocationShouldBeFound("id.equals=" + id);
        defaultLocationShouldNotBeFound("id.notEquals=" + id);

        defaultLocationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLocationShouldNotBeFound("id.greaterThan=" + id);

        defaultLocationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLocationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllLocationsByTypeClassIdIsEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where typeClassId equals to DEFAULT_TYPE_CLASS_ID
        defaultLocationShouldBeFound("typeClassId.equals=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the locationList where typeClassId equals to UPDATED_TYPE_CLASS_ID
        defaultLocationShouldNotBeFound("typeClassId.equals=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllLocationsByTypeClassIdIsInShouldWork() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where typeClassId in DEFAULT_TYPE_CLASS_ID or UPDATED_TYPE_CLASS_ID
        defaultLocationShouldBeFound("typeClassId.in=" + DEFAULT_TYPE_CLASS_ID + "," + UPDATED_TYPE_CLASS_ID);

        // Get all the locationList where typeClassId equals to UPDATED_TYPE_CLASS_ID
        defaultLocationShouldNotBeFound("typeClassId.in=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllLocationsByTypeClassIdIsNullOrNotNull() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where typeClassId is not null
        defaultLocationShouldBeFound("typeClassId.specified=true");

        // Get all the locationList where typeClassId is null
        defaultLocationShouldNotBeFound("typeClassId.specified=false");
    }

    @Test
    void getAllLocationsByTypeClassIdIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where typeClassId is greater than or equal to DEFAULT_TYPE_CLASS_ID
        defaultLocationShouldBeFound("typeClassId.greaterThanOrEqual=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the locationList where typeClassId is greater than or equal to UPDATED_TYPE_CLASS_ID
        defaultLocationShouldNotBeFound("typeClassId.greaterThanOrEqual=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllLocationsByTypeClassIdIsLessThanOrEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where typeClassId is less than or equal to DEFAULT_TYPE_CLASS_ID
        defaultLocationShouldBeFound("typeClassId.lessThanOrEqual=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the locationList where typeClassId is less than or equal to SMALLER_TYPE_CLASS_ID
        defaultLocationShouldNotBeFound("typeClassId.lessThanOrEqual=" + SMALLER_TYPE_CLASS_ID);
    }

    @Test
    void getAllLocationsByTypeClassIdIsLessThanSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where typeClassId is less than DEFAULT_TYPE_CLASS_ID
        defaultLocationShouldNotBeFound("typeClassId.lessThan=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the locationList where typeClassId is less than UPDATED_TYPE_CLASS_ID
        defaultLocationShouldBeFound("typeClassId.lessThan=" + UPDATED_TYPE_CLASS_ID);
    }

    @Test
    void getAllLocationsByTypeClassIdIsGreaterThanSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where typeClassId is greater than DEFAULT_TYPE_CLASS_ID
        defaultLocationShouldNotBeFound("typeClassId.greaterThan=" + DEFAULT_TYPE_CLASS_ID);

        // Get all the locationList where typeClassId is greater than SMALLER_TYPE_CLASS_ID
        defaultLocationShouldBeFound("typeClassId.greaterThan=" + SMALLER_TYPE_CLASS_ID);
    }

    @Test
    void getAllLocationsByTitleIsEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where title equals to DEFAULT_TITLE
        defaultLocationShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the locationList where title equals to UPDATED_TITLE
        defaultLocationShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    void getAllLocationsByTitleIsInShouldWork() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultLocationShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the locationList where title equals to UPDATED_TITLE
        defaultLocationShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    void getAllLocationsByTitleIsNullOrNotNull() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where title is not null
        defaultLocationShouldBeFound("title.specified=true");

        // Get all the locationList where title is null
        defaultLocationShouldNotBeFound("title.specified=false");
    }

    @Test
    void getAllLocationsByTitleContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where title contains DEFAULT_TITLE
        defaultLocationShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the locationList where title contains UPDATED_TITLE
        defaultLocationShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    void getAllLocationsByTitleNotContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where title does not contain DEFAULT_TITLE
        defaultLocationShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the locationList where title does not contain UPDATED_TITLE
        defaultLocationShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    void getAllLocationsByLatIsEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lat equals to DEFAULT_LAT
        defaultLocationShouldBeFound("lat.equals=" + DEFAULT_LAT);

        // Get all the locationList where lat equals to UPDATED_LAT
        defaultLocationShouldNotBeFound("lat.equals=" + UPDATED_LAT);
    }

    @Test
    void getAllLocationsByLatIsInShouldWork() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lat in DEFAULT_LAT or UPDATED_LAT
        defaultLocationShouldBeFound("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT);

        // Get all the locationList where lat equals to UPDATED_LAT
        defaultLocationShouldNotBeFound("lat.in=" + UPDATED_LAT);
    }

    @Test
    void getAllLocationsByLatIsNullOrNotNull() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lat is not null
        defaultLocationShouldBeFound("lat.specified=true");

        // Get all the locationList where lat is null
        defaultLocationShouldNotBeFound("lat.specified=false");
    }

    @Test
    void getAllLocationsByLatIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lat is greater than or equal to DEFAULT_LAT
        defaultLocationShouldBeFound("lat.greaterThanOrEqual=" + DEFAULT_LAT);

        // Get all the locationList where lat is greater than or equal to UPDATED_LAT
        defaultLocationShouldNotBeFound("lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    void getAllLocationsByLatIsLessThanOrEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lat is less than or equal to DEFAULT_LAT
        defaultLocationShouldBeFound("lat.lessThanOrEqual=" + DEFAULT_LAT);

        // Get all the locationList where lat is less than or equal to SMALLER_LAT
        defaultLocationShouldNotBeFound("lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    void getAllLocationsByLatIsLessThanSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lat is less than DEFAULT_LAT
        defaultLocationShouldNotBeFound("lat.lessThan=" + DEFAULT_LAT);

        // Get all the locationList where lat is less than UPDATED_LAT
        defaultLocationShouldBeFound("lat.lessThan=" + UPDATED_LAT);
    }

    @Test
    void getAllLocationsByLatIsGreaterThanSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lat is greater than DEFAULT_LAT
        defaultLocationShouldNotBeFound("lat.greaterThan=" + DEFAULT_LAT);

        // Get all the locationList where lat is greater than SMALLER_LAT
        defaultLocationShouldBeFound("lat.greaterThan=" + SMALLER_LAT);
    }

    @Test
    void getAllLocationsByLonIsEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lon equals to DEFAULT_LON
        defaultLocationShouldBeFound("lon.equals=" + DEFAULT_LON);

        // Get all the locationList where lon equals to UPDATED_LON
        defaultLocationShouldNotBeFound("lon.equals=" + UPDATED_LON);
    }

    @Test
    void getAllLocationsByLonIsInShouldWork() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lon in DEFAULT_LON or UPDATED_LON
        defaultLocationShouldBeFound("lon.in=" + DEFAULT_LON + "," + UPDATED_LON);

        // Get all the locationList where lon equals to UPDATED_LON
        defaultLocationShouldNotBeFound("lon.in=" + UPDATED_LON);
    }

    @Test
    void getAllLocationsByLonIsNullOrNotNull() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lon is not null
        defaultLocationShouldBeFound("lon.specified=true");

        // Get all the locationList where lon is null
        defaultLocationShouldNotBeFound("lon.specified=false");
    }

    @Test
    void getAllLocationsByLonIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lon is greater than or equal to DEFAULT_LON
        defaultLocationShouldBeFound("lon.greaterThanOrEqual=" + DEFAULT_LON);

        // Get all the locationList where lon is greater than or equal to UPDATED_LON
        defaultLocationShouldNotBeFound("lon.greaterThanOrEqual=" + UPDATED_LON);
    }

    @Test
    void getAllLocationsByLonIsLessThanOrEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lon is less than or equal to DEFAULT_LON
        defaultLocationShouldBeFound("lon.lessThanOrEqual=" + DEFAULT_LON);

        // Get all the locationList where lon is less than or equal to SMALLER_LON
        defaultLocationShouldNotBeFound("lon.lessThanOrEqual=" + SMALLER_LON);
    }

    @Test
    void getAllLocationsByLonIsLessThanSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lon is less than DEFAULT_LON
        defaultLocationShouldNotBeFound("lon.lessThan=" + DEFAULT_LON);

        // Get all the locationList where lon is less than UPDATED_LON
        defaultLocationShouldBeFound("lon.lessThan=" + UPDATED_LON);
    }

    @Test
    void getAllLocationsByLonIsGreaterThanSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where lon is greater than DEFAULT_LON
        defaultLocationShouldNotBeFound("lon.greaterThan=" + DEFAULT_LON);

        // Get all the locationList where lon is greater than SMALLER_LON
        defaultLocationShouldBeFound("lon.greaterThan=" + SMALLER_LON);
    }

    @Test
    void getAllLocationsByStreet1IsEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street1 equals to DEFAULT_STREET_1
        defaultLocationShouldBeFound("street1.equals=" + DEFAULT_STREET_1);

        // Get all the locationList where street1 equals to UPDATED_STREET_1
        defaultLocationShouldNotBeFound("street1.equals=" + UPDATED_STREET_1);
    }

    @Test
    void getAllLocationsByStreet1IsInShouldWork() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street1 in DEFAULT_STREET_1 or UPDATED_STREET_1
        defaultLocationShouldBeFound("street1.in=" + DEFAULT_STREET_1 + "," + UPDATED_STREET_1);

        // Get all the locationList where street1 equals to UPDATED_STREET_1
        defaultLocationShouldNotBeFound("street1.in=" + UPDATED_STREET_1);
    }

    @Test
    void getAllLocationsByStreet1IsNullOrNotNull() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street1 is not null
        defaultLocationShouldBeFound("street1.specified=true");

        // Get all the locationList where street1 is null
        defaultLocationShouldNotBeFound("street1.specified=false");
    }

    @Test
    void getAllLocationsByStreet1ContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street1 contains DEFAULT_STREET_1
        defaultLocationShouldBeFound("street1.contains=" + DEFAULT_STREET_1);

        // Get all the locationList where street1 contains UPDATED_STREET_1
        defaultLocationShouldNotBeFound("street1.contains=" + UPDATED_STREET_1);
    }

    @Test
    void getAllLocationsByStreet1NotContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street1 does not contain DEFAULT_STREET_1
        defaultLocationShouldNotBeFound("street1.doesNotContain=" + DEFAULT_STREET_1);

        // Get all the locationList where street1 does not contain UPDATED_STREET_1
        defaultLocationShouldBeFound("street1.doesNotContain=" + UPDATED_STREET_1);
    }

    @Test
    void getAllLocationsByStreet2IsEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street2 equals to DEFAULT_STREET_2
        defaultLocationShouldBeFound("street2.equals=" + DEFAULT_STREET_2);

        // Get all the locationList where street2 equals to UPDATED_STREET_2
        defaultLocationShouldNotBeFound("street2.equals=" + UPDATED_STREET_2);
    }

    @Test
    void getAllLocationsByStreet2IsInShouldWork() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street2 in DEFAULT_STREET_2 or UPDATED_STREET_2
        defaultLocationShouldBeFound("street2.in=" + DEFAULT_STREET_2 + "," + UPDATED_STREET_2);

        // Get all the locationList where street2 equals to UPDATED_STREET_2
        defaultLocationShouldNotBeFound("street2.in=" + UPDATED_STREET_2);
    }

    @Test
    void getAllLocationsByStreet2IsNullOrNotNull() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street2 is not null
        defaultLocationShouldBeFound("street2.specified=true");

        // Get all the locationList where street2 is null
        defaultLocationShouldNotBeFound("street2.specified=false");
    }

    @Test
    void getAllLocationsByStreet2ContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street2 contains DEFAULT_STREET_2
        defaultLocationShouldBeFound("street2.contains=" + DEFAULT_STREET_2);

        // Get all the locationList where street2 contains UPDATED_STREET_2
        defaultLocationShouldNotBeFound("street2.contains=" + UPDATED_STREET_2);
    }

    @Test
    void getAllLocationsByStreet2NotContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street2 does not contain DEFAULT_STREET_2
        defaultLocationShouldNotBeFound("street2.doesNotContain=" + DEFAULT_STREET_2);

        // Get all the locationList where street2 does not contain UPDATED_STREET_2
        defaultLocationShouldBeFound("street2.doesNotContain=" + UPDATED_STREET_2);
    }

    @Test
    void getAllLocationsByStreet3IsEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street3 equals to DEFAULT_STREET_3
        defaultLocationShouldBeFound("street3.equals=" + DEFAULT_STREET_3);

        // Get all the locationList where street3 equals to UPDATED_STREET_3
        defaultLocationShouldNotBeFound("street3.equals=" + UPDATED_STREET_3);
    }

    @Test
    void getAllLocationsByStreet3IsInShouldWork() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street3 in DEFAULT_STREET_3 or UPDATED_STREET_3
        defaultLocationShouldBeFound("street3.in=" + DEFAULT_STREET_3 + "," + UPDATED_STREET_3);

        // Get all the locationList where street3 equals to UPDATED_STREET_3
        defaultLocationShouldNotBeFound("street3.in=" + UPDATED_STREET_3);
    }

    @Test
    void getAllLocationsByStreet3IsNullOrNotNull() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street3 is not null
        defaultLocationShouldBeFound("street3.specified=true");

        // Get all the locationList where street3 is null
        defaultLocationShouldNotBeFound("street3.specified=false");
    }

    @Test
    void getAllLocationsByStreet3ContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street3 contains DEFAULT_STREET_3
        defaultLocationShouldBeFound("street3.contains=" + DEFAULT_STREET_3);

        // Get all the locationList where street3 contains UPDATED_STREET_3
        defaultLocationShouldNotBeFound("street3.contains=" + UPDATED_STREET_3);
    }

    @Test
    void getAllLocationsByStreet3NotContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where street3 does not contain DEFAULT_STREET_3
        defaultLocationShouldNotBeFound("street3.doesNotContain=" + DEFAULT_STREET_3);

        // Get all the locationList where street3 does not contain UPDATED_STREET_3
        defaultLocationShouldBeFound("street3.doesNotContain=" + UPDATED_STREET_3);
    }

    @Test
    void getAllLocationsByBuildingNoIsEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where buildingNo equals to DEFAULT_BUILDING_NO
        defaultLocationShouldBeFound("buildingNo.equals=" + DEFAULT_BUILDING_NO);

        // Get all the locationList where buildingNo equals to UPDATED_BUILDING_NO
        defaultLocationShouldNotBeFound("buildingNo.equals=" + UPDATED_BUILDING_NO);
    }

    @Test
    void getAllLocationsByBuildingNoIsInShouldWork() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where buildingNo in DEFAULT_BUILDING_NO or UPDATED_BUILDING_NO
        defaultLocationShouldBeFound("buildingNo.in=" + DEFAULT_BUILDING_NO + "," + UPDATED_BUILDING_NO);

        // Get all the locationList where buildingNo equals to UPDATED_BUILDING_NO
        defaultLocationShouldNotBeFound("buildingNo.in=" + UPDATED_BUILDING_NO);
    }

    @Test
    void getAllLocationsByBuildingNoIsNullOrNotNull() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where buildingNo is not null
        defaultLocationShouldBeFound("buildingNo.specified=true");

        // Get all the locationList where buildingNo is null
        defaultLocationShouldNotBeFound("buildingNo.specified=false");
    }

    @Test
    void getAllLocationsByBuildingNoIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where buildingNo is greater than or equal to DEFAULT_BUILDING_NO
        defaultLocationShouldBeFound("buildingNo.greaterThanOrEqual=" + DEFAULT_BUILDING_NO);

        // Get all the locationList where buildingNo is greater than or equal to UPDATED_BUILDING_NO
        defaultLocationShouldNotBeFound("buildingNo.greaterThanOrEqual=" + UPDATED_BUILDING_NO);
    }

    @Test
    void getAllLocationsByBuildingNoIsLessThanOrEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where buildingNo is less than or equal to DEFAULT_BUILDING_NO
        defaultLocationShouldBeFound("buildingNo.lessThanOrEqual=" + DEFAULT_BUILDING_NO);

        // Get all the locationList where buildingNo is less than or equal to SMALLER_BUILDING_NO
        defaultLocationShouldNotBeFound("buildingNo.lessThanOrEqual=" + SMALLER_BUILDING_NO);
    }

    @Test
    void getAllLocationsByBuildingNoIsLessThanSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where buildingNo is less than DEFAULT_BUILDING_NO
        defaultLocationShouldNotBeFound("buildingNo.lessThan=" + DEFAULT_BUILDING_NO);

        // Get all the locationList where buildingNo is less than UPDATED_BUILDING_NO
        defaultLocationShouldBeFound("buildingNo.lessThan=" + UPDATED_BUILDING_NO);
    }

    @Test
    void getAllLocationsByBuildingNoIsGreaterThanSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where buildingNo is greater than DEFAULT_BUILDING_NO
        defaultLocationShouldNotBeFound("buildingNo.greaterThan=" + DEFAULT_BUILDING_NO);

        // Get all the locationList where buildingNo is greater than SMALLER_BUILDING_NO
        defaultLocationShouldBeFound("buildingNo.greaterThan=" + SMALLER_BUILDING_NO);
    }

    @Test
    void getAllLocationsByBuildingNameIsEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where buildingName equals to DEFAULT_BUILDING_NAME
        defaultLocationShouldBeFound("buildingName.equals=" + DEFAULT_BUILDING_NAME);

        // Get all the locationList where buildingName equals to UPDATED_BUILDING_NAME
        defaultLocationShouldNotBeFound("buildingName.equals=" + UPDATED_BUILDING_NAME);
    }

    @Test
    void getAllLocationsByBuildingNameIsInShouldWork() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where buildingName in DEFAULT_BUILDING_NAME or UPDATED_BUILDING_NAME
        defaultLocationShouldBeFound("buildingName.in=" + DEFAULT_BUILDING_NAME + "," + UPDATED_BUILDING_NAME);

        // Get all the locationList where buildingName equals to UPDATED_BUILDING_NAME
        defaultLocationShouldNotBeFound("buildingName.in=" + UPDATED_BUILDING_NAME);
    }

    @Test
    void getAllLocationsByBuildingNameIsNullOrNotNull() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where buildingName is not null
        defaultLocationShouldBeFound("buildingName.specified=true");

        // Get all the locationList where buildingName is null
        defaultLocationShouldNotBeFound("buildingName.specified=false");
    }

    @Test
    void getAllLocationsByBuildingNameContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where buildingName contains DEFAULT_BUILDING_NAME
        defaultLocationShouldBeFound("buildingName.contains=" + DEFAULT_BUILDING_NAME);

        // Get all the locationList where buildingName contains UPDATED_BUILDING_NAME
        defaultLocationShouldNotBeFound("buildingName.contains=" + UPDATED_BUILDING_NAME);
    }

    @Test
    void getAllLocationsByBuildingNameNotContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where buildingName does not contain DEFAULT_BUILDING_NAME
        defaultLocationShouldNotBeFound("buildingName.doesNotContain=" + DEFAULT_BUILDING_NAME);

        // Get all the locationList where buildingName does not contain UPDATED_BUILDING_NAME
        defaultLocationShouldBeFound("buildingName.doesNotContain=" + UPDATED_BUILDING_NAME);
    }

    @Test
    void getAllLocationsByFloorIsEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where floor equals to DEFAULT_FLOOR
        defaultLocationShouldBeFound("floor.equals=" + DEFAULT_FLOOR);

        // Get all the locationList where floor equals to UPDATED_FLOOR
        defaultLocationShouldNotBeFound("floor.equals=" + UPDATED_FLOOR);
    }

    @Test
    void getAllLocationsByFloorIsInShouldWork() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where floor in DEFAULT_FLOOR or UPDATED_FLOOR
        defaultLocationShouldBeFound("floor.in=" + DEFAULT_FLOOR + "," + UPDATED_FLOOR);

        // Get all the locationList where floor equals to UPDATED_FLOOR
        defaultLocationShouldNotBeFound("floor.in=" + UPDATED_FLOOR);
    }

    @Test
    void getAllLocationsByFloorIsNullOrNotNull() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where floor is not null
        defaultLocationShouldBeFound("floor.specified=true");

        // Get all the locationList where floor is null
        defaultLocationShouldNotBeFound("floor.specified=false");
    }

    @Test
    void getAllLocationsByFloorIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where floor is greater than or equal to DEFAULT_FLOOR
        defaultLocationShouldBeFound("floor.greaterThanOrEqual=" + DEFAULT_FLOOR);

        // Get all the locationList where floor is greater than or equal to UPDATED_FLOOR
        defaultLocationShouldNotBeFound("floor.greaterThanOrEqual=" + UPDATED_FLOOR);
    }

    @Test
    void getAllLocationsByFloorIsLessThanOrEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where floor is less than or equal to DEFAULT_FLOOR
        defaultLocationShouldBeFound("floor.lessThanOrEqual=" + DEFAULT_FLOOR);

        // Get all the locationList where floor is less than or equal to SMALLER_FLOOR
        defaultLocationShouldNotBeFound("floor.lessThanOrEqual=" + SMALLER_FLOOR);
    }

    @Test
    void getAllLocationsByFloorIsLessThanSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where floor is less than DEFAULT_FLOOR
        defaultLocationShouldNotBeFound("floor.lessThan=" + DEFAULT_FLOOR);

        // Get all the locationList where floor is less than UPDATED_FLOOR
        defaultLocationShouldBeFound("floor.lessThan=" + UPDATED_FLOOR);
    }

    @Test
    void getAllLocationsByFloorIsGreaterThanSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where floor is greater than DEFAULT_FLOOR
        defaultLocationShouldNotBeFound("floor.greaterThan=" + DEFAULT_FLOOR);

        // Get all the locationList where floor is greater than SMALLER_FLOOR
        defaultLocationShouldBeFound("floor.greaterThan=" + SMALLER_FLOOR);
    }

    @Test
    void getAllLocationsByUnitIsEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where unit equals to DEFAULT_UNIT
        defaultLocationShouldBeFound("unit.equals=" + DEFAULT_UNIT);

        // Get all the locationList where unit equals to UPDATED_UNIT
        defaultLocationShouldNotBeFound("unit.equals=" + UPDATED_UNIT);
    }

    @Test
    void getAllLocationsByUnitIsInShouldWork() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where unit in DEFAULT_UNIT or UPDATED_UNIT
        defaultLocationShouldBeFound("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT);

        // Get all the locationList where unit equals to UPDATED_UNIT
        defaultLocationShouldNotBeFound("unit.in=" + UPDATED_UNIT);
    }

    @Test
    void getAllLocationsByUnitIsNullOrNotNull() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where unit is not null
        defaultLocationShouldBeFound("unit.specified=true");

        // Get all the locationList where unit is null
        defaultLocationShouldNotBeFound("unit.specified=false");
    }

    @Test
    void getAllLocationsByUnitIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where unit is greater than or equal to DEFAULT_UNIT
        defaultLocationShouldBeFound("unit.greaterThanOrEqual=" + DEFAULT_UNIT);

        // Get all the locationList where unit is greater than or equal to UPDATED_UNIT
        defaultLocationShouldNotBeFound("unit.greaterThanOrEqual=" + UPDATED_UNIT);
    }

    @Test
    void getAllLocationsByUnitIsLessThanOrEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where unit is less than or equal to DEFAULT_UNIT
        defaultLocationShouldBeFound("unit.lessThanOrEqual=" + DEFAULT_UNIT);

        // Get all the locationList where unit is less than or equal to SMALLER_UNIT
        defaultLocationShouldNotBeFound("unit.lessThanOrEqual=" + SMALLER_UNIT);
    }

    @Test
    void getAllLocationsByUnitIsLessThanSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where unit is less than DEFAULT_UNIT
        defaultLocationShouldNotBeFound("unit.lessThan=" + DEFAULT_UNIT);

        // Get all the locationList where unit is less than UPDATED_UNIT
        defaultLocationShouldBeFound("unit.lessThan=" + UPDATED_UNIT);
    }

    @Test
    void getAllLocationsByUnitIsGreaterThanSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where unit is greater than DEFAULT_UNIT
        defaultLocationShouldNotBeFound("unit.greaterThan=" + DEFAULT_UNIT);

        // Get all the locationList where unit is greater than SMALLER_UNIT
        defaultLocationShouldBeFound("unit.greaterThan=" + SMALLER_UNIT);
    }

    @Test
    void getAllLocationsByPostalCodeIsEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the locationList where postalCode equals to UPDATED_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    void getAllLocationsByPostalCodeIsInShouldWork() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the locationList where postalCode equals to UPDATED_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    void getAllLocationsByPostalCodeIsNullOrNotNull() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where postalCode is not null
        defaultLocationShouldBeFound("postalCode.specified=true");

        // Get all the locationList where postalCode is null
        defaultLocationShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    void getAllLocationsByPostalCodeContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where postalCode contains DEFAULT_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the locationList where postalCode contains UPDATED_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    void getAllLocationsByPostalCodeNotContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultLocationShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the locationList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultLocationShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    void getAllLocationsByOtherIsEqualToSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where other equals to DEFAULT_OTHER
        defaultLocationShouldBeFound("other.equals=" + DEFAULT_OTHER);

        // Get all the locationList where other equals to UPDATED_OTHER
        defaultLocationShouldNotBeFound("other.equals=" + UPDATED_OTHER);
    }

    @Test
    void getAllLocationsByOtherIsInShouldWork() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where other in DEFAULT_OTHER or UPDATED_OTHER
        defaultLocationShouldBeFound("other.in=" + DEFAULT_OTHER + "," + UPDATED_OTHER);

        // Get all the locationList where other equals to UPDATED_OTHER
        defaultLocationShouldNotBeFound("other.in=" + UPDATED_OTHER);
    }

    @Test
    void getAllLocationsByOtherIsNullOrNotNull() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where other is not null
        defaultLocationShouldBeFound("other.specified=true");

        // Get all the locationList where other is null
        defaultLocationShouldNotBeFound("other.specified=false");
    }

    @Test
    void getAllLocationsByOtherContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where other contains DEFAULT_OTHER
        defaultLocationShouldBeFound("other.contains=" + DEFAULT_OTHER);

        // Get all the locationList where other contains UPDATED_OTHER
        defaultLocationShouldNotBeFound("other.contains=" + UPDATED_OTHER);
    }

    @Test
    void getAllLocationsByOtherNotContainsSomething() {
        // Initialize the database
        locationRepository.save(location).block();

        // Get all the locationList where other does not contain DEFAULT_OTHER
        defaultLocationShouldNotBeFound("other.doesNotContain=" + DEFAULT_OTHER);

        // Get all the locationList where other does not contain UPDATED_OTHER
        defaultLocationShouldBeFound("other.doesNotContain=" + UPDATED_OTHER);
    }

    @Test
    void getAllLocationsByGeoDivisionIsEqualToSomething() {
        GeoDivision geoDivision = GeoDivisionResourceIT.createEntity(em);
        geoDivisionRepository.save(geoDivision).block();
        Long geoDivisionId = geoDivision.getId();
        location.setGeoDivisionId(geoDivisionId);
        locationRepository.save(location).block();
        // Get all the locationList where geoDivision equals to geoDivisionId
        defaultLocationShouldBeFound("geoDivisionId.equals=" + geoDivisionId);

        // Get all the locationList where geoDivision equals to (geoDivisionId + 1)
        defaultLocationShouldNotBeFound("geoDivisionId.equals=" + (geoDivisionId + 1));
    }

    @Test
    void getAllLocationsByPartyIsEqualToSomething() {
        Party party = PartyResourceIT.createEntity(em);
        partyRepository.save(party).block();
        Long partyId = party.getId();
        location.setPartyId(partyId);
        locationRepository.save(location).block();
        // Get all the locationList where party equals to partyId
        defaultLocationShouldBeFound("partyId.equals=" + partyId);

        // Get all the locationList where party equals to (partyId + 1)
        defaultLocationShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocationShouldBeFound(String filter) {
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
            .value(hasItem(location.getId().intValue()))
            .jsonPath("$.[*].typeClassId")
            .value(hasItem(DEFAULT_TYPE_CLASS_ID.intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].lat")
            .value(hasItem(DEFAULT_LAT.doubleValue()))
            .jsonPath("$.[*].lon")
            .value(hasItem(DEFAULT_LON.doubleValue()))
            .jsonPath("$.[*].street1")
            .value(hasItem(DEFAULT_STREET_1))
            .jsonPath("$.[*].street2")
            .value(hasItem(DEFAULT_STREET_2))
            .jsonPath("$.[*].street3")
            .value(hasItem(DEFAULT_STREET_3))
            .jsonPath("$.[*].buildingNo")
            .value(hasItem(DEFAULT_BUILDING_NO))
            .jsonPath("$.[*].buildingName")
            .value(hasItem(DEFAULT_BUILDING_NAME))
            .jsonPath("$.[*].floor")
            .value(hasItem(DEFAULT_FLOOR))
            .jsonPath("$.[*].unit")
            .value(hasItem(DEFAULT_UNIT))
            .jsonPath("$.[*].postalCode")
            .value(hasItem(DEFAULT_POSTAL_CODE))
            .jsonPath("$.[*].other")
            .value(hasItem(DEFAULT_OTHER));

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
    private void defaultLocationShouldNotBeFound(String filter) {
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
    void getNonExistingLocation() {
        // Get the location
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLocation() throws Exception {
        // Initialize the database
        locationRepository.save(location).block();

        int databaseSizeBeforeUpdate = locationRepository.findAll().collectList().block().size();

        // Update the location
        Location updatedLocation = locationRepository.findById(location.getId()).block();
        updatedLocation
            .typeClassId(UPDATED_TYPE_CLASS_ID)
            .title(UPDATED_TITLE)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .street1(UPDATED_STREET_1)
            .street2(UPDATED_STREET_2)
            .street3(UPDATED_STREET_3)
            .buildingNo(UPDATED_BUILDING_NO)
            .buildingName(UPDATED_BUILDING_NAME)
            .floor(UPDATED_FLOOR)
            .unit(UPDATED_UNIT)
            .postalCode(UPDATED_POSTAL_CODE)
            .other(UPDATED_OTHER);
        LocationDTO locationDTO = locationMapper.toDto(updatedLocation);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, locationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getTypeClassId()).isEqualTo(UPDATED_TYPE_CLASS_ID);
        assertThat(testLocation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLocation.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testLocation.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testLocation.getStreet1()).isEqualTo(UPDATED_STREET_1);
        assertThat(testLocation.getStreet2()).isEqualTo(UPDATED_STREET_2);
        assertThat(testLocation.getStreet3()).isEqualTo(UPDATED_STREET_3);
        assertThat(testLocation.getBuildingNo()).isEqualTo(UPDATED_BUILDING_NO);
        assertThat(testLocation.getBuildingName()).isEqualTo(UPDATED_BUILDING_NAME);
        assertThat(testLocation.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testLocation.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testLocation.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testLocation.getOther()).isEqualTo(UPDATED_OTHER);
    }

    @Test
    void putNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().collectList().block().size();
        location.setId(longCount.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, locationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().collectList().block().size();
        location.setId(longCount.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().collectList().block().size();
        location.setId(longCount.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        locationRepository.save(location).block();

        int databaseSizeBeforeUpdate = locationRepository.findAll().collectList().block().size();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation.buildingName(UPDATED_BUILDING_NAME).floor(UPDATED_FLOOR).unit(UPDATED_UNIT).postalCode(UPDATED_POSTAL_CODE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLocation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getTypeClassId()).isEqualTo(DEFAULT_TYPE_CLASS_ID);
        assertThat(testLocation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLocation.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testLocation.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testLocation.getStreet1()).isEqualTo(DEFAULT_STREET_1);
        assertThat(testLocation.getStreet2()).isEqualTo(DEFAULT_STREET_2);
        assertThat(testLocation.getStreet3()).isEqualTo(DEFAULT_STREET_3);
        assertThat(testLocation.getBuildingNo()).isEqualTo(DEFAULT_BUILDING_NO);
        assertThat(testLocation.getBuildingName()).isEqualTo(UPDATED_BUILDING_NAME);
        assertThat(testLocation.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testLocation.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testLocation.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testLocation.getOther()).isEqualTo(DEFAULT_OTHER);
    }

    @Test
    void fullUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        locationRepository.save(location).block();

        int databaseSizeBeforeUpdate = locationRepository.findAll().collectList().block().size();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation
            .typeClassId(UPDATED_TYPE_CLASS_ID)
            .title(UPDATED_TITLE)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .street1(UPDATED_STREET_1)
            .street2(UPDATED_STREET_2)
            .street3(UPDATED_STREET_3)
            .buildingNo(UPDATED_BUILDING_NO)
            .buildingName(UPDATED_BUILDING_NAME)
            .floor(UPDATED_FLOOR)
            .unit(UPDATED_UNIT)
            .postalCode(UPDATED_POSTAL_CODE)
            .other(UPDATED_OTHER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLocation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getTypeClassId()).isEqualTo(UPDATED_TYPE_CLASS_ID);
        assertThat(testLocation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLocation.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testLocation.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testLocation.getStreet1()).isEqualTo(UPDATED_STREET_1);
        assertThat(testLocation.getStreet2()).isEqualTo(UPDATED_STREET_2);
        assertThat(testLocation.getStreet3()).isEqualTo(UPDATED_STREET_3);
        assertThat(testLocation.getBuildingNo()).isEqualTo(UPDATED_BUILDING_NO);
        assertThat(testLocation.getBuildingName()).isEqualTo(UPDATED_BUILDING_NAME);
        assertThat(testLocation.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testLocation.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testLocation.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testLocation.getOther()).isEqualTo(UPDATED_OTHER);
    }

    @Test
    void patchNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().collectList().block().size();
        location.setId(longCount.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, locationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().collectList().block().size();
        location.setId(longCount.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().collectList().block().size();
        location.setId(longCount.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(locationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLocation() {
        // Initialize the database
        locationRepository.save(location).block();

        int databaseSizeBeforeDelete = locationRepository.findAll().collectList().block().size();

        // Delete the location
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, location.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Location> locationList = locationRepository.findAll().collectList().block();
        assertThat(locationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
