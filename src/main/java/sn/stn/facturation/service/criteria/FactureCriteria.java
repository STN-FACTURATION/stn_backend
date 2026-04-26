package sn.stn.facturation.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import sn.stn.facturation.domain.enumeration.DeviseFacture;
import sn.stn.facturation.domain.enumeration.StatutFacture;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.stn.facturation.domain.Facture} entity. This class is used
 * in {@link sn.stn.facturation.web.rest.FactureResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /factures?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactureCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DeviseFacture
     */
    public static class DeviseFactureFilter extends Filter<DeviseFacture> {

        public DeviseFactureFilter() {}

        public DeviseFactureFilter(DeviseFactureFilter filter) {
            super(filter);
        }

        @Override
        public DeviseFactureFilter copy() {
            return new DeviseFactureFilter(this);
        }
    }

    /**
     * Class for filtering StatutFacture
     */
    public static class StatutFactureFilter extends Filter<StatutFacture> {

        public StatutFactureFilter() {}

        public StatutFactureFilter(StatutFactureFilter filter) {
            super(filter);
        }

        @Override
        public StatutFactureFilter copy() {
            return new StatutFactureFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numero;

    private LocalDateFilter dateEmission;

    private LocalDateFilter datePaiement;

    private DoubleFilter volumeM3;

    private DoubleFilter montantBaseHt;

    private DoubleFilter montantSupplementsHt;

    private DoubleFilter montantHt;

    private DoubleFilter tauxTva;

    private DoubleFilter montantTva;

    private DoubleFilter montantTtc;

    private DeviseFactureFilter devise;

    private DoubleFilter tauxChangeCfa;

    private StatutFactureFilter statut;

    private StringFilter notes;

    private StringFilter cheminPdf;

    private StringFilter creeParLogin;

    private LongFilter mouvementsId;

    private LongFilter supplementsId;

    private LongFilter navireId;

    private LongFilter clientId;

    private Boolean distinct;

    public FactureCriteria() {}

    public FactureCriteria(FactureCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numero = other.optionalNumero().map(StringFilter::copy).orElse(null);
        this.dateEmission = other.optionalDateEmission().map(LocalDateFilter::copy).orElse(null);
        this.datePaiement = other.optionalDatePaiement().map(LocalDateFilter::copy).orElse(null);
        this.volumeM3 = other.optionalVolumeM3().map(DoubleFilter::copy).orElse(null);
        this.montantBaseHt = other.optionalMontantBaseHt().map(DoubleFilter::copy).orElse(null);
        this.montantSupplementsHt = other.optionalMontantSupplementsHt().map(DoubleFilter::copy).orElse(null);
        this.montantHt = other.optionalMontantHt().map(DoubleFilter::copy).orElse(null);
        this.tauxTva = other.optionalTauxTva().map(DoubleFilter::copy).orElse(null);
        this.montantTva = other.optionalMontantTva().map(DoubleFilter::copy).orElse(null);
        this.montantTtc = other.optionalMontantTtc().map(DoubleFilter::copy).orElse(null);
        this.devise = other.optionalDevise().map(DeviseFactureFilter::copy).orElse(null);
        this.tauxChangeCfa = other.optionalTauxChangeCfa().map(DoubleFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutFactureFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.cheminPdf = other.optionalCheminPdf().map(StringFilter::copy).orElse(null);
        this.creeParLogin = other.optionalCreeParLogin().map(StringFilter::copy).orElse(null);
        this.mouvementsId = other.optionalMouvementsId().map(LongFilter::copy).orElse(null);
        this.supplementsId = other.optionalSupplementsId().map(LongFilter::copy).orElse(null);
        this.navireId = other.optionalNavireId().map(LongFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FactureCriteria copy() {
        return new FactureCriteria(this);
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

    public StringFilter getNumero() {
        return numero;
    }

    public Optional<StringFilter> optionalNumero() {
        return Optional.ofNullable(numero);
    }

    public StringFilter numero() {
        if (numero == null) {
            setNumero(new StringFilter());
        }
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
    }

    public LocalDateFilter getDateEmission() {
        return dateEmission;
    }

    public Optional<LocalDateFilter> optionalDateEmission() {
        return Optional.ofNullable(dateEmission);
    }

    public LocalDateFilter dateEmission() {
        if (dateEmission == null) {
            setDateEmission(new LocalDateFilter());
        }
        return dateEmission;
    }

    public void setDateEmission(LocalDateFilter dateEmission) {
        this.dateEmission = dateEmission;
    }

    public LocalDateFilter getDatePaiement() {
        return datePaiement;
    }

    public Optional<LocalDateFilter> optionalDatePaiement() {
        return Optional.ofNullable(datePaiement);
    }

    public LocalDateFilter datePaiement() {
        if (datePaiement == null) {
            setDatePaiement(new LocalDateFilter());
        }
        return datePaiement;
    }

    public void setDatePaiement(LocalDateFilter datePaiement) {
        this.datePaiement = datePaiement;
    }

    public DoubleFilter getVolumeM3() {
        return volumeM3;
    }

    public Optional<DoubleFilter> optionalVolumeM3() {
        return Optional.ofNullable(volumeM3);
    }

    public DoubleFilter volumeM3() {
        if (volumeM3 == null) {
            setVolumeM3(new DoubleFilter());
        }
        return volumeM3;
    }

    public void setVolumeM3(DoubleFilter volumeM3) {
        this.volumeM3 = volumeM3;
    }

    public DoubleFilter getMontantBaseHt() {
        return montantBaseHt;
    }

    public Optional<DoubleFilter> optionalMontantBaseHt() {
        return Optional.ofNullable(montantBaseHt);
    }

    public DoubleFilter montantBaseHt() {
        if (montantBaseHt == null) {
            setMontantBaseHt(new DoubleFilter());
        }
        return montantBaseHt;
    }

    public void setMontantBaseHt(DoubleFilter montantBaseHt) {
        this.montantBaseHt = montantBaseHt;
    }

    public DoubleFilter getMontantSupplementsHt() {
        return montantSupplementsHt;
    }

    public Optional<DoubleFilter> optionalMontantSupplementsHt() {
        return Optional.ofNullable(montantSupplementsHt);
    }

    public DoubleFilter montantSupplementsHt() {
        if (montantSupplementsHt == null) {
            setMontantSupplementsHt(new DoubleFilter());
        }
        return montantSupplementsHt;
    }

    public void setMontantSupplementsHt(DoubleFilter montantSupplementsHt) {
        this.montantSupplementsHt = montantSupplementsHt;
    }

    public DoubleFilter getMontantHt() {
        return montantHt;
    }

    public Optional<DoubleFilter> optionalMontantHt() {
        return Optional.ofNullable(montantHt);
    }

    public DoubleFilter montantHt() {
        if (montantHt == null) {
            setMontantHt(new DoubleFilter());
        }
        return montantHt;
    }

    public void setMontantHt(DoubleFilter montantHt) {
        this.montantHt = montantHt;
    }

    public DoubleFilter getTauxTva() {
        return tauxTva;
    }

    public Optional<DoubleFilter> optionalTauxTva() {
        return Optional.ofNullable(tauxTva);
    }

    public DoubleFilter tauxTva() {
        if (tauxTva == null) {
            setTauxTva(new DoubleFilter());
        }
        return tauxTva;
    }

    public void setTauxTva(DoubleFilter tauxTva) {
        this.tauxTva = tauxTva;
    }

    public DoubleFilter getMontantTva() {
        return montantTva;
    }

    public Optional<DoubleFilter> optionalMontantTva() {
        return Optional.ofNullable(montantTva);
    }

    public DoubleFilter montantTva() {
        if (montantTva == null) {
            setMontantTva(new DoubleFilter());
        }
        return montantTva;
    }

    public void setMontantTva(DoubleFilter montantTva) {
        this.montantTva = montantTva;
    }

    public DoubleFilter getMontantTtc() {
        return montantTtc;
    }

    public Optional<DoubleFilter> optionalMontantTtc() {
        return Optional.ofNullable(montantTtc);
    }

    public DoubleFilter montantTtc() {
        if (montantTtc == null) {
            setMontantTtc(new DoubleFilter());
        }
        return montantTtc;
    }

    public void setMontantTtc(DoubleFilter montantTtc) {
        this.montantTtc = montantTtc;
    }

    public DeviseFactureFilter getDevise() {
        return devise;
    }

    public Optional<DeviseFactureFilter> optionalDevise() {
        return Optional.ofNullable(devise);
    }

    public DeviseFactureFilter devise() {
        if (devise == null) {
            setDevise(new DeviseFactureFilter());
        }
        return devise;
    }

    public void setDevise(DeviseFactureFilter devise) {
        this.devise = devise;
    }

    public DoubleFilter getTauxChangeCfa() {
        return tauxChangeCfa;
    }

    public Optional<DoubleFilter> optionalTauxChangeCfa() {
        return Optional.ofNullable(tauxChangeCfa);
    }

    public DoubleFilter tauxChangeCfa() {
        if (tauxChangeCfa == null) {
            setTauxChangeCfa(new DoubleFilter());
        }
        return tauxChangeCfa;
    }

    public void setTauxChangeCfa(DoubleFilter tauxChangeCfa) {
        this.tauxChangeCfa = tauxChangeCfa;
    }

    public StatutFactureFilter getStatut() {
        return statut;
    }

    public Optional<StatutFactureFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutFactureFilter statut() {
        if (statut == null) {
            setStatut(new StatutFactureFilter());
        }
        return statut;
    }

    public void setStatut(StatutFactureFilter statut) {
        this.statut = statut;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public Optional<StringFilter> optionalNotes() {
        return Optional.ofNullable(notes);
    }

    public StringFilter notes() {
        if (notes == null) {
            setNotes(new StringFilter());
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public StringFilter getCheminPdf() {
        return cheminPdf;
    }

    public Optional<StringFilter> optionalCheminPdf() {
        return Optional.ofNullable(cheminPdf);
    }

    public StringFilter cheminPdf() {
        if (cheminPdf == null) {
            setCheminPdf(new StringFilter());
        }
        return cheminPdf;
    }

    public void setCheminPdf(StringFilter cheminPdf) {
        this.cheminPdf = cheminPdf;
    }

    public StringFilter getCreeParLogin() {
        return creeParLogin;
    }

    public Optional<StringFilter> optionalCreeParLogin() {
        return Optional.ofNullable(creeParLogin);
    }

    public StringFilter creeParLogin() {
        if (creeParLogin == null) {
            setCreeParLogin(new StringFilter());
        }
        return creeParLogin;
    }

    public void setCreeParLogin(StringFilter creeParLogin) {
        this.creeParLogin = creeParLogin;
    }

    public LongFilter getMouvementsId() {
        return mouvementsId;
    }

    public Optional<LongFilter> optionalMouvementsId() {
        return Optional.ofNullable(mouvementsId);
    }

    public LongFilter mouvementsId() {
        if (mouvementsId == null) {
            setMouvementsId(new LongFilter());
        }
        return mouvementsId;
    }

    public void setMouvementsId(LongFilter mouvementsId) {
        this.mouvementsId = mouvementsId;
    }

    public LongFilter getSupplementsId() {
        return supplementsId;
    }

    public Optional<LongFilter> optionalSupplementsId() {
        return Optional.ofNullable(supplementsId);
    }

    public LongFilter supplementsId() {
        if (supplementsId == null) {
            setSupplementsId(new LongFilter());
        }
        return supplementsId;
    }

    public void setSupplementsId(LongFilter supplementsId) {
        this.supplementsId = supplementsId;
    }

    public LongFilter getNavireId() {
        return navireId;
    }

    public Optional<LongFilter> optionalNavireId() {
        return Optional.ofNullable(navireId);
    }

    public LongFilter navireId() {
        if (navireId == null) {
            setNavireId(new LongFilter());
        }
        return navireId;
    }

    public void setNavireId(LongFilter navireId) {
        this.navireId = navireId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public Optional<LongFilter> optionalClientId() {
        return Optional.ofNullable(clientId);
    }

    public LongFilter clientId() {
        if (clientId == null) {
            setClientId(new LongFilter());
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
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
        final FactureCriteria that = (FactureCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(dateEmission, that.dateEmission) &&
            Objects.equals(datePaiement, that.datePaiement) &&
            Objects.equals(volumeM3, that.volumeM3) &&
            Objects.equals(montantBaseHt, that.montantBaseHt) &&
            Objects.equals(montantSupplementsHt, that.montantSupplementsHt) &&
            Objects.equals(montantHt, that.montantHt) &&
            Objects.equals(tauxTva, that.tauxTva) &&
            Objects.equals(montantTva, that.montantTva) &&
            Objects.equals(montantTtc, that.montantTtc) &&
            Objects.equals(devise, that.devise) &&
            Objects.equals(tauxChangeCfa, that.tauxChangeCfa) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(cheminPdf, that.cheminPdf) &&
            Objects.equals(creeParLogin, that.creeParLogin) &&
            Objects.equals(mouvementsId, that.mouvementsId) &&
            Objects.equals(supplementsId, that.supplementsId) &&
            Objects.equals(navireId, that.navireId) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numero,
            dateEmission,
            datePaiement,
            volumeM3,
            montantBaseHt,
            montantSupplementsHt,
            montantHt,
            tauxTva,
            montantTva,
            montantTtc,
            devise,
            tauxChangeCfa,
            statut,
            notes,
            cheminPdf,
            creeParLogin,
            mouvementsId,
            supplementsId,
            navireId,
            clientId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactureCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumero().map(f -> "numero=" + f + ", ").orElse("") +
            optionalDateEmission().map(f -> "dateEmission=" + f + ", ").orElse("") +
            optionalDatePaiement().map(f -> "datePaiement=" + f + ", ").orElse("") +
            optionalVolumeM3().map(f -> "volumeM3=" + f + ", ").orElse("") +
            optionalMontantBaseHt().map(f -> "montantBaseHt=" + f + ", ").orElse("") +
            optionalMontantSupplementsHt().map(f -> "montantSupplementsHt=" + f + ", ").orElse("") +
            optionalMontantHt().map(f -> "montantHt=" + f + ", ").orElse("") +
            optionalTauxTva().map(f -> "tauxTva=" + f + ", ").orElse("") +
            optionalMontantTva().map(f -> "montantTva=" + f + ", ").orElse("") +
            optionalMontantTtc().map(f -> "montantTtc=" + f + ", ").orElse("") +
            optionalDevise().map(f -> "devise=" + f + ", ").orElse("") +
            optionalTauxChangeCfa().map(f -> "tauxChangeCfa=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalCheminPdf().map(f -> "cheminPdf=" + f + ", ").orElse("") +
            optionalCreeParLogin().map(f -> "creeParLogin=" + f + ", ").orElse("") +
            optionalMouvementsId().map(f -> "mouvementsId=" + f + ", ").orElse("") +
            optionalSupplementsId().map(f -> "supplementsId=" + f + ", ").orElse("") +
            optionalNavireId().map(f -> "navireId=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
