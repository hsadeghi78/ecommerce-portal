package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.UserFavorite;
import com.hs.ec.portal.domain.criteria.UserFavoriteCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.ProductRowMapper;
import com.hs.ec.portal.repository.rowmapper.UserFavoriteRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the UserFavorite entity.
 */
@SuppressWarnings("unused")
class UserFavoriteRepositoryInternalImpl extends SimpleR2dbcRepository<UserFavorite, Long> implements UserFavoriteRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProductRowMapper productMapper;
    private final UserFavoriteRowMapper userfavoriteMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("user_favorite", EntityManager.ENTITY_ALIAS);
    private static final Table productTable = Table.aliased("product", "product");

    public UserFavoriteRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProductRowMapper productMapper,
        UserFavoriteRowMapper userfavoriteMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserFavorite.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.productMapper = productMapper;
        this.userfavoriteMapper = userfavoriteMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<UserFavorite> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserFavorite> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserFavoriteSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProductSqlHelper.getColumns(productTable, "product"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(productTable)
            .on(Column.create("product_id", entityTable))
            .equals(Column.create("id", productTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserFavorite.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserFavorite> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserFavorite> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<UserFavorite> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<UserFavorite> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<UserFavorite> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private UserFavorite process(Row row, RowMetadata metadata) {
        UserFavorite entity = userfavoriteMapper.apply(row, "e");
        entity.setProduct(productMapper.apply(row, "product"));
        return entity;
    }

    @Override
    public <S extends UserFavorite> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<UserFavorite> findByCriteria(UserFavoriteCriteria userFavoriteCriteria, Pageable page) {
        return createQuery(page, buildConditions(userFavoriteCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(UserFavoriteCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(UserFavoriteCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getProductId() != null) {
                builder.buildFilterConditionForField(criteria.getProductId(), productTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
