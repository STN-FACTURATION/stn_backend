package sn.stn.facturation.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import sn.stn.facturation.domain.enumeration.TypeOperation;

/**
 * A DTO for the {@link sn.stn.facturation.domain.Mouvement} entity.
 */
@Schema(description = "Ligne d'opération / mouvement de remorquage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MouvementDTO implements Serializable {

    private Long id;

    @NotNull
    private TypeOperation type;

    @Size(max = 100)
    private String posteA;

    @Size(max = 100)
    private String posteB;

    @DecimalMin(value = "0")
    private Double duree;

    @NotNull
    @DecimalMin(value = "0")
    private Double montantCalcule;

    @Size(max = 500)
    private String libelle;

    private RemorqueurDTO remorqueur;

    @NotNull
    private FactureDTO facture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeOperation getType() {
        return type;
    }

    public void setType(TypeOperation type) {
        this.type = type;
    }

    public String getPosteA() {
        return posteA;
    }

    public void setPosteA(String posteA) {
        this.posteA = posteA;
    }

    public String getPosteB() {
        return posteB;
    }

    public void setPosteB(String posteB) {
        this.posteB = posteB;
    }

    public Double getDuree() {
        return duree;
    }

    public void setDuree(Double duree) {
        this.duree = duree;
    }

    public Double getMontantCalcule() {
        return montantCalcule;
    }

    public void setMontantCalcule(Double montantCalcule) {
        this.montantCalcule = montantCalcule;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public RemorqueurDTO getRemorqueur() {
        return remorqueur;
    }

    public void setRemorqueur(RemorqueurDTO remorqueur) {
        this.remorqueur = remorqueur;
    }

    public FactureDTO getFacture() {
        return facture;
    }

    public void setFacture(FactureDTO facture) {
        this.facture = facture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MouvementDTO)) {
            return false;
        }

        MouvementDTO mouvementDTO = (MouvementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mouvementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MouvementDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", posteA='" + getPosteA() + "'" +
            ", posteB='" + getPosteB() + "'" +
            ", duree=" + getDuree() +
            ", montantCalcule=" + getMontantCalcule() +
            ", libelle='" + getLibelle() + "'" +
            ", remorqueur=" + getRemorqueur() +
            ", facture=" + getFacture() +
            "}";
    }
}
