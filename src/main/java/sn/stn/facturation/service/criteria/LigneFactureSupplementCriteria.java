package sn.stn.facturation.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.stn.facturation.domain.LigneFactureSupplement} entity. This class is used
 * in {@link sn.stn.facturation.web.rest.LigneFactureSupplementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ligne-facture-supplements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LigneFactureSupplementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter montantCalcule;

    private StringFilter description;

    private LongFilter supplementId;

    private LongFilter factureId;

    private Boolean distinct;

    public LigneFactureSupplementCriteria() {}

    public LigneFactureSupplementCriteria(LigneFactureSupplementCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.montantCalcule = other.optionalMontantCalcule().map(DoubleFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.supplementId = other.optionalSupplementId().map(LongFilter::copy).orElse(null);
        this.factureId = other.optionalFactureId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LigneFactureSupplementCriteria copy() {
        return new LigneFactureSupplementCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getSupplementId() {
        return supplementId;
    }

    public Optional<LongFilter> optionalSupplementId() {
        return Optional.ofNullable(supplementId);
    }

    public LongFilter supplementId() {
        if (supplementId == null) {
            setSupplementId(new LongFilter());
        }
        return supplementId;
    }

    public void setSupplementId(LongFilter supplementId) {
        this.supplementId = supplementId;
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
        final LigneFactureSupplementCriteria that = (LigneFactureSupplementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(montantCalcule, that.montantCalcule) &&
            Objects.equals(description, that.description) &&
            Objects.equals(supplementId, that.supplementId) &&
            Objects.equals(factureId, that.factureId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, montantCalcule, description, supplementId, factureId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneFactureSupplementCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMontantCalcule().map(f -> "montantCalcule=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalSupplementId().map(f -> "supplementId=" + f + ", ").orElse("") +
            optionalFactureId().map(f -> "factureId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
