package sn.stn.facturation.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.stn.facturation.domain.Remorqueur} entity.
 */
@Schema(description = "Remorqueur STN")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RemorqueurDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String code;

    @NotNull
    @Size(max = 100)
    private String nom;

    @Size(max = 50)
    private String statut;

    @Size(max = 500)
    private String observation;

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RemorqueurDTO)) {
            return false;
        }

        RemorqueurDTO remorqueurDTO = (RemorqueurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, remorqueurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RemorqueurDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", statut='" + getStatut() + "'" +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
