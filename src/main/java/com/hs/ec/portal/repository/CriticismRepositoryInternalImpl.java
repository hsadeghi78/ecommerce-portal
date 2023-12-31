package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Criticism;
import com.hs.ec.portal.domain.criteria.CriticismCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.CriticismRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Criticism entity.
 */
@SuppressWarnings("unused")
class CriticismRepositoryInternalImpl extends SimpleR2dbcRepository<Criticism, Long> implements CriticismRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CriticismRowMapper criticismMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("criticism", EntityManager.ENTITY_ALIAS);

    public CriticismRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CriticismRowMapper criticismMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Criticism.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.criticismMapper = criticismMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Criticism> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Criticism> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CriticismSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Criticism.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Criticism> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Criticism> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Criticism process(Row row, RowMetadata metadata) {
        Criticism entity = criticismMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Criticism> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Criticism> findByCriteria(CriticismCriteria criticismCriteria, Pageable page) {
        return createQuery(page, buildConditions(criticismCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(CriticismCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(CriticismCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getFullName() != null) {
                builder.buildFilterConditionForField(criteria.getFullName(), entityTable.column("full_name"));
            }
            if (criteria.getEmail() != null) {
                builder.buildFilterConditionForField(criteria.getEmail(), entityTable.column("email"));
            }
            if (criteria.getContactNumber() != null) {
                builder.buildFilterConditionForField(criteria.getContactNumber(), entityTable.column("contact_number"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
        }
        return builder.buildConditions();
    }
}
