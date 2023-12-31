package com.hs.ec.portal.repository;

import com.hs.ec.portal.domain.FileDocument;
import com.hs.ec.portal.domain.criteria.FileDocumentCriteria;
import com.hs.ec.portal.repository.rowmapper.ColumnConverter;
import com.hs.ec.portal.repository.rowmapper.FileDocumentRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the FileDocument entity.
 */
@SuppressWarnings("unused")
class FileDocumentRepositoryInternalImpl extends SimpleR2dbcRepository<FileDocument, Long> implements FileDocumentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FileDocumentRowMapper filedocumentMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("file_document", EntityManager.ENTITY_ALIAS);

    public FileDocumentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FileDocumentRowMapper filedocumentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(FileDocument.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.filedocumentMapper = filedocumentMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<FileDocument> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<FileDocument> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FileDocumentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, FileDocument.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<FileDocument> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<FileDocument> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private FileDocument process(Row row, RowMetadata metadata) {
        FileDocument entity = filedocumentMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends FileDocument> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<FileDocument> findByCriteria(FileDocumentCriteria fileDocumentCriteria, Pageable page) {
        return createQuery(page, buildConditions(fileDocumentCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(FileDocumentCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(FileDocumentCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getFileName() != null) {
                builder.buildFilterConditionForField(criteria.getFileName(), entityTable.column("file_name"));
            }
            if (criteria.getFilePath() != null) {
                builder.buildFilterConditionForField(criteria.getFilePath(), entityTable.column("file_path"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
        }
        return builder.buildConditions();
    }
}
