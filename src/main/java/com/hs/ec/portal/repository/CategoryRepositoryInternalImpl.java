package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.Category;
import com.hs.ec.portal.domain.criteria.CategoryCriteria;
import com.hs.ec.portal.repository.rowmapper.CategoryRowMapper;
import com.hs.ec.portal.repository.rowmapper.CategoryRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Category entity.
 */
@SuppressWarnings("unused")
class CategoryRepositoryInternalImpl extends SimpleR2dbcRepository<Category, Long> implements CategoryRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CategoryRowMapper categoryMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("category", EntityManager.ENTITY_ALIAS);
    private static final Table parentTable = Table.aliased("category", "parent");

    public CategoryRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CategoryRowMapper categoryMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Category.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.categoryMapper = categoryMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Category> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Category> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CategorySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CategorySqlHelper.getColumns(parentTable, "parent"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(parentTable)
            .on(Column.create("parent_id", entityTable))
            .equals(Column.create("id", parentTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Category.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Category> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Category> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Category> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Category> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Category> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Category process(Row row, RowMetadata metadata) {
        Category entity = categoryMapper.apply(row, "e");
        entity.setParent(categoryMapper.apply(row, "parent"));
        return entity;
    }

    @Override
    public <S extends Category> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Category> findByCriteria(CategoryCriteria categoryCriteria, Pageable page) {
        return createQuery(page, buildConditions(categoryCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(CategoryCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(CategoryCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getTitle() != null) {
                builder.buildFilterConditionForField(criteria.getTitle(), entityTable.column("title"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
            if (criteria.getHasChild() != null) {
                builder.buildFilterConditionForField(criteria.getHasChild(), entityTable.column("has_child"));
            }
            if (criteria.getLevel() != null) {
                builder.buildFilterConditionForField(criteria.getLevel(), entityTable.column("level"));
            }
            if (criteria.getKeywords() != null) {
                builder.buildFilterConditionForField(criteria.getKeywords(), entityTable.column("keywords"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getParentId() != null) {
                builder.buildFilterConditionForField(criteria.getParentId(), parentTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
