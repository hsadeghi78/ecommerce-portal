package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.ClassType;
import com.hs.ec.portal.domain.criteria.ClassTypeCriteria;
import com.hs.ec.portal.repository.rowmapper.ClassTypeRowMapper;
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
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the ClassType entity.
 */
@SuppressWarnings("unused")
class ClassTypeRepositoryInternalImpl extends SimpleR2dbcRepository<ClassType, Long> implements ClassTypeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ClassTypeRowMapper classtypeMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("class_type", EntityManager.ENTITY_ALIAS);

    public ClassTypeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ClassTypeRowMapper classtypeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ClassType.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.classtypeMapper = classtypeMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<ClassType> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ClassType> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ClassTypeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ClassType.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ClassType> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ClassType> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private ClassType process(Row row, RowMetadata metadata) {
        ClassType entity = classtypeMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ClassType> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<ClassType> findByCriteria(ClassTypeCriteria classTypeCriteria, Pageable page) {
        return createQuery(page, buildConditions(classTypeCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ClassTypeCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ClassTypeCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getTitle() != null) {
                builder.buildFilterConditionForField(criteria.getTitle(), entityTable.column("title"));
            }
            if (criteria.getTypeCode() != null) {
                builder.buildFilterConditionForField(criteria.getTypeCode(), entityTable.column("type_code"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
        }
        return builder.buildConditions();
    }
}
