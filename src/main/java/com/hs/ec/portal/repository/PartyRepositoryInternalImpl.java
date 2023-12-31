package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Party;
import com.hs.ec.portal.domain.criteria.PartyCriteria;
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
 * Spring Data R2DBC custom repository implementation for the Party entity.
 */
@SuppressWarnings("unused")
class PartyRepositoryInternalImpl extends SimpleR2dbcRepository<Party, Long> implements PartyRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PartyRowMapper partyMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("party", EntityManager.ENTITY_ALIAS);

    public PartyRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PartyRowMapper partyMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Party.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.partyMapper = partyMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Party> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Party> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PartySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Party.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Party> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Party> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Party process(Row row, RowMetadata metadata) {
        Party entity = partyMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Party> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Party> findByCriteria(PartyCriteria partyCriteria, Pageable page) {
        return createQuery(page, buildConditions(partyCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(PartyCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(PartyCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getTitle() != null) {
                builder.buildFilterConditionForField(criteria.getTitle(), entityTable.column("title"));
            }
            if (criteria.getPartyCode() != null) {
                builder.buildFilterConditionForField(criteria.getPartyCode(), entityTable.column("party_code"));
            }
            if (criteria.getTradeTitle() != null) {
                builder.buildFilterConditionForField(criteria.getTradeTitle(), entityTable.column("trade_title"));
            }
            if (criteria.getActivationDate() != null) {
                builder.buildFilterConditionForField(criteria.getActivationDate(), entityTable.column("activation_date"));
            }
            if (criteria.getExpirationDate() != null) {
                builder.buildFilterConditionForField(criteria.getExpirationDate(), entityTable.column("expiration_date"));
            }
            if (criteria.getActivationStatus() != null) {
                builder.buildFilterConditionForField(criteria.getActivationStatus(), entityTable.column("activation_status"));
            }
            if (criteria.getPersonType() != null) {
                builder.buildFilterConditionForField(criteria.getPersonType(), entityTable.column("person_type"));
            }
        }
        return builder.buildConditions();
    }
}
