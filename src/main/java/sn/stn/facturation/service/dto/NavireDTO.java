package sn.stn.facturation.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.stn.facturation.domain.Navire} entity.
 */
@Schema(description = "Navire appartenant à un client")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NavireDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String nom;

    @Size(max = 20)
    private String numeroImo;

    @NotNull
    @DecimalMin(value = "0")
    private Double jaugeBrute;

    @DecimalMin(value = "0")
    private Double longueur;

    @DecimalMin(value = "0")
    private Double largeur;

    @DecimalMin(value = "0")
    private Double tirant;

    @Size(max = 100)
    private String pavillon;

    @NotNull
    private Boolean actif;

    @NotNull
    private ClientDTO client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumeroImo() {
        return numeroImo;
    }

    public void setNumeroImo(String numeroImo) {
        this.numeroImo = numeroImo;
    }

    public Double getJaugeBrute() {
        return jaugeBrute;
    }

    public void setJaugeBrute(Double jaugeBrute) {
        this.jaugeBrute = jaugeBrute;
    }

    public Double getLongueur() {
        return longueur;
    }

    public void setLongueur(Double longueur) {
        this.longueur = longueur;
    }

    public Double getLargeur() {
        return largeur;
    }

    public void setLargeur(Double largeur) {
        this.largeur = largeur;
    }

    public Double getTirant() {
        return tirant;
    }

    public void setTirant(Double tirant) {
        this.tirant = tirant;
    }

    public String getPavillon() {
        return pavillon;
    }

    public void setPavillon(String pavillon) {
        this.pavillon = pavillon;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NavireDTO)) {
            return false;
        }

        NavireDTO navireDTO = (NavireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, navireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NavireDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", numeroImo='" + getNumeroImo() + "'" +
            ", jaugeBrute=" + getJaugeBrute() +
            ", longueur=" + getLongueur() +
            ", largeur=" + getLargeur() +
            ", tirant=" + getTirant() +
            ", pavillon='" + getPavillon() + "'" +
            ", actif='" + getActif() + "'" +
            ", client=" + getClient() +
            "}";
    }
}
