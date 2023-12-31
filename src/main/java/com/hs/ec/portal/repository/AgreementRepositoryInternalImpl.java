package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Agreement;
import com.hs.ec.portal.domain.criteria.AgreementCriteria;
import com.hs.ec.portal.repository.rowmapper.AgreementRowMapper;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
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
 * Spring Data R2DBC custom repository implementation for the Agreement entity.
 */
@SuppressWarnings("unused")
class AgreementRepositoryInternalImpl extends SimpleR2dbcRepository<Agreement, Long> implements AgreementRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PartyRowMapper partyMapper;
    private final AgreementRowMapper agreementMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("agreement", EntityManager.ENTITY_ALIAS);
    private static final Table providerTable = Table.aliased("party", "provider");
    private static final Table consumerTable = Table.aliased("party", "consumer");

    public AgreementRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PartyRowMapper partyMapper,
        AgreementRowMapper agreementMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Agreement.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.partyMapper = partyMapper;
        this.agreementMapper = agreementMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Agreement> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Agreement> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AgreementSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PartySqlHelper.getColumns(providerTable, "provider"));
        columns.addAll(PartySqlHelper.getColumns(consumerTable, "consumer"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(providerTable)
            .on(Column.create("provider_id", entityTable))
            .equals(Column.create("id", providerTable))
            .leftOuterJoin(consumerTable)
            .on(Column.create("consumer_id", entityTable))
            .equals(Column.create("id", consumerTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Agreement.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Agreement> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Agreement> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Agreement> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Agreement> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Agreement> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Agreement process(Row row, RowMetadata metadata) {
        Agreement entity = agreementMapper.apply(row, "e");
        entity.setProvider(partyMapper.apply(row, "provider"));
        entity.setConsumer(partyMapper.apply(row, "consumer"));
        return entity;
    }

    @Override
    public <S extends Agreement> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Agreement> findByCriteria(AgreementCriteria agreementCriteria, Pageable page) {
        return createQuery(page, buildConditions(agreementCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(AgreementCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(AgreementCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getStartDate() != null) {
                builder.buildFilterConditionForField(criteria.getStartDate(), entityTable.column("start_date"));
            }
            if (criteria.getEndDate() != null) {
                builder.buildFilterConditionForField(criteria.getEndDate(), entityTable.column("end_date"));
            }
            if (criteria.getActivationStatusClassId() != null) {
                builder.buildFilterConditionForField(
                    criteria.getActivationStatusClassId(),
                    entityTable.column("activation_status_class_id")
                );
            }
            if (criteria.getInfrastructureBenefit() != null) {
                builder.buildFilterConditionForField(criteria.getInfrastructureBenefit(), entityTable.column("infrastructure_benefit"));
            }
            if (criteria.getExtraBenefit() != null) {
                builder.buildFilterConditionForField(criteria.getExtraBenefit(), entityTable.column("extra_benefit"));
            }
            if (criteria.getProviderId() != null) {
                builder.buildFilterConditionForField(criteria.getProviderId(), providerTable.column("id"));
            }
            if (criteria.getConsumerId() != null) {
                builder.buildFilterConditionForField(criteria.getConsumerId(), consumerTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
