package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.FileDocument;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.domain.criteria.ProductCriteria;
import com.hs.ec.portal.repository.rowmapper.CategoryRowMapper;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.PartyRowMapper;
import com.hs.ec.portal.repository.rowmapper.ProductRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Product entity.
 */
@SuppressWarnings("unused")
class ProductRepositoryInternalImpl extends SimpleR2dbcRepository<Product, Long> implements ProductRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CategoryRowMapper categoryMapper;
    private final PartyRowMapper partyMapper;
    private final ProductRowMapper productMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("product", EntityManager.ENTITY_ALIAS);
    private static final Table categoryTable = Table.aliased("category", "category");
    private static final Table partyTable = Table.aliased("party", "party");
    private static final Table parentTable = Table.aliased("product", "parent");

    private static final EntityManager.LinkTable documentsLink = new EntityManager.LinkTable(
        "rel_product__documents",
        "product_id",
        "documents_id"
    );

    public ProductRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CategoryRowMapper categoryMapper,
        PartyRowMapper partyMapper,
        ProductRowMapper productMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Product.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.categoryMapper = categoryMapper;
        this.partyMapper = partyMapper;
        this.productMapper = productMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Product> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Product> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProductSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CategorySqlHelper.getColumns(categoryTable, "category"));
        columns.addAll(PartySqlHelper.getColumns(partyTable, "party"));
        columns.addAll(ProductSqlHelper.getColumns(parentTable, "parent"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(categoryTable)
            .on(Column.create("category_id", entityTable))
            .equals(Column.create("id", categoryTable))
            .leftOuterJoin(partyTable)
            .on(Column.create("party_id", entityTable))
            .equals(Column.create("id", partyTable))
            .leftOuterJoin(parentTable)
            .on(Column.create("parent_id", entityTable))
            .equals(Column.create("id", parentTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Product.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Product> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Product> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Product> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Product> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Product> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Product process(Row row, RowMetadata metadata) {
        Product entity = productMapper.apply(row, "e");
        entity.setCategory(categoryMapper.apply(row, "category"));
        entity.setParty(partyMapper.apply(row, "party"));
        entity.setParent(productMapper.apply(row, "parent"));
        return entity;
    }

    @Override
    public <S extends Product> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Product> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(documentsLink, entity.getId(), entity.getDocuments().stream().map(FileDocument::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(documentsLink, entityId);
    }

    @Override
    public Flux<Product> findByCriteria(ProductCriteria productCriteria, Pageable page) {
        return createQuery(page, buildConditions(productCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ProductCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ProductCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getTypeClassId() != null) {
                builder.buildFilterConditionForField(criteria.getTypeClassId(), entityTable.column("type_class_id"));
            }
            if (criteria.getBrandClassId() != null) {
                builder.buildFilterConditionForField(criteria.getBrandClassId(), entityTable.column("brand_class_id"));
            }
            if (criteria.getSizee() != null) {
                builder.buildFilterConditionForField(criteria.getSizee(), entityTable.column("sizee"));
            }
            if (criteria.getRegularSizeClassId() != null) {
                builder.buildFilterConditionForField(criteria.getRegularSizeClassId(), entityTable.column("regular_size_class_id"));
            }
            if (criteria.getLanguageClassId() != null) {
                builder.buildFilterConditionForField(criteria.getLanguageClassId(), entityTable.column("language_class_id"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getKeywords() != null) {
                builder.buildFilterConditionForField(criteria.getKeywords(), entityTable.column("keywords"));
            }
            if (criteria.getCount() != null) {
                builder.buildFilterConditionForField(criteria.getCount(), entityTable.column("count"));
            }
            if (criteria.getDiscount() != null) {
                builder.buildFilterConditionForField(criteria.getDiscount(), entityTable.column("discount"));
            }
            if (criteria.getOriginalPrice() != null) {
                builder.buildFilterConditionForField(criteria.getOriginalPrice(), entityTable.column("original_price"));
            }
            if (criteria.getFinalPrice() != null) {
                builder.buildFilterConditionForField(criteria.getFinalPrice(), entityTable.column("final_price"));
            }
            if (criteria.getPublishDate() != null) {
                builder.buildFilterConditionForField(criteria.getPublishDate(), entityTable.column("publish_date"));
            }
            if (criteria.getTransportDate() != null) {
                builder.buildFilterConditionForField(criteria.getTransportDate(), entityTable.column("transport_date"));
            }
            if (criteria.getCurrencyClassId() != null) {
                builder.buildFilterConditionForField(criteria.getCurrencyClassId(), entityTable.column("currency_class_id"));
            }
            if (criteria.getBonus() != null) {
                builder.buildFilterConditionForField(criteria.getBonus(), entityTable.column("bonus"));
            }
            if (criteria.getWarrantyClassId() != null) {
                builder.buildFilterConditionForField(criteria.getWarrantyClassId(), entityTable.column("warranty_class_id"));
            }
            if (criteria.getDeliveryPlaceClassId() != null) {
                builder.buildFilterConditionForField(criteria.getDeliveryPlaceClassId(), entityTable.column("delivery_place_class_id"));
            }
            if (criteria.getPaymentPlaceClassId() != null) {
                builder.buildFilterConditionForField(criteria.getPaymentPlaceClassId(), entityTable.column("payment_place_class_id"));
            }
            if (criteria.getPerformance() != null) {
                builder.buildFilterConditionForField(criteria.getPerformance(), entityTable.column("performance"));
            }
            if (criteria.getOriginalityClassId() != null) {
                builder.buildFilterConditionForField(criteria.getOriginalityClassId(), entityTable.column("originality_class_id"));
            }
            if (criteria.getSatisfaction() != null) {
                builder.buildFilterConditionForField(criteria.getSatisfaction(), entityTable.column("satisfaction"));
            }
            if (criteria.getUsed() != null) {
                builder.buildFilterConditionForField(criteria.getUsed(), entityTable.column("used"));
            }
            if (criteria.getCategoryId() != null) {
                builder.buildFilterConditionForField(criteria.getCategoryId(), categoryTable.column("id"));
            }
            if (criteria.getPartyId() != null) {
                builder.buildFilterConditionForField(criteria.getPartyId(), partyTable.column("id"));
            }
            if (criteria.getParentId() != null) {
                builder.buildFilterConditionForField(criteria.getParentId(), parentTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
