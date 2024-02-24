package it.ibl.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Azione.
 */
@Document(collection = "azione")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "azione")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Azione implements Serializable {

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

    @Field("descrizione")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String descrizione;

    @DBRef
    @Field("ruolo")
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "azioni" }, allowSetters = true)
    private Set<Ruolo> ruolos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Azione id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreated() {
        return this.created;
    }

    public Azione created(LocalDate created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getModified() {
        return this.modified;
    }

    public Azione modified(LocalDate modified) {
        this.setModified(modified);
        return this;
    }

    public void setModified(LocalDate modified) {
        this.modified = modified;
    }

    public String getNomeAzione() {
        return this.nomeAzione;
    }

    public Azione nomeAzione(String nomeAzione) {
        this.setNomeAzione(nomeAzione);
        return this;
    }

    public void setNomeAzione(String nomeAzione) {
        this.nomeAzione = nomeAzione;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public Azione descrizione(String descrizione) {
        this.setDescrizione(descrizione);
        return this;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Set<Ruolo> getRuolos() {
        return this.ruolos;
    }

    public void setRuolos(Set<Ruolo> ruolos) {
        if (this.ruolos != null) {
            this.ruolos.forEach(i -> i.setAzioni(null));
        }
        if (ruolos != null) {
            ruolos.forEach(i -> i.setAzioni(this));
        }
        this.ruolos = ruolos;
    }

    public Azione ruolos(Set<Ruolo> ruolos) {
        this.setRuolos(ruolos);
        return this;
    }

    public Azione addRuolo(Ruolo ruolo) {
        this.ruolos.add(ruolo);
        ruolo.setAzioni(this);
        return this;
    }

    public Azione removeRuolo(Ruolo ruolo) {
        this.ruolos.remove(ruolo);
        ruolo.setAzioni(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Azione)) {
            return false;
        }
        return getId() != null && getId().equals(((Azione) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Azione{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            ", nomeAzione='" + getNomeAzione() + "'" +
            ", descrizione='" + getDescrizione() + "'" +
            "}";
    }
}
