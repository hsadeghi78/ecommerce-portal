package com.hs.ec.portal.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hs.ec.portal.domain.FileDocument} entity.
 */
@Schema(description = "@author Hossein Sadeghi (hsadeghi78@gmail.com)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileDocumentDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 250)
    private String fileName;

    @Lob
    private byte[] fileContent;

    private String fileContentContentType;

    @Size(max = 2000)
    private String filePath;

    @NotNull(message = "must not be null")
    @Size(max = 3000)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContentContentType() {
        return fileContentContentType;
    }

    public void setFileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileDocumentDTO)) {
            return false;
        }

        FileDocumentDTO fileDocumentDTO = (FileDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileDocumentDTO{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileContent='" + getFileContent() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
