package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Factor;
import com.hs.ec.portal.domain.criteria.FactorCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.FactorRowMapper;
import com.hs.ec.portal.repository.rowmapper.LocationRowMapper;
import com.hs.ec.portal.repository.rowmapper.PartyRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Factor entity.
 */
@SuppressWarnings("unused")
class FactorRepositoryInternalImpl extends SimpleR2dbcRepository<Factor, Long> implements FactorRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final LocationRowMapper locationMapper;
    private final PartyRowMapper partyMapper;
    private final FactorRowMapper factorMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("factor", EntityManager.ENTITY_ALIAS);
    private static final Table locationTable = Table.aliased("location", "location");
    private static final Table buyerPartyTable = Table.aliased("party", "buyerParty");
    private static final Table sellerPartyTable = Table.aliased("party", "sellerParty");

    public FactorRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        LocationRowMapper locationMapper,
        PartyRowMapper partyMapper,
        FactorRowMapper factorMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Factor.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.locationMapper = locationMapper;
        this.partyMapper = partyMapper;
        this.factorMapper = factorMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Factor> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Factor> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FactorSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(LocationSqlHelper.getColumns(locationTable, "location"));
        columns.addAll(PartySqlHelper.getColumns(buyerPartyTable, "buyerParty"));
        columns.addAll(PartySqlHelper.getColumns(sellerPartyTable, "sellerParty"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(locationTable)
            .on(Column.create("location_id", entityTable))
            .equals(Column.create("id", locationTable))
            .leftOuterJoin(buyerPartyTable)
            .on(Column.create("buyer_party_id", entityTable))
            .equals(Column.create("id", buyerPartyTable))
            .leftOuterJoin(sellerPartyTable)
            .on(Column.create("seller_party_id", entityTable))
            .equals(Column.create("id", sellerPartyTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Factor.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Factor> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Factor> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Factor> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Factor> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Factor> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Factor process(Row row, RowMetadata metadata) {
        Factor entity = factorMapper.apply(row, "e");
        entity.setLocation(locationMapper.apply(row, "location"));
        entity.setBuyerParty(partyMapper.apply(row, "buyerParty"));
        entity.setSellerParty(partyMapper.apply(row, "sellerParty"));
        return entity;
    }

    @Override
    public <S extends Factor> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Factor> findByCriteria(FactorCriteria factorCriteria, Pageable page) {
        return createQuery(page, buildConditions(factorCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(FactorCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(FactorCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getTitle() != null) {
                builder.buildFilterConditionForField(criteria.getTitle(), entityTable.column("title"));
            }
            if (criteria.getFactorCode() != null) {
                builder.buildFilterConditionForField(criteria.getFactorCode(), entityTable.column("factor_code"));
            }
            if (criteria.getLastStatusClassId() != null) {
                builder.buildFilterConditionForField(criteria.getLastStatusClassId(), entityTable.column("last_status_class_id"));
            }
            if (criteria.getPaymentStateClassId() != null) {
                builder.buildFilterConditionForField(criteria.getPaymentStateClassId(), entityTable.column("payment_state_class_id"));
            }
            if (criteria.getCategoryClassId() != null) {
                builder.buildFilterConditionForField(criteria.getCategoryClassId(), entityTable.column("category_class_id"));
            }
            if (criteria.getTotalPrice() != null) {
                builder.buildFilterConditionForField(criteria.getTotalPrice(), entityTable.column("total_price"));
            }
            if (criteria.getDiscount() != null) {
                builder.buildFilterConditionForField(criteria.getDiscount(), entityTable.column("discount"));
            }
            if (criteria.getDiscountCode() != null) {
                builder.buildFilterConditionForField(criteria.getDiscountCode(), entityTable.column("discount_code"));
            }
            if (criteria.getFinalTax() != null) {
                builder.buildFilterConditionForField(criteria.getFinalTax(), entityTable.column("final_tax"));
            }
            if (criteria.getPayable() != null) {
                builder.buildFilterConditionForField(criteria.getPayable(), entityTable.column("payable"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getLocationId() != null) {
                builder.buildFilterConditionForField(criteria.getLocationId(), locationTable.column("id"));
            }
            if (criteria.getBuyerPartyId() != null) {
                builder.buildFilterConditionForField(criteria.getBuyerPartyId(), buyerPartyTable.column("id"));
            }
            if (criteria.getSellerPartyId() != null) {
                builder.buildFilterConditionForField(criteria.getSellerPartyId(), sellerPartyTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
