package sn.stn.facturation.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.stn.facturation.domain.enumeration.TypeSupplement;

/**
 * Supplément tarifaire (S1 à S10 + Veille Sécurité)
 */
@Entity
@Table(name = "supplement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Supplement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "code", length = 10, nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(max = 300)
    @Column(name = "libelle", length = 300, nullable = false)
    private String libelle;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeSupplement type;

    /**
     * ex: 50.0 pour +50%
     */
    @DecimalMin(value = "0")
    @DecimalMax(value = "200")
    @Column(name = "taux_pourcentage")
    private Double tauxPourcentage;

    /**
     * ex: 146.0 € pour S9
     */
    @DecimalMin(value = "0")
    @Column(name = "montant_fixe")
    private Double montantFixe;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Supplement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Supplement code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Supplement libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public TypeSupplement getType() {
        return this.type;
    }

    public Supplement type(TypeSupplement type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeSupplement type) {
        this.type = type;
    }

    public Double getTauxPourcentage() {
        return this.tauxPourcentage;
    }

    public Supplement tauxPourcentage(Double tauxPourcentage) {
        this.setTauxPourcentage(tauxPourcentage);
        return this;
    }

    public void setTauxPourcentage(Double tauxPourcentage) {
        this.tauxPourcentage = tauxPourcentage;
    }

    public Double getMontantFixe() {
        return this.montantFixe;
    }

    public Supplement montantFixe(Double montantFixe) {
        this.setMontantFixe(montantFixe);
        return this;
    }

    public void setMontantFixe(Double montantFixe) {
        this.montantFixe = montantFixe;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Supplement actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Supplement)) {
            return false;
        }
        return getId() != null && getId().equals(((Supplement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Supplement{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", type='" + getType() + "'" +
            ", tauxPourcentage=" + getTauxPourcentage() +
            ", montantFixe=" + getMontantFixe() +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
