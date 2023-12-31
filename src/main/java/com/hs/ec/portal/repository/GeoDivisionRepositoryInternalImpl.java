package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.GeoDivision;
import com.hs.ec.portal.domain.criteria.GeoDivisionCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.GeoDivisionRowMapper;
import com.hs.ec.portal.repository.rowmapper.GeoDivisionRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the GeoDivision entity.
 */
@SuppressWarnings("unused")
class GeoDivisionRepositoryInternalImpl extends SimpleR2dbcRepository<GeoDivision, Long> implements GeoDivisionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final GeoDivisionRowMapper geodivisionMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("geo_division", EntityManager.ENTITY_ALIAS);
    private static final Table parentTable = Table.aliased("geo_division", "parent");

    public GeoDivisionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        GeoDivisionRowMapper geodivisionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(GeoDivision.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.geodivisionMapper = geodivisionMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<GeoDivision> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<GeoDivision> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = GeoDivisionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(GeoDivisionSqlHelper.getColumns(parentTable, "parent"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(parentTable)
            .on(Column.create("parent_id", entityTable))
            .equals(Column.create("id", parentTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, GeoDivision.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<GeoDivision> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<GeoDivision> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<GeoDivision> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<GeoDivision> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<GeoDivision> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private GeoDivision process(Row row, RowMetadata metadata) {
        GeoDivision entity = geodivisionMapper.apply(row, "e");
        entity.setParent(geodivisionMapper.apply(row, "parent"));
        return entity;
    }

    @Override
    public <S extends GeoDivision> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<GeoDivision> findByCriteria(GeoDivisionCriteria geoDivisionCriteria, Pageable page) {
        return createQuery(page, buildConditions(geoDivisionCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(GeoDivisionCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(GeoDivisionCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
            if (criteria.getLevel() != null) {
                builder.buildFilterConditionForField(criteria.getLevel(), entityTable.column("level"));
            }
            if (criteria.getParentId() != null) {
                builder.buildFilterConditionForField(criteria.getParentId(), parentTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
