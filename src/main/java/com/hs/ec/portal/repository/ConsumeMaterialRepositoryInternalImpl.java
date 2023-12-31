package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.ConsumeMaterial;
import com.hs.ec.portal.domain.criteria.ConsumeMaterialCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.ConsumeMaterialRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ConsumeMaterial entity.
 */
@SuppressWarnings("unused")
class ConsumeMaterialRepositoryInternalImpl
    extends SimpleR2dbcRepository<ConsumeMaterial, Long>
    implements ConsumeMaterialRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProductRowMapper productMapper;
    private final ConsumeMaterialRowMapper consumematerialMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("consume_material", EntityManager.ENTITY_ALIAS);
    private static final Table productTable = Table.aliased("product", "product");

    public ConsumeMaterialRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProductRowMapper productMapper,
        ConsumeMaterialRowMapper consumematerialMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ConsumeMaterial.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.productMapper = productMapper;
        this.consumematerialMapper = consumematerialMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<ConsumeMaterial> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ConsumeMaterial> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ConsumeMaterialSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProductSqlHelper.getColumns(productTable, "product"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(productTable)
            .on(Column.create("product_id", entityTable))
            .equals(Column.create("id", productTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ConsumeMaterial.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ConsumeMaterial> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ConsumeMaterial> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<ConsumeMaterial> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<ConsumeMaterial> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<ConsumeMaterial> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private ConsumeMaterial process(Row row, RowMetadata metadata) {
        ConsumeMaterial entity = consumematerialMapper.apply(row, "e");
        entity.setProduct(productMapper.apply(row, "product"));
        return entity;
    }

    @Override
    public <S extends ConsumeMaterial> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<ConsumeMaterial> findByCriteria(ConsumeMaterialCriteria consumeMaterialCriteria, Pageable page) {
        return createQuery(page, buildConditions(consumeMaterialCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ConsumeMaterialCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ConsumeMaterialCriteria criteria) {
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
