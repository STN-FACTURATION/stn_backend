package sn.stn.facturation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Navire appartenant à un client
 */
@Entity
@Table(name = "navire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Navire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "nom", length = 200, nullable = false)
    private String nom;

    @Size(max = 20)
    @Column(name = "numero_imo", length = 20)
    private String numeroImo;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "jauge_brute", nullable = false)
    private Double jaugeBrute;

    @DecimalMin(value = "0")
    @Column(name = "longueur")
    private Double longueur;

    @DecimalMin(value = "0")
    @Column(name = "largeur")
    private Double largeur;

    @DecimalMin(value = "0")
    @Column(name = "tirant")
    private Double tirant;

    @Size(max = 100)
    @Column(name = "pavillon", length = 100)
    private String pavillon;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "navires", "factures" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Navire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Navire nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumeroImo() {
        return this.numeroImo;
    }

    public Navire numeroImo(String numeroImo) {
        this.setNumeroImo(numeroImo);
        return this;
    }

    public void setNumeroImo(String numeroImo) {
        this.numeroImo = numeroImo;
    }

    public Double getJaugeBrute() {
        return this.jaugeBrute;
    }

    public Navire jaugeBrute(Double jaugeBrute) {
        this.setJaugeBrute(jaugeBrute);
        return this;
    }

    public void setJaugeBrute(Double jaugeBrute) {
        this.jaugeBrute = jaugeBrute;
    }

    public Double getLongueur() {
        return this.longueur;
    }

    public Navire longueur(Double longueur) {
        this.setLongueur(longueur);
        return this;
    }

    public void setLongueur(Double longueur) {
        this.longueur = longueur;
    }

    public Double getLargeur() {
        return this.largeur;
    }

    public Navire largeur(Double largeur) {
        this.setLargeur(largeur);
        return this;
    }

    public void setLargeur(Double largeur) {
        this.largeur = largeur;
    }

    public Double getTirant() {
        return this.tirant;
    }

    public Navire tirant(Double tirant) {
        this.setTirant(tirant);
        return this;
    }

    public void setTirant(Double tirant) {
        this.tirant = tirant;
    }

    public String getPavillon() {
        return this.pavillon;
    }

    public Navire pavillon(String pavillon) {
        this.setPavillon(pavillon);
        return this;
    }

    public void setPavillon(String pavillon) {
        this.pavillon = pavillon;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Navire actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Navire client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Navire)) {
            return false;
        }
        return getId() != null && getId().equals(((Navire) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Navire{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", numeroImo='" + getNumeroImo() + "'" +
            ", jaugeBrute=" + getJaugeBrute() +
            ", longueur=" + getLongueur() +
            ", largeur=" + getLargeur() +
            ", tirant=" + getTirant() +
            ", pavillon='" + getPavillon() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
