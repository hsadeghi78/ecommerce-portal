package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.VwClassification;
import com.hs.ec.portal.domain.criteria.VwClassificationCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.VwClassificationRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the VwClassification entity.
 */
@SuppressWarnings("unused")
class VwClassificationRepositoryInternalImpl
    extends SimpleR2dbcRepository<VwClassification, Long>
    implements VwClassificationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final VwClassificationRowMapper vwclassificationMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("vw_classification", EntityManager.ENTITY_ALIAS);

    public VwClassificationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        VwClassificationRowMapper vwclassificationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(VwClassification.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.vwclassificationMapper = vwclassificationMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<VwClassification> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<VwClassification> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = VwClassificationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, VwClassification.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<VwClassification> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<VwClassification> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private VwClassification process(Row row, RowMetadata metadata) {
        VwClassification entity = vwclassificationMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends VwClassification> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<VwClassification> findByCriteria(VwClassificationCriteria vwClassificationCriteria, Pageable page) {
        return createQuery(page, buildConditions(vwClassificationCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(VwClassificationCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(VwClassificationCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getTitle() != null) {
                builder.buildFilterConditionForField(criteria.getTitle(), entityTable.column("title"));
            }
            if (criteria.getClassCode() != null) {
                builder.buildFilterConditionForField(criteria.getClassCode(), entityTable.column("class_code"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getLanguageClassId() != null) {
                builder.buildFilterConditionForField(criteria.getLanguageClassId(), entityTable.column("language_class_id"));
            }
            if (criteria.getTypeTitle() != null) {
                builder.buildFilterConditionForField(criteria.getTypeTitle(), entityTable.column("type_title"));
            }
            if (criteria.getTypeCode() != null) {
                builder.buildFilterConditionForField(criteria.getTypeCode(), entityTable.column("type_code"));
            }
            if (criteria.getTypeDesc() != null) {
                builder.buildFilterConditionForField(criteria.getTypeDesc(), entityTable.column("type_desc"));
            }
        }
        return builder.buildConditions();
    }
}
