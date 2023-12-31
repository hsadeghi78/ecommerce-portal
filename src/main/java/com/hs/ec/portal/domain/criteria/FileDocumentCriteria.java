package com.hs.ec.portal.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hs.ec.portal.domain.FileDocument} entity. This class is used
 * in {@link com.hs.ec.portal.web.rest.FileDocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /file-documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileDocumentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fileName;

    private StringFilter filePath;

    private StringFilter description;

    private Boolean distinct;

    public FileDocumentCriteria() {}

    public FileDocumentCriteria(FileDocumentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fileName = other.fileName == null ? null : other.fileName.copy();
        this.filePath = other.filePath == null ? null : other.filePath.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FileDocumentCriteria copy() {
        return new FileDocumentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFileName() {
        return fileName;
    }

    public StringFilter fileName() {
        if (fileName == null) {
            fileName = new StringFilter();
        }
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }

    public StringFilter getFilePath() {
        return filePath;
    }

    public StringFilter filePath() {
        if (filePath == null) {
            filePath = new StringFilter();
        }
        return filePath;
    }

    public void setFilePath(StringFilter filePath) {
        this.filePath = filePath;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FileDocumentCriteria that = (FileDocumentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(filePath, that.filePath) &&
            Objects.equals(description, that.description) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, filePath, description, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileDocumentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fileName != null ? "fileName=" + fileName + ", " : "") +
            (filePath != null ? "filePath=" + filePath + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
