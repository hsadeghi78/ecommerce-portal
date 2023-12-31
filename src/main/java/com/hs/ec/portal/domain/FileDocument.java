package com.hs.ec.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Hossein Sadeghi (hsadeghi78@gmail.com)
 */
@Table("file_document")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 250)
    @Column("file_name")
    private String fileName;

    @Column("file_content")
    private byte[] fileContent;

    @NotNull
    @Column("file_content_content_type")
    private String fileContentContentType;

    @Size(max = 2000)
    @Column("file_path")
    private String filePath;

    @NotNull(message = "must not be null")
    @Size(max = 3000)
    @Column("description")
    private String description;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "productItems",
            "userComments",
            "children",
            "favorites",
            "materials",
            "factorItems",
            "documents",
            "category",
            "party",
            "parent",
            "campaigns",
        },
        allowSetters = true
    )
    private Set<Product> prices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FileDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public FileDocument fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return this.fileContent;
    }

    public FileDocument fileContent(byte[] fileContent) {
        this.setFileContent(fileContent);
        return this;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContentContentType() {
        return this.fileContentContentType;
    }

    public FileDocument fileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
        return this;
    }

    public void setFileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public FileDocument filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return this.description;
    }

    public FileDocument description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Product> getPrices() {
        return this.prices;
    }

    public void setPrices(Set<Product> products) {
        if (this.prices != null) {
            this.prices.forEach(i -> i.removeDocuments(this));
        }
        if (products != null) {
            products.forEach(i -> i.addDocuments(this));
        }
        this.prices = products;
    }

    public FileDocument prices(Set<Product> products) {
        this.setPrices(products);
        return this;
    }

    public FileDocument addPrices(Product product) {
        this.prices.add(product);
        product.getDocuments().add(this);
        return this;
    }

    public FileDocument removePrices(Product product) {
        this.prices.remove(product);
        product.getDocuments().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((FileDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileDocument{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileContent='" + getFileContent() + "'" +
            ", fileContentContentType='" + getFileContentContentType() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
