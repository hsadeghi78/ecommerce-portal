package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.ProductHistory;
import com.hs.ec.portal.domain.criteria.ProductHistoryCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.ProductHistoryRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ProductHistory entity.
 */
@SuppressWarnings("unused")
class ProductHistoryRepositoryInternalImpl extends SimpleR2dbcRepository<ProductHistory, Long> implements ProductHistoryRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProductHistoryRowMapper producthistoryMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("price_history", EntityManager.ENTITY_ALIAS);

    public ProductHistoryRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProductHistoryRowMapper producthistoryMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ProductHistory.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.producthistoryMapper = producthistoryMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<ProductHistory> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ProductHistory> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProductHistorySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ProductHistory.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ProductHistory> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ProductHistory> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private ProductHistory process(Row row, RowMetadata metadata) {
        ProductHistory entity = producthistoryMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ProductHistory> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<ProductHistory> findByCriteria(ProductHistoryCriteria productHistoryCriteria, Pageable page) {
        return createQuery(page, buildConditions(productHistoryCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ProductHistoryCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ProductHistoryCriteria criteria) {
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
                builder.buildFilterConditionForField(criteria.getCategoryId(), entityTable.column("category_id"));
            }
            if (criteria.getPartyId() != null) {
                builder.buildFilterConditionForField(criteria.getPartyId(), entityTable.column("party_id"));
            }
            if (criteria.getProductId() != null) {
                builder.buildFilterConditionForField(criteria.getProductId(), entityTable.column("product_id"));
            }
            if (criteria.getPriceId() != null) {
                builder.buildFilterConditionForField(criteria.getPriceId(), entityTable.column("price_id"));
            }
            if (criteria.getCampaignId() != null) {
                builder.buildFilterConditionForField(criteria.getCampaignId(), entityTable.column("campaign_id"));
            }
        }
        return builder.buildConditions();
    }
}
