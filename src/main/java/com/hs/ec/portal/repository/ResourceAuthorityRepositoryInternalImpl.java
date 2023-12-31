package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.ResourceAuthority;
import com.hs.ec.portal.domain.criteria.ResourceAuthorityCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.MyAuthorityRowMapper;
import com.hs.ec.portal.repository.rowmapper.ResourceAuthorityRowMapper;
import com.hs.ec.portal.repository.rowmapper.ResourceRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ResourceAuthority entity.
 */
@SuppressWarnings("unused")
class ResourceAuthorityRepositoryInternalImpl
    extends SimpleR2dbcRepository<ResourceAuthority, Long>
    implements ResourceAuthorityRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ResourceRowMapper resourceMapper;
    private final MyAuthorityRowMapper myauthorityMapper;
    private final ResourceAuthorityRowMapper resourceauthorityMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("resource_authority", EntityManager.ENTITY_ALIAS);
    private static final Table resourceTable = Table.aliased("resource", "e_resource");
    private static final Table myAuthorityTable = Table.aliased("my_authority", "myAuthority");

    public ResourceAuthorityRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ResourceRowMapper resourceMapper,
        MyAuthorityRowMapper myauthorityMapper,
        ResourceAuthorityRowMapper resourceauthorityMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ResourceAuthority.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.resourceMapper = resourceMapper;
        this.myauthorityMapper = myauthorityMapper;
        this.resourceauthorityMapper = resourceauthorityMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<ResourceAuthority> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ResourceAuthority> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ResourceAuthoritySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ResourceSqlHelper.getColumns(resourceTable, "resource"));
        columns.addAll(MyAuthoritySqlHelper.getColumns(myAuthorityTable, "myAuthority"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(resourceTable)
            .on(Column.create("resource_id", entityTable))
            .equals(Column.create("id", resourceTable))
            .leftOuterJoin(myAuthorityTable)
            .on(Column.create("my_authority_id", entityTable))
            .equals(Column.create("id", myAuthorityTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ResourceAuthority.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ResourceAuthority> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ResourceAuthority> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<ResourceAuthority> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<ResourceAuthority> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<ResourceAuthority> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private ResourceAuthority process(Row row, RowMetadata metadata) {
        ResourceAuthority entity = resourceauthorityMapper.apply(row, "e");
        entity.setResource(resourceMapper.apply(row, "resource"));
        entity.setMyAuthority(myauthorityMapper.apply(row, "myAuthority"));
        return entity;
    }

    @Override
    public <S extends ResourceAuthority> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<ResourceAuthority> findByCriteria(ResourceAuthorityCriteria resourceAuthorityCriteria, Pageable page) {
        return createQuery(page, buildConditions(resourceAuthorityCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ResourceAuthorityCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ResourceAuthorityCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getVerb() != null) {
                builder.buildFilterConditionForField(criteria.getVerb(), entityTable.column("verb"));
            }
            if (criteria.getResourceId() != null) {
                builder.buildFilterConditionForField(criteria.getResourceId(), resourceTable.column("id"));
            }
            if (criteria.getMyAuthorityId() != null) {
                builder.buildFilterConditionForField(criteria.getMyAuthorityId(), myAuthorityTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
