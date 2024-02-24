package it.ibl.portal.domain;

import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Utente.
 */
@Document(collection = "utente")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "utente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Field("created")
    private LocalDate created;

    @Field("modified")
    private LocalDate modified;

    @Field("username")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String username;

    @Field("password")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String password;

    @Field("mail")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String mail;

    @Field("mobile")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String mobile;

    @Field("facebook")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String facebook;

    @Field("google")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String google;

    @Field("instangram")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String instangram;

    @Field("provider")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String provider;

    @Field("attivo")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean attivo;

    @Field("motivo_bolocco")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String motivoBolocco;

    @Field("data_bolocco")
    private LocalDate dataBolocco;

    @Field("registration_date")
    private LocalDate registrationDate;

    @Field("last_access")
    private LocalDate lastAccess;

    @DBRef
    @Field("ruolo")
    private Ruolo ruolo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreated() {
        return this.created;
    }

    public Utente created(LocalDate created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getModified() {
        return this.modified;
    }

    public Utente modified(LocalDate modified) {
        this.setModified(modified);
        return this;
    }

    public void setModified(LocalDate modified) {
        this.modified = modified;
    }

    public String getUsername() {
        return this.username;
    }

    public Utente username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public Utente password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return this.mail;
    }

    public Utente mail(String mail) {
        this.setMail(mail);
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Utente mobile(String mobile) {
        this.setMobile(mobile);
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public Utente facebook(String facebook) {
        this.setFacebook(facebook);
        return this;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGoogle() {
        return this.google;
    }

    public Utente google(String google) {
        this.setGoogle(google);
        return this;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getInstangram() {
        return this.instangram;
    }

    public Utente instangram(String instangram) {
        this.setInstangram(instangram);
        return this;
    }

    public void setInstangram(String instangram) {
        this.instangram = instangram;
    }

    public String getProvider() {
        return this.provider;
    }

    public Utente provider(String provider) {
        this.setProvider(provider);
        return this;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Boolean getAttivo() {
        return this.attivo;
    }

    public Utente attivo(Boolean attivo) {
        this.setAttivo(attivo);
        return this;
    }

    public void setAttivo(Boolean attivo) {
        this.attivo = attivo;
    }

    public String getMotivoBolocco() {
        return this.motivoBolocco;
    }

    public Utente motivoBolocco(String motivoBolocco) {
        this.setMotivoBolocco(motivoBolocco);
        return this;
    }

    public void setMotivoBolocco(String motivoBolocco) {
        this.motivoBolocco = motivoBolocco;
    }

    public LocalDate getDataBolocco() {
        return this.dataBolocco;
    }

    public Utente dataBolocco(LocalDate dataBolocco) {
        this.setDataBolocco(dataBolocco);
        return this;
    }

    public void setDataBolocco(LocalDate dataBolocco) {
        this.dataBolocco = dataBolocco;
    }

    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public Utente registrationDate(LocalDate registrationDate) {
        this.setRegistrationDate(registrationDate);
        return this;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getLastAccess() {
        return this.lastAccess;
    }

    public Utente lastAccess(LocalDate lastAccess) {
        this.setLastAccess(lastAccess);
        return this;
    }

    public void setLastAccess(LocalDate lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Ruolo getRuolo() {
        return this.ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    public Utente ruolo(Ruolo ruolo) {
        this.setRuolo(ruolo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utente)) {
            return false;
        }
        return getId() != null && getId().equals(((Utente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utente{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", mail='" + getMail() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", facebook='" + getFacebook() + "'" +
            ", google='" + getGoogle() + "'" +
            ", instangram='" + getInstangram() + "'" +
            ", provider='" + getProvider() + "'" +
            ", attivo='" + getAttivo() + "'" +
            ", motivoBolocco='" + getMotivoBolocco() + "'" +
            ", dataBolocco='" + getDataBolocco() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", lastAccess='" + getLastAccess() + "'" +
            "}";
    }
}
