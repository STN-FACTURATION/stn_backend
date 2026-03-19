package sn.stn.facturation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Tranche tarifaire du barème Port de Dakar
 */
@Entity
@Table(name = "tarif")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tarif implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "tranche_min", nullable = false)
    private Double trancheMin;

    /**
     * null = illimité (100 000 m³ et au-delà)
     */
    @DecimalMin(value = "0")
    @Column(name = "tranche_max")
    private Double trancheMax;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "prix_euro", nullable = false)
    private Double prixEuro;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Size(max = 300)
    @Column(name = "description", length = 300)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tarif")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tarif" }, allowSetters = true)
    private Set<HistoriqueTarif> historiques = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tarif id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTrancheMin() {
        return this.trancheMin;
    }

    public Tarif trancheMin(Double trancheMin) {
        this.setTrancheMin(trancheMin);
        return this;
    }

    public void setTrancheMin(Double trancheMin) {
        this.trancheMin = trancheMin;
    }

    public Double getTrancheMax() {
        return this.trancheMax;
    }

    public Tarif trancheMax(Double trancheMax) {
        this.setTrancheMax(trancheMax);
        return this;
    }

    public void setTrancheMax(Double trancheMax) {
        this.trancheMax = trancheMax;
    }

    public Double getPrixEuro() {
        return this.prixEuro;
    }

    public Tarif prixEuro(Double prixEuro) {
        this.setPrixEuro(prixEuro);
        return this;
    }

    public void setPrixEuro(Double prixEuro) {
        this.prixEuro = prixEuro;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Tarif actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Tarif dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Tarif dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getDescription() {
        return this.description;
    }

    public Tarif description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<HistoriqueTarif> getHistoriques() {
        return this.historiques;
    }

    public void setHistoriques(Set<HistoriqueTarif> historiqueTarifs) {
        if (this.historiques != null) {
            this.historiques.forEach(i -> i.setTarif(null));
        }
        if (historiqueTarifs != null) {
            historiqueTarifs.forEach(i -> i.setTarif(this));
        }
        this.historiques = historiqueTarifs;
    }

    public Tarif historiques(Set<HistoriqueTarif> historiqueTarifs) {
        this.setHistoriques(historiqueTarifs);
        return this;
    }

    public Tarif addHistoriques(HistoriqueTarif historiqueTarif) {
        this.historiques.add(historiqueTarif);
        historiqueTarif.setTarif(this);
        return this;
    }

    public Tarif removeHistoriques(HistoriqueTarif historiqueTarif) {
        this.historiques.remove(historiqueTarif);
        historiqueTarif.setTarif(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tarif)) {
            return false;
        }
        return getId() != null && getId().equals(((Tarif) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tarif{" +
            "id=" + getId() +
            ", trancheMin=" + getTrancheMin() +
            ", trancheMax=" + getTrancheMax() +
            ", prixEuro=" + getPrixEuro() +
            ", actif='" + getActif() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
