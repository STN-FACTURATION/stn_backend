package sn.stn.facturation.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link sn.stn.facturation.domain.HistoriqueTarif} entity.
 */
@Schema(description = "Historique des modifications de tarifs")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueTarifDTO implements Serializable {

    private Long id;

    @NotNull
    private Double ancienneValeur;

    @NotNull
    private Double nouvelleValeur;

    @NotNull
    private Instant dateModification;

    @Size(max = 500)
    private String commentaire;

    @Size(max = 50)
    private String modifieParLogin;

    @NotNull
    private TarifDTO tarif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAncienneValeur() {
        return ancienneValeur;
    }

    public void setAncienneValeur(Double ancienneValeur) {
        this.ancienneValeur = ancienneValeur;
    }

    public Double getNouvelleValeur() {
        return nouvelleValeur;
    }

    public void setNouvelleValeur(Double nouvelleValeur) {
        this.nouvelleValeur = nouvelleValeur;
    }

    public Instant getDateModification() {
        return dateModification;
    }

    public void setDateModification(Instant dateModification) {
        this.dateModification = dateModification;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getModifieParLogin() {
        return modifieParLogin;
    }

    public void setModifieParLogin(String modifieParLogin) {
        this.modifieParLogin = modifieParLogin;
    }

    public TarifDTO getTarif() {
        return tarif;
    }

    public void setTarif(TarifDTO tarif) {
        this.tarif = tarif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriqueTarifDTO)) {
            return false;
        }

        HistoriqueTarifDTO historiqueTarifDTO = (HistoriqueTarifDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historiqueTarifDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueTarifDTO{" +
            "id=" + getId() +
            ", ancienneValeur=" + getAncienneValeur() +
            ", nouvelleValeur=" + getNouvelleValeur() +
            ", dateModification='" + getDateModification() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", modifieParLogin='" + getModifieParLogin() + "'" +
            ", tarif=" + getTarif() +
            "}";
    }
}
