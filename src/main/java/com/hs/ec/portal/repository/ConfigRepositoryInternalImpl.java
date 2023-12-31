package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Config;
import com.hs.ec.portal.domain.criteria.ConfigCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.ConfigRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Config entity.
 */
@SuppressWarnings("unused")
class ConfigRepositoryInternalImpl extends SimpleR2dbcRepository<Config, Long> implements ConfigRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ConfigRowMapper configMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("bs_config", EntityManager.ENTITY_ALIAS);

    public ConfigRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ConfigRowMapper configMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Config.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.configMapper = configMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Config> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Config> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ConfigSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Config.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Config> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Config> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Config process(Row row, RowMetadata metadata) {
        Config entity = configMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Config> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Config> findByCriteria(ConfigCriteria configCriteria, Pageable page) {
        return createQuery(page, buildConditions(configCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ConfigCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ConfigCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getDisplayName() != null) {
                builder.buildFilterConditionForField(criteria.getDisplayName(), entityTable.column("display_name"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
            if (criteria.getValue() != null) {
                builder.buildFilterConditionForField(criteria.getValue(), entityTable.column("value"));
            }
        }
        return builder.buildConditions();
    }
}
