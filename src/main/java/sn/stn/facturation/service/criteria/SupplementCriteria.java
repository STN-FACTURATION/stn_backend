package sn.stn.facturation.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import sn.stn.facturation.domain.enumeration.TypeSupplement;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.stn.facturation.domain.Supplement} entity. This class is used
 * in {@link sn.stn.facturation.web.rest.SupplementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /supplements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplementCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeSupplement
     */
    public static class TypeSupplementFilter extends Filter<TypeSupplement> {

        public TypeSupplementFilter() {}

        public TypeSupplementFilter(TypeSupplementFilter filter) {
            super(filter);
        }

        @Override
        public TypeSupplementFilter copy() {
            return new TypeSupplementFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter libelle;

    private TypeSupplementFilter type;

    private DoubleFilter tauxPourcentage;

    private DoubleFilter montantFixe;

    private BooleanFilter actif;

    private Boolean distinct;

    public SupplementCriteria() {}

    public SupplementCriteria(SupplementCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.libelle = other.optionalLibelle().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(TypeSupplementFilter::copy).orElse(null);
        this.tauxPourcentage = other.optionalTauxPourcentage().map(DoubleFilter::copy).orElse(null);
        this.montantFixe = other.optionalMontantFixe().map(DoubleFilter::copy).orElse(null);
        this.actif = other.optionalActif().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SupplementCriteria copy() {
        return new SupplementCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getLibelle() {
        return libelle;
    }

    public Optional<StringFilter> optionalLibelle() {
        return Optional.ofNullable(libelle);
    }

    public StringFilter libelle() {
        if (libelle == null) {
            setLibelle(new StringFilter());
        }
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
    }

    public TypeSupplementFilter getType() {
        return type;
    }

    public Optional<TypeSupplementFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public TypeSupplementFilter type() {
        if (type == null) {
            setType(new TypeSupplementFilter());
        }
        return type;
    }

    public void setType(TypeSupplementFilter type) {
        this.type = type;
    }

    public DoubleFilter getTauxPourcentage() {
        return tauxPourcentage;
    }

    public Optional<DoubleFilter> optionalTauxPourcentage() {
        return Optional.ofNullable(tauxPourcentage);
    }

    public DoubleFilter tauxPourcentage() {
        if (tauxPourcentage == null) {
            setTauxPourcentage(new DoubleFilter());
        }
        return tauxPourcentage;
    }

    public void setTauxPourcentage(DoubleFilter tauxPourcentage) {
        this.tauxPourcentage = tauxPourcentage;
    }

    public DoubleFilter getMontantFixe() {
        return montantFixe;
    }

    public Optional<DoubleFilter> optionalMontantFixe() {
        return Optional.ofNullable(montantFixe);
    }

    public DoubleFilter montantFixe() {
        if (montantFixe == null) {
            setMontantFixe(new DoubleFilter());
        }
        return montantFixe;
    }

    public void setMontantFixe(DoubleFilter montantFixe) {
        this.montantFixe = montantFixe;
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
        final SupplementCriteria that = (SupplementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(type, that.type) &&
            Objects.equals(tauxPourcentage, that.tauxPourcentage) &&
            Objects.equals(montantFixe, that.montantFixe) &&
            Objects.equals(actif, that.actif) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, libelle, type, tauxPourcentage, montantFixe, actif, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplementCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalLibelle().map(f -> "libelle=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalTauxPourcentage().map(f -> "tauxPourcentage=" + f + ", ").orElse("") +
            optionalMontantFixe().map(f -> "montantFixe=" + f + ", ").orElse("") +
            optionalActif().map(f -> "actif=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
