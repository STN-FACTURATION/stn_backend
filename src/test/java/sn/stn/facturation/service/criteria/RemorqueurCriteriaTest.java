package sn.stn.facturation.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RemorqueurCriteriaTest {

    @Test
    void newRemorqueurCriteriaHasAllFiltersNullTest() {
        var remorqueurCriteria = new RemorqueurCriteria();
        assertThat(remorqueurCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void remorqueurCriteriaFluentMethodsCreatesFiltersTest() {
        var remorqueurCriteria = new RemorqueurCriteria();

        setAllFilters(remorqueurCriteria);

        assertThat(remorqueurCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void remorqueurCriteriaCopyCreatesNullFilterTest() {
        var remorqueurCriteria = new RemorqueurCriteria();
        var copy = remorqueurCriteria.copy();

        assertThat(remorqueurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(remorqueurCriteria)
        );
    }

    @Test
    void remorqueurCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var remorqueurCriteria = new RemorqueurCriteria();
        setAllFilters(remorqueurCriteria);

        var copy = remorqueurCriteria.copy();

        assertThat(remorqueurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(remorqueurCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var remorqueurCriteria = new RemorqueurCriteria();

        assertThat(remorqueurCriteria).hasToString("RemorqueurCriteria{}");
    }

    private static void setAllFilters(RemorqueurCriteria remorqueurCriteria) {
        remorqueurCriteria.id();
        remorqueurCriteria.code();
        remorqueurCriteria.nom();
        remorqueurCriteria.statut();
        remorqueurCriteria.observation();
        remorqueurCriteria.distinct();
    }

    private static Condition<RemorqueurCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getObservation()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RemorqueurCriteria> copyFiltersAre(RemorqueurCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getObservation(), copy.getObservation()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
