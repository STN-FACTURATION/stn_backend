package sn.stn.facturation.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import sn.stn.facturation.domain.enumeration.TypeOperation;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.stn.facturation.domain.Mouvement} entity. This class is used
 * in {@link sn.stn.facturation.web.rest.MouvementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mouvements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MouvementCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeOperation
     */
    public static class TypeOperationFilter extends Filter<TypeOperation> {

        public TypeOperationFilter() {}

        public TypeOperationFilter(TypeOperationFilter filter) {
            super(filter);
        }

        @Override
        public TypeOperationFilter copy() {
            return new TypeOperationFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TypeOperationFilter type;

    private StringFilter posteA;

    private StringFilter posteB;

    private DoubleFilter duree;

    private DoubleFilter montantCalcule;

    private StringFilter libelle;

    private LongFilter remorqueurId;

    private LongFilter factureId;

    private Boolean distinct;

    public MouvementCriteria() {}

    public MouvementCriteria(MouvementCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.type = other.optionalType().map(TypeOperationFilter::copy).orElse(null);
        this.posteA = other.optionalPosteA().map(StringFilter::copy).orElse(null);
        this.posteB = other.optionalPosteB().map(StringFilter::copy).orElse(null);
        this.duree = other.optionalDuree().map(DoubleFilter::copy).orElse(null);
        this.montantCalcule = other.optionalMontantCalcule().map(DoubleFilter::copy).orElse(null);
        this.libelle = other.optionalLibelle().map(StringFilter::copy).orElse(null);
        this.remorqueurId = other.optionalRemorqueurId().map(LongFilter::copy).orElse(null);
        this.factureId = other.optionalFactureId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MouvementCriteria copy() {
        return new MouvementCriteria(this);
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

    public TypeOperationFilter getType() {
        return type;
    }

    public Optional<TypeOperationFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public TypeOperationFilter type() {
        if (type == null) {
            setType(new TypeOperationFilter());
        }
        return type;
    }

    public void setType(TypeOperationFilter type) {
        this.type = type;
    }

    public StringFilter getPosteA() {
        return posteA;
    }

    public Optional<StringFilter> optionalPosteA() {
        return Optional.ofNullable(posteA);
    }

    public StringFilter posteA() {
        if (posteA == null) {
            setPosteA(new StringFilter());
        }
        return posteA;
    }

    public void setPosteA(StringFilter posteA) {
        this.posteA = posteA;
    }

    public StringFilter getPosteB() {
        return posteB;
    }

    public Optional<StringFilter> optionalPosteB() {
        return Optional.ofNullable(posteB);
    }

    public StringFilter posteB() {
        if (posteB == null) {
            setPosteB(new StringFilter());
        }
        return posteB;
    }

    public void setPosteB(StringFilter posteB) {
        this.posteB = posteB;
    }

    public DoubleFilter getDuree() {
        return duree;
    }

    public Optional<DoubleFilter> optionalDuree() {
        return Optional.ofNullable(duree);
    }

    public DoubleFilter duree() {
        if (duree == null) {
            setDuree(new DoubleFilter());
        }
        return duree;
    }

    public void setDuree(DoubleFilter duree) {
        this.duree = duree;
    }

    public DoubleFilter getMontantCalcule() {
        return montantCalcule;
    }

    public Optional<DoubleFilter> optionalMontantCalcule() {
        return Optional.ofNullable(montantCalcule);
    }

    public DoubleFilter montantCalcule() {
        if (montantCalcule == null) {
            setMontantCalcule(new DoubleFilter());
        }
        return montantCalcule;
    }

    public void setMontantCalcule(DoubleFilter montantCalcule) {
        this.montantCalcule = montantCalcule;
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

    public LongFilter getRemorqueurId() {
        return remorqueurId;
    }

    public Optional<LongFilter> optionalRemorqueurId() {
        return Optional.ofNullable(remorqueurId);
    }

    public LongFilter remorqueurId() {
        if (remorqueurId == null) {
            setRemorqueurId(new LongFilter());
        }
        return remorqueurId;
    }

    public void setRemorqueurId(LongFilter remorqueurId) {
        this.remorqueurId = remorqueurId;
    }

    public LongFilter getFactureId() {
        return factureId;
    }

    public Optional<LongFilter> optionalFactureId() {
        return Optional.ofNullable(factureId);
    }

    public LongFilter factureId() {
        if (factureId == null) {
            setFactureId(new LongFilter());
        }
        return factureId;
    }

    public void setFactureId(LongFilter factureId) {
        this.factureId = factureId;
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
        final MouvementCriteria that = (MouvementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(posteA, that.posteA) &&
            Objects.equals(posteB, that.posteB) &&
            Objects.equals(duree, that.duree) &&
            Objects.equals(montantCalcule, that.montantCalcule) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(remorqueurId, that.remorqueurId) &&
            Objects.equals(factureId, that.factureId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, posteA, posteB, duree, montantCalcule, libelle, remorqueurId, factureId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MouvementCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalPosteA().map(f -> "posteA=" + f + ", ").orElse("") +
            optionalPosteB().map(f -> "posteB=" + f + ", ").orElse("") +
            optionalDuree().map(f -> "duree=" + f + ", ").orElse("") +
            optionalMontantCalcule().map(f -> "montantCalcule=" + f + ", ").orElse("") +
            optionalLibelle().map(f -> "libelle=" + f + ", ").orElse("") +
            optionalRemorqueurId().map(f -> "remorqueurId=" + f + ", ").orElse("") +
            optionalFactureId().map(f -> "factureId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
