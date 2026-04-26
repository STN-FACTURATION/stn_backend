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
import sn.stn.facturation.domain.enumeration.DeviseFacture;
import sn.stn.facturation.domain.enumeration.StatutFacture;

/**
 * Facture principale
 */
@Entity
@Table(name = "facture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "numero", length = 50, nullable = false, unique = true)
    private String numero;

    @NotNull
    @Column(name = "date_emission", nullable = false)
    private LocalDate dateEmission;

    @Column(name = "date_paiement")
    private LocalDate datePaiement;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "volume_m_3", nullable = false)
    private Double volumeM3;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montant_base_ht", nullable = false)
    private Double montantBaseHt;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montant_supplements_ht", nullable = false)
    private Double montantSupplementsHt;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montant_ht", nullable = false)
    private Double montantHt;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "taux_tva", nullable = false)
    private Double tauxTva;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montant_tva", nullable = false)
    private Double montantTva;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montant_ttc", nullable = false)
    private Double montantTtc;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "devise", nullable = false)
    private DeviseFacture devise;

    /**
     * taux EUR→XOF si devise=XOF
     */
    @DecimalMin(value = "0")
    @Column(name = "taux_change_cfa")
    private Double tauxChangeCfa;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutFacture statut;

    @Size(max = 1000)
    @Column(name = "notes", length = 1000)
    private String notes;

    @Size(max = 500)
    @Column(name = "chemin_pdf", length = 500)
    private String cheminPdf;

    /**
     * login de l'utilisateur connecté
     */
    @Size(max = 50)
    @Column(name = "cree_par_login", length = 50)
    private String creeParLogin;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "remorqueur", "facture" }, allowSetters = true)
    private Set<Mouvement> mouvements = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "supplement", "facture" }, allowSetters = true)
    private Set<LigneFactureSupplement> supplements = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Navire navire;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "factures" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Facture id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Facture numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDateEmission() {
        return this.dateEmission;
    }

    public Facture dateEmission(LocalDate dateEmission) {
        this.setDateEmission(dateEmission);
        return this;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public LocalDate getDatePaiement() {
        return this.datePaiement;
    }

    public Facture datePaiement(LocalDate datePaiement) {
        this.setDatePaiement(datePaiement);
        return this;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public Double getVolumeM3() {
        return this.volumeM3;
    }

    public Facture volumeM3(Double volumeM3) {
        this.setVolumeM3(volumeM3);
        return this;
    }

    public void setVolumeM3(Double volumeM3) {
        this.volumeM3 = volumeM3;
    }

    public Double getMontantBaseHt() {
        return this.montantBaseHt;
    }

    public Facture montantBaseHt(Double montantBaseHt) {
        this.setMontantBaseHt(montantBaseHt);
        return this;
    }

    public void setMontantBaseHt(Double montantBaseHt) {
        this.montantBaseHt = montantBaseHt;
    }

    public Double getMontantSupplementsHt() {
        return this.montantSupplementsHt;
    }

    public Facture montantSupplementsHt(Double montantSupplementsHt) {
        this.setMontantSupplementsHt(montantSupplementsHt);
        return this;
    }

    public void setMontantSupplementsHt(Double montantSupplementsHt) {
        this.montantSupplementsHt = montantSupplementsHt;
    }

    public Double getMontantHt() {
        return this.montantHt;
    }

    public Facture montantHt(Double montantHt) {
        this.setMontantHt(montantHt);
        return this;
    }

    public void setMontantHt(Double montantHt) {
        this.montantHt = montantHt;
    }

    public Double getTauxTva() {
        return this.tauxTva;
    }

    public Facture tauxTva(Double tauxTva) {
        this.setTauxTva(tauxTva);
        return this;
    }

    public void setTauxTva(Double tauxTva) {
        this.tauxTva = tauxTva;
    }

    public Double getMontantTva() {
        return this.montantTva;
    }

    public Facture montantTva(Double montantTva) {
        this.setMontantTva(montantTva);
        return this;
    }

    public void setMontantTva(Double montantTva) {
        this.montantTva = montantTva;
    }

    public Double getMontantTtc() {
        return this.montantTtc;
    }

    public Facture montantTtc(Double montantTtc) {
        this.setMontantTtc(montantTtc);
        return this;
    }

    public void setMontantTtc(Double montantTtc) {
        this.montantTtc = montantTtc;
    }

    public DeviseFacture getDevise() {
        return this.devise;
    }

    public Facture devise(DeviseFacture devise) {
        this.setDevise(devise);
        return this;
    }

    public void setDevise(DeviseFacture devise) {
        this.devise = devise;
    }

    public Double getTauxChangeCfa() {
        return this.tauxChangeCfa;
    }

    public Facture tauxChangeCfa(Double tauxChangeCfa) {
        this.setTauxChangeCfa(tauxChangeCfa);
        return this;
    }

    public void setTauxChangeCfa(Double tauxChangeCfa) {
        this.tauxChangeCfa = tauxChangeCfa;
    }

    public StatutFacture getStatut() {
        return this.statut;
    }

    public Facture statut(StatutFacture statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutFacture statut) {
        this.statut = statut;
    }

    public String getNotes() {
        return this.notes;
    }

    public Facture notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCheminPdf() {
        return this.cheminPdf;
    }

    public Facture cheminPdf(String cheminPdf) {
        this.setCheminPdf(cheminPdf);
        return this;
    }

    public void setCheminPdf(String cheminPdf) {
        this.cheminPdf = cheminPdf;
    }

    public String getCreeParLogin() {
        return this.creeParLogin;
    }

    public Facture creeParLogin(String creeParLogin) {
        this.setCreeParLogin(creeParLogin);
        return this;
    }

    public void setCreeParLogin(String creeParLogin) {
        this.creeParLogin = creeParLogin;
    }

    public Set<Mouvement> getMouvements() {
        return this.mouvements;
    }

    public void setMouvements(Set<Mouvement> mouvements) {
        if (this.mouvements != null) {
            this.mouvements.forEach(i -> i.setFacture(null));
        }
        if (mouvements != null) {
            mouvements.forEach(i -> i.setFacture(this));
        }
        this.mouvements = mouvements;
    }

    public Facture mouvements(Set<Mouvement> mouvements) {
        this.setMouvements(mouvements);
        return this;
    }

    public Facture addMouvements(Mouvement mouvement) {
        this.mouvements.add(mouvement);
        mouvement.setFacture(this);
        return this;
    }

    public Facture removeMouvements(Mouvement mouvement) {
        this.mouvements.remove(mouvement);
        mouvement.setFacture(null);
        return this;
    }

    public Set<LigneFactureSupplement> getSupplements() {
        return this.supplements;
    }

    public void setSupplements(Set<LigneFactureSupplement> ligneFactureSupplements) {
        if (this.supplements != null) {
            this.supplements.forEach(i -> i.setFacture(null));
        }
        if (ligneFactureSupplements != null) {
            ligneFactureSupplements.forEach(i -> i.setFacture(this));
        }
        this.supplements = ligneFactureSupplements;
    }

    public Facture supplements(Set<LigneFactureSupplement> ligneFactureSupplements) {
        this.setSupplements(ligneFactureSupplements);
        return this;
    }

    public Facture addSupplements(LigneFactureSupplement ligneFactureSupplement) {
        this.supplements.add(ligneFactureSupplement);
        ligneFactureSupplement.setFacture(this);
        return this;
    }

    public Facture removeSupplements(LigneFactureSupplement ligneFactureSupplement) {
        this.supplements.remove(ligneFactureSupplement);
        ligneFactureSupplement.setFacture(null);
        return this;
    }

    public Navire getNavire() {
        return this.navire;
    }

    public void setNavire(Navire navire) {
        this.navire = navire;
    }

    public Facture navire(Navire navire) {
        this.setNavire(navire);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Facture client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facture)) {
            return false;
        }
        return getId() != null && getId().equals(((Facture) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facture{" +
                "id=" + getId() +
                ", numero='" + getNumero() + "'" +
                ", dateEmission='" + getDateEmission() + "'" +
                ", datePaiement='" + getDatePaiement() + "'" +
                ", volumeM3=" + getVolumeM3() +
                ", montantBaseHt=" + getMontantBaseHt() +
                ", montantSupplementsHt=" + getMontantSupplementsHt() +
                ", montantHt=" + getMontantHt() +
                ", tauxTva=" + getTauxTva() +
                ", montantTva=" + getMontantTva() +
                ", montantTtc=" + getMontantTtc() +
                ", devise='" + getDevise() + "'" +
                ", tauxChangeCfa=" + getTauxChangeCfa() +
                ", statut='" + getStatut() + "'" +
                ", notes='" + getNotes() + "'" +
                ", cheminPdf='" + getCheminPdf() + "'" +
                ", creeParLogin='" + getCreeParLogin() + "'" +
                "}";
    }
}
