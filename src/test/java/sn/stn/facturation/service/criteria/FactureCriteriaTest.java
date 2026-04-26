package sn.stn.facturation.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FactureCriteriaTest {

    @Test
    void newFactureCriteriaHasAllFiltersNullTest() {
        var factureCriteria = new FactureCriteria();
        assertThat(factureCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void factureCriteriaFluentMethodsCreatesFiltersTest() {
        var factureCriteria = new FactureCriteria();

        setAllFilters(factureCriteria);

        assertThat(factureCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void factureCriteriaCopyCreatesNullFilterTest() {
        var factureCriteria = new FactureCriteria();
        var copy = factureCriteria.copy();

        assertThat(factureCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(factureCriteria)
        );
    }

    @Test
    void factureCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var factureCriteria = new FactureCriteria();
        setAllFilters(factureCriteria);

        var copy = factureCriteria.copy();

        assertThat(factureCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(factureCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var factureCriteria = new FactureCriteria();

        assertThat(factureCriteria).hasToString("FactureCriteria{}");
    }

    private static void setAllFilters(FactureCriteria factureCriteria) {
        factureCriteria.id();
        factureCriteria.numero();
        factureCriteria.dateEmission();
        factureCriteria.datePaiement();
        factureCriteria.volumeM3();
        factureCriteria.montantBaseHt();
        factureCriteria.montantSupplementsHt();
        factureCriteria.montantHt();
        factureCriteria.tauxTva();
        factureCriteria.montantTva();
        factureCriteria.montantTtc();
        factureCriteria.devise();
        factureCriteria.tauxChangeCfa();
        factureCriteria.statut();
        factureCriteria.notes();
        factureCriteria.cheminPdf();
        factureCriteria.creeParLogin();
        factureCriteria.mouvementsId();
        factureCriteria.supplementsId();
        factureCriteria.navireId();
        factureCriteria.clientId();
        factureCriteria.distinct();
    }

    private static Condition<FactureCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumero()) &&
                condition.apply(criteria.getDateEmission()) &&
                condition.apply(criteria.getDatePaiement()) &&
                condition.apply(criteria.getVolumeM3()) &&
                condition.apply(criteria.getMontantBaseHt()) &&
                condition.apply(criteria.getMontantSupplementsHt()) &&
                condition.apply(criteria.getMontantHt()) &&
                condition.apply(criteria.getTauxTva()) &&
                condition.apply(criteria.getMontantTva()) &&
                condition.apply(criteria.getMontantTtc()) &&
                condition.apply(criteria.getDevise()) &&
                condition.apply(criteria.getTauxChangeCfa()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getCheminPdf()) &&
                condition.apply(criteria.getCreeParLogin()) &&
                condition.apply(criteria.getMouvementsId()) &&
                condition.apply(criteria.getSupplementsId()) &&
                condition.apply(criteria.getNavireId()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FactureCriteria> copyFiltersAre(FactureCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumero(), copy.getNumero()) &&
                condition.apply(criteria.getDateEmission(), copy.getDateEmission()) &&
                condition.apply(criteria.getDatePaiement(), copy.getDatePaiement()) &&
                condition.apply(criteria.getVolumeM3(), copy.getVolumeM3()) &&
                condition.apply(criteria.getMontantBaseHt(), copy.getMontantBaseHt()) &&
                condition.apply(criteria.getMontantSupplementsHt(), copy.getMontantSupplementsHt()) &&
                condition.apply(criteria.getMontantHt(), copy.getMontantHt()) &&
                condition.apply(criteria.getTauxTva(), copy.getTauxTva()) &&
                condition.apply(criteria.getMontantTva(), copy.getMontantTva()) &&
                condition.apply(criteria.getMontantTtc(), copy.getMontantTtc()) &&
                condition.apply(criteria.getDevise(), copy.getDevise()) &&
                condition.apply(criteria.getTauxChangeCfa(), copy.getTauxChangeCfa()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getCheminPdf(), copy.getCheminPdf()) &&
                condition.apply(criteria.getCreeParLogin(), copy.getCreeParLogin()) &&
                condition.apply(criteria.getMouvementsId(), copy.getMouvementsId()) &&
                condition.apply(criteria.getSupplementsId(), copy.getSupplementsId()) &&
                condition.apply(criteria.getNavireId(), copy.getNavireId()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
