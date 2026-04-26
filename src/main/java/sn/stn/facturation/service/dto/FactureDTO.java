package sn.stn.facturation.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import sn.stn.facturation.domain.enumeration.DeviseFacture;
import sn.stn.facturation.domain.enumeration.StatutFacture;

/**
 * A DTO for the {@link sn.stn.facturation.domain.Facture} entity.
 */
@Schema(description = "Facture principale")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactureDTO implements Serializable {

    private Long id;

    @Size(max = 50)
    private String numero;

    @NotNull
    private Instant dateEmission;

    private LocalDate datePaiement;

    @NotNull
    @DecimalMin(value = "0")
    private Double volumeM3;

    @NotNull
    @DecimalMin(value = "0")
    private Double montantBaseHt;

    @NotNull
    @DecimalMin(value = "0")
    private Double montantSupplementsHt;

    @NotNull
    @DecimalMin(value = "0")
    private Double montantHt;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private Double tauxTva;

    @NotNull
    @DecimalMin(value = "0")
    private Double montantTva;

    @NotNull
    @DecimalMin(value = "0")
    private Double montantTtc;

    @NotNull
    private DeviseFacture devise;

    @DecimalMin(value = "0")
    @Schema(description = "taux EUR→XOF si devise=XOF")
    private Double tauxChangeCfa;

    @NotNull
    private StatutFacture statut;

    @Size(max = 1000)
    private String notes;

    @Size(max = 500)
    private String cheminPdf;

    @Size(max = 50)
    @Schema(description = "login de l'utilisateur connecté")
    private String creeParLogin;

    private NavireDTO navire;

    private ClientDTO client;

    private Set<MouvementDTO> mouvements;

    private Set<LigneFactureSupplementDTO> supplements;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Instant getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(Instant dateEmission) {
        this.dateEmission = dateEmission;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public Double getVolumeM3() {
        return volumeM3;
    }

    public void setVolumeM3(Double volumeM3) {
        this.volumeM3 = volumeM3;
    }

    public Double getMontantBaseHt() {
        return montantBaseHt;
    }

    public void setMontantBaseHt(Double montantBaseHt) {
        this.montantBaseHt = montantBaseHt;
    }

    public Double getMontantSupplementsHt() {
        return montantSupplementsHt;
    }

    public void setMontantSupplementsHt(Double montantSupplementsHt) {
        this.montantSupplementsHt = montantSupplementsHt;
    }

    public Double getMontantHt() {
        return montantHt;
    }

    public void setMontantHt(Double montantHt) {
        this.montantHt = montantHt;
    }

    public Double getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(Double tauxTva) {
        this.tauxTva = tauxTva;
    }

    public Double getMontantTva() {
        return montantTva;
    }

    public void setMontantTva(Double montantTva) {
        this.montantTva = montantTva;
    }

    public Double getMontantTtc() {
        return montantTtc;
    }

    public void setMontantTtc(Double montantTtc) {
        this.montantTtc = montantTtc;
    }

    public DeviseFacture getDevise() {
        return devise;
    }

    public void setDevise(DeviseFacture devise) {
        this.devise = devise;
    }

    public Double getTauxChangeCfa() {
        return tauxChangeCfa;
    }

    public void setTauxChangeCfa(Double tauxChangeCfa) {
        this.tauxChangeCfa = tauxChangeCfa;
    }

    public StatutFacture getStatut() {
        return statut;
    }

    public void setStatut(StatutFacture statut) {
        this.statut = statut;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCheminPdf() {
        return cheminPdf;
    }

    public void setCheminPdf(String cheminPdf) {
        this.cheminPdf = cheminPdf;
    }

    public String getCreeParLogin() {
        return creeParLogin;
    }

    public void setCreeParLogin(String creeParLogin) {
        this.creeParLogin = creeParLogin;
    }

    public NavireDTO getNavire() {
        return navire;
    }

    public void setNavire(NavireDTO navire) {
        this.navire = navire;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public Set<MouvementDTO> getMouvements() {
        return mouvements;
    }

    public void setMouvements(Set<MouvementDTO> mouvements) {
        this.mouvements = mouvements;
    }

    public Set<LigneFactureSupplementDTO> getSupplements() {
        return supplements;
    }

    public void setSupplements(Set<LigneFactureSupplementDTO> supplements) {
        this.supplements = supplements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactureDTO)) {
            return false;
        }

        FactureDTO factureDTO = (FactureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, factureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactureDTO{" +
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
                ", navire=" + getNavire() +
                ", client=" + getClient() +
                ", mouvements=" + getMouvements() +
                ", supplements=" + getSupplements() +
                "}";
    }
}
