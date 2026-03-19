package sn.stn.facturation.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.stn.facturation.domain.LigneFactureSupplement} entity.
 */
@Schema(description = "Ligne de supplément appliqué à une facture")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LigneFactureSupplementDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private Double montantCalcule;

    @Size(max = 300)
    private String description;

    @NotNull
    private SupplementDTO supplement;

    @NotNull
    private FactureDTO facture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontantCalcule() {
        return montantCalcule;
    }

    public void setMontantCalcule(Double montantCalcule) {
        this.montantCalcule = montantCalcule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SupplementDTO getSupplement() {
        return supplement;
    }

    public void setSupplement(SupplementDTO supplement) {
        this.supplement = supplement;
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
        if (!(o instanceof LigneFactureSupplementDTO)) {
            return false;
        }

        LigneFactureSupplementDTO ligneFactureSupplementDTO = (LigneFactureSupplementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ligneFactureSupplementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneFactureSupplementDTO{" +
            "id=" + getId() +
            ", montantCalcule=" + getMontantCalcule() +
            ", description='" + getDescription() + "'" +
            ", supplement=" + getSupplement() +
            ", facture=" + getFacture() +
            "}";
    }
}
