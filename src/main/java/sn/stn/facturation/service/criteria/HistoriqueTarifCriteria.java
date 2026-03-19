package sn.stn.facturation.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.stn.facturation.domain.HistoriqueTarif} entity. This class is used
 * in {@link sn.stn.facturation.web.rest.HistoriqueTarifResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /historique-tarifs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueTarifCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter ancienneValeur;

    private DoubleFilter nouvelleValeur;

    private InstantFilter dateModification;

    private StringFilter commentaire;

    private StringFilter modifieParLogin;

    private LongFilter tarifId;

    private Boolean distinct;

    public HistoriqueTarifCriteria() {}

    public HistoriqueTarifCriteria(HistoriqueTarifCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.ancienneValeur = other.optionalAncienneValeur().map(DoubleFilter::copy).orElse(null);
        this.nouvelleValeur = other.optionalNouvelleValeur().map(DoubleFilter::copy).orElse(null);
        this.dateModification = other.optionalDateModification().map(InstantFilter::copy).orElse(null);
        this.commentaire = other.optionalCommentaire().map(StringFilter::copy).orElse(null);
        this.modifieParLogin = other.optionalModifieParLogin().map(StringFilter::copy).orElse(null);
        this.tarifId = other.optionalTarifId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HistoriqueTarifCriteria copy() {
        return new HistoriqueTarifCriteria(this);
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

    public DoubleFilter getAncienneValeur() {
        return ancienneValeur;
    }

    public Optional<DoubleFilter> optionalAncienneValeur() {
        return Optional.ofNullable(ancienneValeur);
    }

    public DoubleFilter ancienneValeur() {
        if (ancienneValeur == null) {
            setAncienneValeur(new DoubleFilter());
        }
        return ancienneValeur;
    }

    public void setAncienneValeur(DoubleFilter ancienneValeur) {
        this.ancienneValeur = ancienneValeur;
    }

    public DoubleFilter getNouvelleValeur() {
        return nouvelleValeur;
    }

    public Optional<DoubleFilter> optionalNouvelleValeur() {
        return Optional.ofNullable(nouvelleValeur);
    }

    public DoubleFilter nouvelleValeur() {
        if (nouvelleValeur == null) {
            setNouvelleValeur(new DoubleFilter());
        }
        return nouvelleValeur;
    }

    public void setNouvelleValeur(DoubleFilter nouvelleValeur) {
        this.nouvelleValeur = nouvelleValeur;
    }

    public InstantFilter getDateModification() {
        return dateModification;
    }

    public Optional<InstantFilter> optionalDateModification() {
        return Optional.ofNullable(dateModification);
    }

    public InstantFilter dateModification() {
        if (dateModification == null) {
            setDateModification(new InstantFilter());
        }
        return dateModification;
    }

    public void setDateModification(InstantFilter dateModification) {
        this.dateModification = dateModification;
    }

    public StringFilter getCommentaire() {
        return commentaire;
    }

    public Optional<StringFilter> optionalCommentaire() {
        return Optional.ofNullable(commentaire);
    }

    public StringFilter commentaire() {
        if (commentaire == null) {
            setCommentaire(new StringFilter());
        }
        return commentaire;
    }

    public void setCommentaire(StringFilter commentaire) {
        this.commentaire = commentaire;
    }

    public StringFilter getModifieParLogin() {
        return modifieParLogin;
    }

    public Optional<StringFilter> optionalModifieParLogin() {
        return Optional.ofNullable(modifieParLogin);
    }

    public StringFilter modifieParLogin() {
        if (modifieParLogin == null) {
            setModifieParLogin(new StringFilter());
        }
        return modifieParLogin;
    }

    public void setModifieParLogin(StringFilter modifieParLogin) {
        this.modifieParLogin = modifieParLogin;
    }

    public LongFilter getTarifId() {
        return tarifId;
    }

    public Optional<LongFilter> optionalTarifId() {
        return Optional.ofNullable(tarifId);
    }

    public LongFilter tarifId() {
        if (tarifId == null) {
            setTarifId(new LongFilter());
        }
        return tarifId;
    }

    public void setTarifId(LongFilter tarifId) {
        this.tarifId = tarifId;
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
        final HistoriqueTarifCriteria that = (HistoriqueTarifCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ancienneValeur, that.ancienneValeur) &&
            Objects.equals(nouvelleValeur, that.nouvelleValeur) &&
            Objects.equals(dateModification, that.dateModification) &&
            Objects.equals(commentaire, that.commentaire) &&
            Objects.equals(modifieParLogin, that.modifieParLogin) &&
            Objects.equals(tarifId, that.tarifId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ancienneValeur, nouvelleValeur, dateModification, commentaire, modifieParLogin, tarifId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueTarifCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAncienneValeur().map(f -> "ancienneValeur=" + f + ", ").orElse("") +
            optionalNouvelleValeur().map(f -> "nouvelleValeur=" + f + ", ").orElse("") +
            optionalDateModification().map(f -> "dateModification=" + f + ", ").orElse("") +
            optionalCommentaire().map(f -> "commentaire=" + f + ", ").orElse("") +
            optionalModifieParLogin().map(f -> "modifieParLogin=" + f + ", ").orElse("") +
            optionalTarifId().map(f -> "tarifId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
