package it.ibl.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Ruolo.
 */
@Document(collection = "ruolo")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ruolo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ruolo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field("created")
    private LocalDate created;

    @Field("modified")
    private LocalDate modified;

    @Field("nome_azione")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nomeAzione;

    @DBRef
    @Field("azioni")
    @JsonIgnoreProperties(value = { "ruolos" }, allowSetters = true)
    private Azione azioni;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ruolo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreated() {
        return this.created;
    }

    public Ruolo created(LocalDate created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getModified() {
        return this.modified;
    }

    public Ruolo modified(LocalDate modified) {
        this.setModified(modified);
        return this;
    }

    public void setModified(LocalDate modified) {
        this.modified = modified;
    }

    public String getNomeAzione() {
        return this.nomeAzione;
    }

    public Ruolo nomeAzione(String nomeAzione) {
        this.setNomeAzione(nomeAzione);
        return this;
    }

    public void setNomeAzione(String nomeAzione) {
        this.nomeAzione = nomeAzione;
    }

    public Azione getAzioni() {
        return this.azioni;
    }

    public void setAzioni(Azione azione) {
        this.azioni = azione;
    }

    public Ruolo azioni(Azione azione) {
        this.setAzioni(azione);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ruolo)) {
            return false;
        }
        return getId() != null && getId().equals(((Ruolo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ruolo{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            ", nomeAzione='" + getNomeAzione() + "'" +
            "}";
    }
}
