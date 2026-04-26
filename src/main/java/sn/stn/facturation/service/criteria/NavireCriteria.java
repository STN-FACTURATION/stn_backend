package sn.stn.facturation.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.stn.facturation.domain.Navire} entity. This class is used
 * in {@link sn.stn.facturation.web.rest.NavireResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /navires?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NavireCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter numeroImo;

    private DoubleFilter jaugeBrute;

    private DoubleFilter longueur;

    private DoubleFilter largeur;

    private DoubleFilter tirant;

    private StringFilter pavillon;

    private BooleanFilter actif;

    private Boolean distinct;

    public NavireCriteria() {}

    public NavireCriteria(NavireCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.numeroImo = other.optionalNumeroImo().map(StringFilter::copy).orElse(null);
        this.jaugeBrute = other.optionalJaugeBrute().map(DoubleFilter::copy).orElse(null);
        this.longueur = other.optionalLongueur().map(DoubleFilter::copy).orElse(null);
        this.largeur = other.optionalLargeur().map(DoubleFilter::copy).orElse(null);
        this.tirant = other.optionalTirant().map(DoubleFilter::copy).orElse(null);
        this.pavillon = other.optionalPavillon().map(StringFilter::copy).orElse(null);
        this.actif = other.optionalActif().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NavireCriteria copy() {
        return new NavireCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNom() {
        return nom;
    }

    public Optional<StringFilter> optionalNom() {
        return Optional.ofNullable(nom);
    }

    public StringFilter nom() {
        if (nom == null) {
            setNom(new StringFilter());
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getNumeroImo() {
        return numeroImo;
    }

    public Optional<StringFilter> optionalNumeroImo() {
        return Optional.ofNullable(numeroImo);
    }

    public StringFilter numeroImo() {
        if (numeroImo == null) {
            setNumeroImo(new StringFilter());
        }
        return numeroImo;
    }

    public void setNumeroImo(StringFilter numeroImo) {
        this.numeroImo = numeroImo;
    }

    public DoubleFilter getJaugeBrute() {
        return jaugeBrute;
    }

    public Optional<DoubleFilter> optionalJaugeBrute() {
        return Optional.ofNullable(jaugeBrute);
    }

    public DoubleFilter jaugeBrute() {
        if (jaugeBrute == null) {
            setJaugeBrute(new DoubleFilter());
        }
        return jaugeBrute;
    }

    public void setJaugeBrute(DoubleFilter jaugeBrute) {
        this.jaugeBrute = jaugeBrute;
    }

    public DoubleFilter getLongueur() {
        return longueur;
    }

    public Optional<DoubleFilter> optionalLongueur() {
        return Optional.ofNullable(longueur);
    }

    public DoubleFilter longueur() {
        if (longueur == null) {
            setLongueur(new DoubleFilter());
        }
        return longueur;
    }

    public void setLongueur(DoubleFilter longueur) {
        this.longueur = longueur;
    }

    public DoubleFilter getLargeur() {
        return largeur;
    }

    public Optional<DoubleFilter> optionalLargeur() {
        return Optional.ofNullable(largeur);
    }

    public DoubleFilter largeur() {
        if (largeur == null) {
            setLargeur(new DoubleFilter());
        }
        return largeur;
    }

    public void setLargeur(DoubleFilter largeur) {
        this.largeur = largeur;
    }

    public DoubleFilter getTirant() {
        return tirant;
    }

    public Optional<DoubleFilter> optionalTirant() {
        return Optional.ofNullable(tirant);
    }

    public DoubleFilter tirant() {
        if (tirant == null) {
            setTirant(new DoubleFilter());
        }
        return tirant;
    }

    public void setTirant(DoubleFilter tirant) {
        this.tirant = tirant;
    }

    public StringFilter getPavillon() {
        return pavillon;
    }

    public Optional<StringFilter> optionalPavillon() {
        return Optional.ofNullable(pavillon);
    }

    public StringFilter pavillon() {
        if (pavillon == null) {
            setPavillon(new StringFilter());
        }
        return pavillon;
    }

    public void setPavillon(StringFilter pavillon) {
        this.pavillon = pavillon;
    }

    public BooleanFilter getActif() {
        return actif;
    }

    public Optional<BooleanFilter> optionalActif() {
        return Optional.ofNullable(actif);
    }

    public BooleanFilter actif() {
        if (actif == null) {
            setActif(new BooleanFilter());
        }
        return actif;
    }

    public void setActif(BooleanFilter actif) {
        this.actif = actif;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NavireCriteria that = (NavireCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(numeroImo, that.numeroImo) &&
            Objects.equals(jaugeBrute, that.jaugeBrute) &&
            Objects.equals(longueur, that.longueur) &&
            Objects.equals(largeur, that.largeur) &&
            Objects.equals(tirant, that.tirant) &&
            Objects.equals(pavillon, that.pavillon) &&
            Objects.equals(actif, that.actif) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, numeroImo, jaugeBrute, longueur, largeur, tirant, pavillon, actif, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NavireCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalNumeroImo().map(f -> "numeroImo=" + f + ", ").orElse("") +
            optionalJaugeBrute().map(f -> "jaugeBrute=" + f + ", ").orElse("") +
            optionalLongueur().map(f -> "longueur=" + f + ", ").orElse("") +
            optionalLargeur().map(f -> "largeur=" + f + ", ").orElse("") +
            optionalTirant().map(f -> "tirant=" + f + ", ").orElse("") +
            optionalPavillon().map(f -> "pavillon=" + f + ", ").orElse("") +
            optionalActif().map(f -> "actif=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
