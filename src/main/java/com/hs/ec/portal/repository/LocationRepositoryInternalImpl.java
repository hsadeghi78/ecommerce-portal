package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Location;
import com.hs.ec.portal.domain.criteria.LocationCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.GeoDivisionRowMapper;
import com.hs.ec.portal.repository.rowmapper.LocationRowMapper;
import com.hs.ec.portal.repository.rowmapper.PartyRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the Location entity.
 */
@SuppressWarnings("unused")
class LocationRepositoryInternalImpl extends SimpleR2dbcRepository<Location, Long> implements LocationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final GeoDivisionRowMapper geodivisionMapper;
    private final PartyRowMapper partyMapper;
    private final LocationRowMapper locationMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("location", EntityManager.ENTITY_ALIAS);
    private static final Table geoDivisionTable = Table.aliased("geo_division", "geoDivision");
    private static final Table partyTable = Table.aliased("party", "party");

    public LocationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        GeoDivisionRowMapper geodivisionMapper,
        PartyRowMapper partyMapper,
        LocationRowMapper locationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Location.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.geodivisionMapper = geodivisionMapper;
        this.partyMapper = partyMapper;
        this.locationMapper = locationMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Location> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Location> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = LocationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(GeoDivisionSqlHelper.getColumns(geoDivisionTable, "geoDivision"));
        columns.addAll(PartySqlHelper.getColumns(partyTable, "party"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(geoDivisionTable)
            .on(Column.create("geo_division_id", entityTable))
            .equals(Column.create("id", geoDivisionTable))
            .leftOuterJoin(partyTable)
            .on(Column.create("party_id", entityTable))
            .equals(Column.create("id", partyTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Location.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Location> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Location> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Location> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Location> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Location> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Location process(Row row, RowMetadata metadata) {
        Location entity = locationMapper.apply(row, "e");
        entity.setGeoDivision(geodivisionMapper.apply(row, "geoDivision"));
        entity.setParty(partyMapper.apply(row, "party"));
        return entity;
    }

    @Override
    public <S extends Location> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Location> findByCriteria(LocationCriteria locationCriteria, Pageable page) {
        return createQuery(page, buildConditions(locationCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(LocationCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(LocationCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getTypeClassId() != null) {
                builder.buildFilterConditionForField(criteria.getTypeClassId(), entityTable.column("type_class_id"));
            }
            if (criteria.getTitle() != null) {
                builder.buildFilterConditionForField(criteria.getTitle(), entityTable.column("title"));
            }
            if (criteria.getLat() != null) {
                builder.buildFilterConditionForField(criteria.getLat(), entityTable.column("lat"));
            }
            if (criteria.getLon() != null) {
                builder.buildFilterConditionForField(criteria.getLon(), entityTable.column("lon"));
            }
            if (criteria.getStreet1() != null) {
                builder.buildFilterConditionForField(criteria.getStreet1(), entityTable.column("street_1"));
            }
            if (criteria.getStreet2() != null) {
                builder.buildFilterConditionForField(criteria.getStreet2(), entityTable.column("street_2"));
            }
            if (criteria.getStreet3() != null) {
                builder.buildFilterConditionForField(criteria.getStreet3(), entityTable.column("street_3"));
            }
            if (criteria.getBuildingNo() != null) {
                builder.buildFilterConditionForField(criteria.getBuildingNo(), entityTable.column("building_no"));
            }
            if (criteria.getBuildingName() != null) {
                builder.buildFilterConditionForField(criteria.getBuildingName(), entityTable.column("building_name"));
            }
            if (criteria.getFloor() != null) {
                builder.buildFilterConditionForField(criteria.getFloor(), entityTable.column("floor"));
            }
            if (criteria.getUnit() != null) {
                builder.buildFilterConditionForField(criteria.getUnit(), entityTable.column("unit"));
            }
            if (criteria.getPostalCode() != null) {
                builder.buildFilterConditionForField(criteria.getPostalCode(), entityTable.column("postal_code"));
            }
            if (criteria.getOther() != null) {
                builder.buildFilterConditionForField(criteria.getOther(), entityTable.column("other"));
            }
            if (criteria.getGeoDivisionId() != null) {
                builder.buildFilterConditionForField(criteria.getGeoDivisionId(), geoDivisionTable.column("id"));
            }
            if (criteria.getPartyId() != null) {
                builder.buildFilterConditionForField(criteria.getPartyId(), partyTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
