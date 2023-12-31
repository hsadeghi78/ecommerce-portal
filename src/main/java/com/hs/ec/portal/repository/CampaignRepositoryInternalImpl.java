package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Campaign;
import com.hs.ec.portal.domain.Product;
import com.hs.ec.portal.domain.criteria.CampaignCriteria;
import com.hs.ec.portal.repository.rowmapper.CampaignRowMapper;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
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
 * Spring Data R2DBC custom repository implementation for the Campaign entity.
 */
@SuppressWarnings("unused")
class CampaignRepositoryInternalImpl extends SimpleR2dbcRepository<Campaign, Long> implements CampaignRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CampaignRowMapper campaignMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("campaign", EntityManager.ENTITY_ALIAS);

    private static final EntityManager.LinkTable productsLink = new EntityManager.LinkTable(
        "rel_campaign__products",
        "campaign_id",
        "products_id"
    );

    public CampaignRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CampaignRowMapper campaignMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Campaign.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.campaignMapper = campaignMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Campaign> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Campaign> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CampaignSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Campaign.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Campaign> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Campaign> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Campaign> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Campaign> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Campaign> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Campaign process(Row row, RowMetadata metadata) {
        Campaign entity = campaignMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Campaign> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Campaign> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(productsLink, entity.getId(), entity.getProducts().stream().map(Product::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(productsLink, entityId);
    }

    @Override
    public Flux<Campaign> findByCriteria(CampaignCriteria campaignCriteria, Pageable page) {
        return createQuery(page, buildConditions(campaignCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(CampaignCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(CampaignCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getTitle() != null) {
                builder.buildFilterConditionForField(criteria.getTitle(), entityTable.column("title"));
            }
            if (criteria.getStartDate() != null) {
                builder.buildFilterConditionForField(criteria.getStartDate(), entityTable.column("start_date"));
            }
            if (criteria.getEndDate() != null) {
                builder.buildFilterConditionForField(criteria.getEndDate(), entityTable.column("end_date"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
        }
        return builder.buildConditions();
    }
}
