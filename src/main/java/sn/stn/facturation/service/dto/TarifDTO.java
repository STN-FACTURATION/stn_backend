package sn.stn.facturation.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.stn.facturation.domain.Tarif} entity.
 */
@Schema(description = "Tranche tarifaire du barème Port de Dakar")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TarifDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private Double trancheMin;

    @DecimalMin(value = "0")
    @Schema(description = "null = illimité (100 000 m³ et au-delà)")
    private Double trancheMax;

    @NotNull
    @DecimalMin(value = "0")
    private Double prixEuro;

    @NotNull
    private Boolean actif;

    @NotNull
    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Size(max = 300)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTrancheMin() {
        return trancheMin;
    }

    public void setTrancheMin(Double trancheMin) {
        this.trancheMin = trancheMin;
    }

    public Double getTrancheMax() {
        return trancheMax;
    }

    public void setTrancheMax(Double trancheMax) {
        this.trancheMax = trancheMax;
    }

    public Double getPrixEuro() {
        return prixEuro;
    }

    public void setPrixEuro(Double prixEuro) {
        this.prixEuro = prixEuro;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TarifDTO)) {
            return false;
        }

        TarifDTO tarifDTO = (TarifDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tarifDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TarifDTO{" +
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
