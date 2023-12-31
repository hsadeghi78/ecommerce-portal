package com.hs.ec.portal.repository.rowmapper;

import com.hs.ec.portal.domain.FileDocument;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FileDocument}, with proper type conversions.
 */
@Service
public class FileDocumentRowMapper implements BiFunction<Row, String, FileDocument> {

    private final ColumnConverter converter;

    public FileDocumentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FileDocument} stored in the database.
     */
    @Override
    public FileDocument apply(Row row, String prefix) {
        FileDocument entity = new FileDocument();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFileName(converter.fromRow(row, prefix + "_file_name", String.class));
        entity.setFileContentContentType(converter.fromRow(row, prefix + "_file_content_content_type", String.class));
        entity.setFileContent(converter.fromRow(row, prefix + "_file_content", byte[].class));
        entity.setFilePath(converter.fromRow(row, prefix + "_file_path", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}
