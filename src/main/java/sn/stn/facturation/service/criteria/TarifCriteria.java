package sn.stn.facturation.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.stn.facturation.domain.Tarif} entity. This class is used
 * in {@link sn.stn.facturation.web.rest.TarifResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tarifs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TarifCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter trancheMin;

    private DoubleFilter trancheMax;

    private DoubleFilter prixEuro;

    private BooleanFilter actif;

    private LocalDateFilter dateDebut;

    private LocalDateFilter dateFin;

    private StringFilter description;

    private LongFilter historiquesId;

    private Boolean distinct;

    public TarifCriteria() {}

    public TarifCriteria(TarifCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.trancheMin = other.optionalTrancheMin().map(DoubleFilter::copy).orElse(null);
        this.trancheMax = other.optionalTrancheMax().map(DoubleFilter::copy).orElse(null);
        this.prixEuro = other.optionalPrixEuro().map(DoubleFilter::copy).orElse(null);
        this.actif = other.optionalActif().map(BooleanFilter::copy).orElse(null);
        this.dateDebut = other.optionalDateDebut().map(LocalDateFilter::copy).orElse(null);
        this.dateFin = other.optionalDateFin().map(LocalDateFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.historiquesId = other.optionalHistoriquesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TarifCriteria copy() {
        return new TarifCriteria(this);
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

    public DoubleFilter getTrancheMin() {
        return trancheMin;
    }

    public Optional<DoubleFilter> optionalTrancheMin() {
        return Optional.ofNullable(trancheMin);
    }

    public DoubleFilter trancheMin() {
        if (trancheMin == null) {
            setTrancheMin(new DoubleFilter());
        }
        return trancheMin;
    }

    public void setTrancheMin(DoubleFilter trancheMin) {
        this.trancheMin = trancheMin;
    }

    public DoubleFilter getTrancheMax() {
        return trancheMax;
    }

    public Optional<DoubleFilter> optionalTrancheMax() {
        return Optional.ofNullable(trancheMax);
    }

    public DoubleFilter trancheMax() {
        if (trancheMax == null) {
            setTrancheMax(new DoubleFilter());
        }
        return trancheMax;
    }

    public void setTrancheMax(DoubleFilter trancheMax) {
        this.trancheMax = trancheMax;
    }

    public DoubleFilter getPrixEuro() {
        return prixEuro;
    }

    public Optional<DoubleFilter> optionalPrixEuro() {
        return Optional.ofNullable(prixEuro);
    }

    public DoubleFilter prixEuro() {
        if (prixEuro == null) {
            setPrixEuro(new DoubleFilter());
        }
        return prixEuro;
    }

    public void setPrixEuro(DoubleFilter prixEuro) {
        this.prixEuro = prixEuro;
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

    public LocalDateFilter getDateDebut() {
        return dateDebut;
    }

    public Optional<LocalDateFilter> optionalDateDebut() {
        return Optional.ofNullable(dateDebut);
    }

    public LocalDateFilter dateDebut() {
        if (dateDebut == null) {
            setDateDebut(new LocalDateFilter());
        }
        return dateDebut;
    }

    public void setDateDebut(LocalDateFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateFilter getDateFin() {
        return dateFin;
    }

    public Optional<LocalDateFilter> optionalDateFin() {
        return Optional.ofNullable(dateFin);
    }

    public LocalDateFilter dateFin() {
        if (dateFin == null) {
            setDateFin(new LocalDateFilter());
        }
        return dateFin;
    }

    public void setDateFin(LocalDateFilter dateFin) {
        this.dateFin = dateFin;
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

    public LongFilter getHistoriquesId() {
        return historiquesId;
    }

    public Optional<LongFilter> optionalHistoriquesId() {
        return Optional.ofNullable(historiquesId);
    }

    public LongFilter historiquesId() {
        if (historiquesId == null) {
            setHistoriquesId(new LongFilter());
        }
        return historiquesId;
    }

    public void setHistoriquesId(LongFilter historiquesId) {
        this.historiquesId = historiquesId;
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
        final TarifCriteria that = (TarifCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(trancheMin, that.trancheMin) &&
            Objects.equals(trancheMax, that.trancheMax) &&
            Objects.equals(prixEuro, that.prixEuro) &&
            Objects.equals(actif, that.actif) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(description, that.description) &&
            Objects.equals(historiquesId, that.historiquesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trancheMin, trancheMax, prixEuro, actif, dateDebut, dateFin, description, historiquesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TarifCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTrancheMin().map(f -> "trancheMin=" + f + ", ").orElse("") +
            optionalTrancheMax().map(f -> "trancheMax=" + f + ", ").orElse("") +
            optionalPrixEuro().map(f -> "prixEuro=" + f + ", ").orElse("") +
            optionalActif().map(f -> "actif=" + f + ", ").orElse("") +
            optionalDateDebut().map(f -> "dateDebut=" + f + ", ").orElse("") +
            optionalDateFin().map(f -> "dateFin=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalHistoriquesId().map(f -> "historiquesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
