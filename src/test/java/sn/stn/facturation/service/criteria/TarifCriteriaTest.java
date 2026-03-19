package sn.stn.facturation.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TarifCriteriaTest {

    @Test
    void newTarifCriteriaHasAllFiltersNullTest() {
        var tarifCriteria = new TarifCriteria();
        assertThat(tarifCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tarifCriteriaFluentMethodsCreatesFiltersTest() {
        var tarifCriteria = new TarifCriteria();

        setAllFilters(tarifCriteria);

        assertThat(tarifCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tarifCriteriaCopyCreatesNullFilterTest() {
        var tarifCriteria = new TarifCriteria();
        var copy = tarifCriteria.copy();

        assertThat(tarifCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tarifCriteria)
        );
    }

    @Test
    void tarifCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tarifCriteria = new TarifCriteria();
        setAllFilters(tarifCriteria);

        var copy = tarifCriteria.copy();

        assertThat(tarifCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tarifCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tarifCriteria = new TarifCriteria();

        assertThat(tarifCriteria).hasToString("TarifCriteria{}");
    }

    private static void setAllFilters(TarifCriteria tarifCriteria) {
        tarifCriteria.id();
        tarifCriteria.trancheMin();
        tarifCriteria.trancheMax();
        tarifCriteria.prixEuro();
        tarifCriteria.actif();
        tarifCriteria.dateDebut();
        tarifCriteria.dateFin();
        tarifCriteria.description();
        tarifCriteria.historiquesId();
        tarifCriteria.distinct();
    }

    private static Condition<TarifCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTrancheMin()) &&
                condition.apply(criteria.getTrancheMax()) &&
                condition.apply(criteria.getPrixEuro()) &&
                condition.apply(criteria.getActif()) &&
                condition.apply(criteria.getDateDebut()) &&
                condition.apply(criteria.getDateFin()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getHistoriquesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TarifCriteria> copyFiltersAre(TarifCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTrancheMin(), copy.getTrancheMin()) &&
                condition.apply(criteria.getTrancheMax(), copy.getTrancheMax()) &&
                condition.apply(criteria.getPrixEuro(), copy.getPrixEuro()) &&
                condition.apply(criteria.getActif(), copy.getActif()) &&
                condition.apply(criteria.getDateDebut(), copy.getDateDebut()) &&
                condition.apply(criteria.getDateFin(), copy.getDateFin()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getHistoriquesId(), copy.getHistoriquesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
