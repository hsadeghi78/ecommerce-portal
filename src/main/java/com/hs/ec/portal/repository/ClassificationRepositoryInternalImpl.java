package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Classification;
import com.hs.ec.portal.domain.criteria.ClassificationCriteria;
import com.hs.ec.portal.repository.rowmapper.ClassTypeRowMapper;
import com.hs.ec.portal.repository.rowmapper.ClassificationRowMapper;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
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
 * Spring Data R2DBC custom repository implementation for the Classification entity.
 */
@SuppressWarnings("unused")
class ClassificationRepositoryInternalImpl extends SimpleR2dbcRepository<Classification, Long> implements ClassificationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ClassTypeRowMapper classtypeMapper;
    private final ClassificationRowMapper classificationMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("classification", EntityManager.ENTITY_ALIAS);
    private static final Table classTypeTable = Table.aliased("class_type", "classType");

    public ClassificationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ClassTypeRowMapper classtypeMapper,
        ClassificationRowMapper classificationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Classification.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.classtypeMapper = classtypeMapper;
        this.classificationMapper = classificationMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Classification> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Classification> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ClassificationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ClassTypeSqlHelper.getColumns(classTypeTable, "classType"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(classTypeTable)
            .on(Column.create("class_type_id", entityTable))
            .equals(Column.create("id", classTypeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Classification.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Classification> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Classification> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Classification> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Classification> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Classification> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Classification process(Row row, RowMetadata metadata) {
        Classification entity = classificationMapper.apply(row, "e");
        entity.setClassType(classtypeMapper.apply(row, "classType"));
        return entity;
    }

    @Override
    public <S extends Classification> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Classification> findByCriteria(ClassificationCriteria classificationCriteria, Pageable page) {
        return createQuery(page, buildConditions(classificationCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ClassificationCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ClassificationCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getTitle() != null) {
                builder.buildFilterConditionForField(criteria.getTitle(), entityTable.column("title"));
            }
            if (criteria.getClassCode() != null) {
                builder.buildFilterConditionForField(criteria.getClassCode(), entityTable.column("class_code"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getLanguageClassId() != null) {
                builder.buildFilterConditionForField(criteria.getLanguageClassId(), entityTable.column("language_class_id"));
            }
            if (criteria.getClassTypeId() != null) {
                builder.buildFilterConditionForField(criteria.getClassTypeId(), classTypeTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
