package com.hs.ec.portal.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Config.
 */
@Table("bs_config")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Size(max = 300)
    @Column("display_name")
    private String displayName;

    @Size(max = 300)
    @Column("code")
    private String code;

    @Size(max = 1500)
    @Column("value")
    private String value;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Config id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Config displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCode() {
        return this.code;
    }

    public Config code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return this.value;
    }

    public Config value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Config)) {
            return false;
        }
        return getId() != null && getId().equals(((Config) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Config{" +
            "id=" + getId() +
            ", displayName='" + getDisplayName() + "'" +
            ", code='" + getCode() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
