package sn.stn.facturation.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SupplementCriteriaTest {

    @Test
    void newSupplementCriteriaHasAllFiltersNullTest() {
        var supplementCriteria = new SupplementCriteria();
        assertThat(supplementCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void supplementCriteriaFluentMethodsCreatesFiltersTest() {
        var supplementCriteria = new SupplementCriteria();

        setAllFilters(supplementCriteria);

        assertThat(supplementCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void supplementCriteriaCopyCreatesNullFilterTest() {
        var supplementCriteria = new SupplementCriteria();
        var copy = supplementCriteria.copy();

        assertThat(supplementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(supplementCriteria)
        );
    }

    @Test
    void supplementCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var supplementCriteria = new SupplementCriteria();
        setAllFilters(supplementCriteria);

        var copy = supplementCriteria.copy();

        assertThat(supplementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(supplementCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var supplementCriteria = new SupplementCriteria();

        assertThat(supplementCriteria).hasToString("SupplementCriteria{}");
    }

    private static void setAllFilters(SupplementCriteria supplementCriteria) {
        supplementCriteria.id();
        supplementCriteria.code();
        supplementCriteria.libelle();
        supplementCriteria.type();
        supplementCriteria.tauxPourcentage();
        supplementCriteria.montantFixe();
        supplementCriteria.actif();
        supplementCriteria.distinct();
    }

    private static Condition<SupplementCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getLibelle()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getTauxPourcentage()) &&
                condition.apply(criteria.getMontantFixe()) &&
                condition.apply(criteria.getActif()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SupplementCriteria> copyFiltersAre(SupplementCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getLibelle(), copy.getLibelle()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getTauxPourcentage(), copy.getTauxPourcentage()) &&
                condition.apply(criteria.getMontantFixe(), copy.getMontantFixe()) &&
                condition.apply(criteria.getActif(), copy.getActif()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
