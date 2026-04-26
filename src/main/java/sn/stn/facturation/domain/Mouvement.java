package sn.stn.facturation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.stn.facturation.domain.enumeration.TypeOperation;

/**
 * Ligne d'opération / mouvement de remorquage
 */
@Entity
@Table(name = "mouvement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mouvement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeOperation type;

    @Size(max = 100)
    @Column(name = "poste_a", length = 100)
    private String posteA;

    @Size(max = 100)
    @Column(name = "poste_b", length = 100)
    private String posteB;

    @DecimalMin(value = "0")
    @Column(name = "duree")
    private Double duree;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montant_calcule", nullable = false)
    private Double montantCalcule;

    @Size(max = 500)
    @Column(name = "libelle", length = 500)
    private String libelle;

    @ManyToOne(fetch = FetchType.LAZY)
    private Remorqueur remorqueur;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "mouvements", "supplements", "navire", "client" }, allowSetters = true)
    private Facture facture;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mouvement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeOperation getType() {
        return this.type;
    }

    public Mouvement type(TypeOperation type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeOperation type) {
        this.type = type;
    }

    public String getPosteA() {
        return this.posteA;
    }

    public Mouvement posteA(String posteA) {
        this.setPosteA(posteA);
        return this;
    }

    public void setPosteA(String posteA) {
        this.posteA = posteA;
    }

    public String getPosteB() {
        return this.posteB;
    }

    public Mouvement posteB(String posteB) {
        this.setPosteB(posteB);
        return this;
    }

    public void setPosteB(String posteB) {
        this.posteB = posteB;
    }

    public Double getDuree() {
        return this.duree;
    }

    public Mouvement duree(Double duree) {
        this.setDuree(duree);
        return this;
    }

    public void setDuree(Double duree) {
        this.duree = duree;
    }

    public Double getMontantCalcule() {
        return this.montantCalcule;
    }

    public Mouvement montantCalcule(Double montantCalcule) {
        this.setMontantCalcule(montantCalcule);
        return this;
    }

    public void setMontantCalcule(Double montantCalcule) {
        this.montantCalcule = montantCalcule;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Mouvement libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Remorqueur getRemorqueur() {
        return this.remorqueur;
    }

    public void setRemorqueur(Remorqueur remorqueur) {
        this.remorqueur = remorqueur;
    }

    public Mouvement remorqueur(Remorqueur remorqueur) {
        this.setRemorqueur(remorqueur);
        return this;
    }

    public Facture getFacture() {
        return this.facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public Mouvement facture(Facture facture) {
        this.setFacture(facture);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mouvement)) {
            return false;
        }
        return getId() != null && getId().equals(((Mouvement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mouvement{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", posteA='" + getPosteA() + "'" +
            ", posteB='" + getPosteB() + "'" +
            ", duree=" + getDuree() +
            ", montantCalcule=" + getMontantCalcule() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
