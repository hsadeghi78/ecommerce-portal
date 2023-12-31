package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.UserComment;
import com.hs.ec.portal.domain.criteria.UserCommentCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.FactorRowMapper;
import com.hs.ec.portal.repository.rowmapper.PartyRowMapper;
import com.hs.ec.portal.repository.rowmapper.ProductRowMapper;
import com.hs.ec.portal.repository.rowmapper.UserCommentRowMapper;
import com.hs.ec.portal.repository.rowmapper.UserCommentRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the UserComment entity.
 */
@SuppressWarnings("unused")
class UserCommentRepositoryInternalImpl extends SimpleR2dbcRepository<UserComment, Long> implements UserCommentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PartyRowMapper partyMapper;
    private final ProductRowMapper productMapper;
    private final FactorRowMapper factorMapper;
    private final UserCommentRowMapper usercommentMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("user_comment", EntityManager.ENTITY_ALIAS);
    private static final Table partyTable = Table.aliased("party", "party");
    private static final Table productTable = Table.aliased("product", "product");
    private static final Table factorTable = Table.aliased("factor", "factor");
    private static final Table parentTable = Table.aliased("user_comment", "parent");

    public UserCommentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PartyRowMapper partyMapper,
        ProductRowMapper productMapper,
        FactorRowMapper factorMapper,
        UserCommentRowMapper usercommentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserComment.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.partyMapper = partyMapper;
        this.productMapper = productMapper;
        this.factorMapper = factorMapper;
        this.usercommentMapper = usercommentMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<UserComment> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserComment> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserCommentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PartySqlHelper.getColumns(partyTable, "party"));
        columns.addAll(ProductSqlHelper.getColumns(productTable, "product"));
        columns.addAll(FactorSqlHelper.getColumns(factorTable, "factor"));
        columns.addAll(UserCommentSqlHelper.getColumns(parentTable, "parent"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(partyTable)
            .on(Column.create("party_id", entityTable))
            .equals(Column.create("id", partyTable))
            .leftOuterJoin(productTable)
            .on(Column.create("product_id", entityTable))
            .equals(Column.create("id", productTable))
            .leftOuterJoin(factorTable)
            .on(Column.create("factor_id", entityTable))
            .equals(Column.create("id", factorTable))
            .leftOuterJoin(parentTable)
            .on(Column.create("parent_id", entityTable))
            .equals(Column.create("id", parentTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserComment.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserComment> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserComment> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<UserComment> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<UserComment> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<UserComment> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private UserComment process(Row row, RowMetadata metadata) {
        UserComment entity = usercommentMapper.apply(row, "e");
        entity.setParty(partyMapper.apply(row, "party"));
        entity.setProduct(productMapper.apply(row, "product"));
        entity.setFactor(factorMapper.apply(row, "factor"));
        entity.setParent(usercommentMapper.apply(row, "parent"));
        return entity;
    }

    @Override
    public <S extends UserComment> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<UserComment> findByCriteria(UserCommentCriteria userCommentCriteria, Pageable page) {
        return createQuery(page, buildConditions(userCommentCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(UserCommentCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(UserCommentCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getRating() != null) {
                builder.buildFilterConditionForField(criteria.getRating(), entityTable.column("rating"));
            }
            if (criteria.getVisible() != null) {
                builder.buildFilterConditionForField(criteria.getVisible(), entityTable.column("visible"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getPartyId() != null) {
                builder.buildFilterConditionForField(criteria.getPartyId(), partyTable.column("id"));
            }
            if (criteria.getProductId() != null) {
                builder.buildFilterConditionForField(criteria.getProductId(), productTable.column("id"));
            }
            if (criteria.getFactorId() != null) {
                builder.buildFilterConditionForField(criteria.getFactorId(), factorTable.column("id"));
            }
            if (criteria.getParentId() != null) {
                builder.buildFilterConditionForField(criteria.getParentId(), parentTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
