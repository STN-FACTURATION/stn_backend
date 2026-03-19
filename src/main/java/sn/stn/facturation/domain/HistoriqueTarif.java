package sn.stn.facturation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Historique des modifications de tarifs
 */
@Entity
@Table(name = "historique_tarif")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueTarif implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ancienne_valeur", nullable = false)
    private Double ancienneValeur;

    @NotNull
    @Column(name = "nouvelle_valeur", nullable = false)
    private Double nouvelleValeur;

    @NotNull
    @Column(name = "date_modification", nullable = false)
    private Instant dateModification;

    @Size(max = 500)
    @Column(name = "commentaire", length = 500)
    private String commentaire;

    @Size(max = 50)
    @Column(name = "modifie_par_login", length = 50)
    private String modifieParLogin;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "historiques" }, allowSetters = true)
    private Tarif tarif;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoriqueTarif id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAncienneValeur() {
        return this.ancienneValeur;
    }

    public HistoriqueTarif ancienneValeur(Double ancienneValeur) {
        this.setAncienneValeur(ancienneValeur);
        return this;
    }

    public void setAncienneValeur(Double ancienneValeur) {
        this.ancienneValeur = ancienneValeur;
    }

    public Double getNouvelleValeur() {
        return this.nouvelleValeur;
    }

    public HistoriqueTarif nouvelleValeur(Double nouvelleValeur) {
        this.setNouvelleValeur(nouvelleValeur);
        return this;
    }

    public void setNouvelleValeur(Double nouvelleValeur) {
        this.nouvelleValeur = nouvelleValeur;
    }

    public Instant getDateModification() {
        return this.dateModification;
    }

    public HistoriqueTarif dateModification(Instant dateModification) {
        this.setDateModification(dateModification);
        return this;
    }

    public void setDateModification(Instant dateModification) {
        this.dateModification = dateModification;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public HistoriqueTarif commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getModifieParLogin() {
        return this.modifieParLogin;
    }

    public HistoriqueTarif modifieParLogin(String modifieParLogin) {
        this.setModifieParLogin(modifieParLogin);
        return this;
    }

    public void setModifieParLogin(String modifieParLogin) {
        this.modifieParLogin = modifieParLogin;
    }

    public Tarif getTarif() {
        return this.tarif;
    }

    public void setTarif(Tarif tarif) {
        this.tarif = tarif;
    }

    public HistoriqueTarif tarif(Tarif tarif) {
        this.setTarif(tarif);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriqueTarif)) {
            return false;
        }
        return getId() != null && getId().equals(((HistoriqueTarif) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueTarif{" +
            "id=" + getId() +
            ", ancienneValeur=" + getAncienneValeur() +
            ", nouvelleValeur=" + getNouvelleValeur() +
            ", dateModification='" + getDateModification() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", modifieParLogin='" + getModifieParLogin() + "'" +
            "}";
    }
}
