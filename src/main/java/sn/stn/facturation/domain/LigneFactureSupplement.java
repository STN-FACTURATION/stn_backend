package sn.stn.facturation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Ligne de supplément appliqué à une facture
 */
@Entity
@Table(name = "ligne_facture_supplement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LigneFactureSupplement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montant_calcule", nullable = false)
    private Double montantCalcule;

    @Size(max = 300)
    @Column(name = "description", length = 300)
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    private Supplement supplement;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "mouvements", "supplements", "navire", "client" }, allowSetters = true)
    private Facture facture;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LigneFactureSupplement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontantCalcule() {
        return this.montantCalcule;
    }

    public LigneFactureSupplement montantCalcule(Double montantCalcule) {
        this.setMontantCalcule(montantCalcule);
        return this;
    }

    public void setMontantCalcule(Double montantCalcule) {
        this.montantCalcule = montantCalcule;
    }

    public String getDescription() {
        return this.description;
    }

    public LigneFactureSupplement description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Supplement getSupplement() {
        return this.supplement;
    }

    public void setSupplement(Supplement supplement) {
        this.supplement = supplement;
    }

    public LigneFactureSupplement supplement(Supplement supplement) {
        this.setSupplement(supplement);
        return this;
    }

    public Facture getFacture() {
        return this.facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public LigneFactureSupplement facture(Facture facture) {
        this.setFacture(facture);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigneFactureSupplement)) {
            return false;
        }
        return getId() != null && getId().equals(((LigneFactureSupplement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneFactureSupplement{" +
            "id=" + getId() +
            ", montantCalcule=" + getMontantCalcule() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
