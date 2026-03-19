package sn.stn.facturation.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import sn.stn.facturation.domain.enumeration.TypeSupplement;

/**
 * A DTO for the {@link sn.stn.facturation.domain.Supplement} entity.
 */
@Schema(description = "Supplément tarifaire (S1 à S10 + Veille Sécurité)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplementDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String code;

    @NotNull
    @Size(max = 300)
    private String libelle;

    @NotNull
    private TypeSupplement type;

    @DecimalMin(value = "0")
    @DecimalMax(value = "200")
    @Schema(description = "ex: 50.0 pour +50%")
    private Double tauxPourcentage;

    @DecimalMin(value = "0")
    @Schema(description = "ex: 146.0 € pour S9")
    private Double montantFixe;

    @NotNull
    private Boolean actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public TypeSupplement getType() {
        return type;
    }

    public void setType(TypeSupplement type) {
        this.type = type;
    }

    public Double getTauxPourcentage() {
        return tauxPourcentage;
    }

    public void setTauxPourcentage(Double tauxPourcentage) {
        this.tauxPourcentage = tauxPourcentage;
    }

    public Double getMontantFixe() {
        return montantFixe;
    }

    public void setMontantFixe(Double montantFixe) {
        this.montantFixe = montantFixe;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupplementDTO)) {
            return false;
        }

        SupplementDTO supplementDTO = (SupplementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, supplementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplementDTO{" +
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
