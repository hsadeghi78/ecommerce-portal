package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Contact;
import com.hs.ec.portal.domain.criteria.ContactCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.ContactRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Contact entity.
 */
@SuppressWarnings("unused")
class ContactRepositoryInternalImpl extends SimpleR2dbcRepository<Contact, Long> implements ContactRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PartyRowMapper partyMapper;
    private final ContactRowMapper contactMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("contact", EntityManager.ENTITY_ALIAS);
    private static final Table partyTable = Table.aliased("party", "party");

    public ContactRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PartyRowMapper partyMapper,
        ContactRowMapper contactMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Contact.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.partyMapper = partyMapper;
        this.contactMapper = contactMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Contact> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Contact> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ContactSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PartySqlHelper.getColumns(partyTable, "party"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(partyTable)
            .on(Column.create("party_id", entityTable))
            .equals(Column.create("id", partyTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Contact.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Contact> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Contact> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Contact> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Contact> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Contact> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Contact process(Row row, RowMetadata metadata) {
        Contact entity = contactMapper.apply(row, "e");
        entity.setParty(partyMapper.apply(row, "party"));
        return entity;
    }

    @Override
    public <S extends Contact> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Contact> findByCriteria(ContactCriteria contactCriteria, Pageable page) {
        return createQuery(page, buildConditions(contactCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ContactCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ContactCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getContactValue() != null) {
                builder.buildFilterConditionForField(criteria.getContactValue(), entityTable.column("contact_value"));
            }
            if (criteria.getTypeClassId() != null) {
                builder.buildFilterConditionForField(criteria.getTypeClassId(), entityTable.column("type_class_id"));
            }
            if (criteria.getPrefix() != null) {
                builder.buildFilterConditionForField(criteria.getPrefix(), entityTable.column("prefix"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getPartyId() != null) {
                builder.buildFilterConditionForField(criteria.getPartyId(), partyTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
