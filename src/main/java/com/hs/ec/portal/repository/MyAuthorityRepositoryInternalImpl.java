package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.MyAuthority;
import com.hs.ec.portal.domain.criteria.MyAuthorityCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.MyAuthorityRowMapper;
import com.hs.ec.portal.repository.rowmapper.MyAuthorityRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the MyAuthority entity.
 */
@SuppressWarnings("unused")
class MyAuthorityRepositoryInternalImpl extends SimpleR2dbcRepository<MyAuthority, Long> implements MyAuthorityRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final MyAuthorityRowMapper myauthorityMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("my_authority", EntityManager.ENTITY_ALIAS);
    private static final Table parentTable = Table.aliased("my_authority", "parent");

    public MyAuthorityRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        MyAuthorityRowMapper myauthorityMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(MyAuthority.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.myauthorityMapper = myauthorityMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<MyAuthority> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<MyAuthority> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MyAuthoritySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(MyAuthoritySqlHelper.getColumns(parentTable, "parent"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(parentTable)
            .on(Column.create("parent_id", entityTable))
            .equals(Column.create("id", parentTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, MyAuthority.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<MyAuthority> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<MyAuthority> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<MyAuthority> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<MyAuthority> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<MyAuthority> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private MyAuthority process(Row row, RowMetadata metadata) {
        MyAuthority entity = myauthorityMapper.apply(row, "e");
        entity.setParent(myauthorityMapper.apply(row, "parent"));
        return entity;
    }

    @Override
    public <S extends MyAuthority> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<MyAuthority> findByCriteria(MyAuthorityCriteria myAuthorityCriteria, Pageable page) {
        return createQuery(page, buildConditions(myAuthorityCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(MyAuthorityCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(MyAuthorityCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getDisplayName() != null) {
                builder.buildFilterConditionForField(criteria.getDisplayName(), entityTable.column("display_name"));
            }
            if (criteria.getParentId() != null) {
                builder.buildFilterConditionForField(criteria.getParentId(), parentTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
