package sn.stn.facturation.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HistoriqueTarifCriteriaTest {

    @Test
    void newHistoriqueTarifCriteriaHasAllFiltersNullTest() {
        var historiqueTarifCriteria = new HistoriqueTarifCriteria();
        assertThat(historiqueTarifCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void historiqueTarifCriteriaFluentMethodsCreatesFiltersTest() {
        var historiqueTarifCriteria = new HistoriqueTarifCriteria();

        setAllFilters(historiqueTarifCriteria);

        assertThat(historiqueTarifCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void historiqueTarifCriteriaCopyCreatesNullFilterTest() {
        var historiqueTarifCriteria = new HistoriqueTarifCriteria();
        var copy = historiqueTarifCriteria.copy();

        assertThat(historiqueTarifCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(historiqueTarifCriteria)
        );
    }

    @Test
    void historiqueTarifCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var historiqueTarifCriteria = new HistoriqueTarifCriteria();
        setAllFilters(historiqueTarifCriteria);

        var copy = historiqueTarifCriteria.copy();

        assertThat(historiqueTarifCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(historiqueTarifCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var historiqueTarifCriteria = new HistoriqueTarifCriteria();

        assertThat(historiqueTarifCriteria).hasToString("HistoriqueTarifCriteria{}");
    }

    private static void setAllFilters(HistoriqueTarifCriteria historiqueTarifCriteria) {
        historiqueTarifCriteria.id();
        historiqueTarifCriteria.ancienneValeur();
        historiqueTarifCriteria.nouvelleValeur();
        historiqueTarifCriteria.dateModification();
        historiqueTarifCriteria.commentaire();
        historiqueTarifCriteria.modifieParLogin();
        historiqueTarifCriteria.tarifId();
        historiqueTarifCriteria.distinct();
    }

    private static Condition<HistoriqueTarifCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAncienneValeur()) &&
                condition.apply(criteria.getNouvelleValeur()) &&
                condition.apply(criteria.getDateModification()) &&
                condition.apply(criteria.getCommentaire()) &&
                condition.apply(criteria.getModifieParLogin()) &&
                condition.apply(criteria.getTarifId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HistoriqueTarifCriteria> copyFiltersAre(
        HistoriqueTarifCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAncienneValeur(), copy.getAncienneValeur()) &&
                condition.apply(criteria.getNouvelleValeur(), copy.getNouvelleValeur()) &&
                condition.apply(criteria.getDateModification(), copy.getDateModification()) &&
                condition.apply(criteria.getCommentaire(), copy.getCommentaire()) &&
                condition.apply(criteria.getModifieParLogin(), copy.getModifieParLogin()) &&
                condition.apply(criteria.getTarifId(), copy.getTarifId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
