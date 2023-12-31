package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Wallet;
import com.hs.ec.portal.domain.criteria.WalletCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.WalletRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Wallet entity.
 */
@SuppressWarnings("unused")
class WalletRepositoryInternalImpl extends SimpleR2dbcRepository<Wallet, Long> implements WalletRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WalletRowMapper walletMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("wallet", EntityManager.ENTITY_ALIAS);

    public WalletRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WalletRowMapper walletMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Wallet.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.walletMapper = walletMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Wallet> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Wallet> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = WalletSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Wallet.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Wallet> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Wallet> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Wallet process(Row row, RowMetadata metadata) {
        Wallet entity = walletMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Wallet> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Wallet> findByCriteria(WalletCriteria walletCriteria, Pageable page) {
        return createQuery(page, buildConditions(walletCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(WalletCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(WalletCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getTransTypeClassId() != null) {
                builder.buildFilterConditionForField(criteria.getTransTypeClassId(), entityTable.column("trans_type_class_id"));
            }
            if (criteria.getStock() != null) {
                builder.buildFilterConditionForField(criteria.getStock(), entityTable.column("stock"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getDeposit() != null) {
                builder.buildFilterConditionForField(criteria.getDeposit(), entityTable.column("deposit"));
            }
            if (criteria.getWithdrawal() != null) {
                builder.buildFilterConditionForField(criteria.getWithdrawal(), entityTable.column("withdrawal"));
            }
        }
        return builder.buildConditions();
    }
}
