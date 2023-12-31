package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.FactorItem;
import com.hs.ec.portal.domain.criteria.FactorItemCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.FactorItemRowMapper;
import com.hs.ec.portal.repository.rowmapper.FactorRowMapper;
import com.hs.ec.portal.repository.rowmapper.ProductRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the FactorItem entity.
 */
@SuppressWarnings("unused")
class FactorItemRepositoryInternalImpl extends SimpleR2dbcRepository<FactorItem, Long> implements FactorItemRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FactorRowMapper factorMapper;
    private final ProductRowMapper productMapper;
    private final FactorItemRowMapper factoritemMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("factor_item", EntityManager.ENTITY_ALIAS);
    private static final Table factorTable = Table.aliased("factor", "factor");
    private static final Table productTable = Table.aliased("product", "product");

    public FactorItemRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FactorRowMapper factorMapper,
        ProductRowMapper productMapper,
        FactorItemRowMapper factoritemMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(FactorItem.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.factorMapper = factorMapper;
        this.productMapper = productMapper;
        this.factoritemMapper = factoritemMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<FactorItem> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<FactorItem> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FactorItemSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(FactorSqlHelper.getColumns(factorTable, "factor"));
        columns.addAll(ProductSqlHelper.getColumns(productTable, "product"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(factorTable)
            .on(Column.create("factor_id", entityTable))
            .equals(Column.create("id", factorTable))
            .leftOuterJoin(productTable)
            .on(Column.create("product_id", entityTable))
            .equals(Column.create("id", productTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, FactorItem.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<FactorItem> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<FactorItem> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<FactorItem> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<FactorItem> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<FactorItem> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private FactorItem process(Row row, RowMetadata metadata) {
        FactorItem entity = factoritemMapper.apply(row, "e");
        entity.setFactor(factorMapper.apply(row, "factor"));
        entity.setProduct(productMapper.apply(row, "product"));
        return entity;
    }

    @Override
    public <S extends FactorItem> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<FactorItem> findByCriteria(FactorItemCriteria factorItemCriteria, Pageable page) {
        return createQuery(page, buildConditions(factorItemCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(FactorItemCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(FactorItemCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getRowNum() != null) {
                builder.buildFilterConditionForField(criteria.getRowNum(), entityTable.column("row_num"));
            }
            if (criteria.getTitle() != null) {
                builder.buildFilterConditionForField(criteria.getTitle(), entityTable.column("title"));
            }
            if (criteria.getCount() != null) {
                builder.buildFilterConditionForField(criteria.getCount(), entityTable.column("count"));
            }
            if (criteria.getDiscount() != null) {
                builder.buildFilterConditionForField(criteria.getDiscount(), entityTable.column("discount"));
            }
            if (criteria.getTax() != null) {
                builder.buildFilterConditionForField(criteria.getTax(), entityTable.column("tax"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getFactorId() != null) {
                builder.buildFilterConditionForField(criteria.getFactorId(), factorTable.column("id"));
            }
            if (criteria.getProductId() != null) {
                builder.buildFilterConditionForField(criteria.getProductId(), productTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
