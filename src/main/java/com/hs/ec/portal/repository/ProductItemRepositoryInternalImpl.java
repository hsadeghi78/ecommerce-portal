package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.ProductItem;
import com.hs.ec.portal.domain.criteria.ProductItemCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.ProductItemRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ProductItem entity.
 */
@SuppressWarnings("unused")
class ProductItemRepositoryInternalImpl extends SimpleR2dbcRepository<ProductItem, Long> implements ProductItemRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProductRowMapper productMapper;
    private final ProductItemRowMapper productitemMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("product_item", EntityManager.ENTITY_ALIAS);
    private static final Table productTable = Table.aliased("product", "product");

    public ProductItemRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProductRowMapper productMapper,
        ProductItemRowMapper productitemMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ProductItem.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.productMapper = productMapper;
        this.productitemMapper = productitemMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<ProductItem> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ProductItem> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProductItemSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProductSqlHelper.getColumns(productTable, "product"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(productTable)
            .on(Column.create("product_id", entityTable))
            .equals(Column.create("id", productTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ProductItem.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ProductItem> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ProductItem> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<ProductItem> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<ProductItem> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<ProductItem> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private ProductItem process(Row row, RowMetadata metadata) {
        ProductItem entity = productitemMapper.apply(row, "e");
        entity.setProduct(productMapper.apply(row, "product"));
        return entity;
    }

    @Override
    public <S extends ProductItem> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<ProductItem> findByCriteria(ProductItemCriteria productItemCriteria, Pageable page) {
        return createQuery(page, buildConditions(productItemCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ProductItemCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ProductItemCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getTypeClassId() != null) {
                builder.buildFilterConditionForField(criteria.getTypeClassId(), entityTable.column("type_class_id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getValue() != null) {
                builder.buildFilterConditionForField(criteria.getValue(), entityTable.column("value"));
            }
            if (criteria.getProductId() != null) {
                builder.buildFilterConditionForField(criteria.getProductId(), productTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
