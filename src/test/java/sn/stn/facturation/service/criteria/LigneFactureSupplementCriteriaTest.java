package sn.stn.facturation.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LigneFactureSupplementCriteriaTest {

    @Test
    void newLigneFactureSupplementCriteriaHasAllFiltersNullTest() {
        var ligneFactureSupplementCriteria = new LigneFactureSupplementCriteria();
        assertThat(ligneFactureSupplementCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void ligneFactureSupplementCriteriaFluentMethodsCreatesFiltersTest() {
        var ligneFactureSupplementCriteria = new LigneFactureSupplementCriteria();

        setAllFilters(ligneFactureSupplementCriteria);

        assertThat(ligneFactureSupplementCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void ligneFactureSupplementCriteriaCopyCreatesNullFilterTest() {
        var ligneFactureSupplementCriteria = new LigneFactureSupplementCriteria();
        var copy = ligneFactureSupplementCriteria.copy();

        assertThat(ligneFactureSupplementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(ligneFactureSupplementCriteria)
        );
    }

    @Test
    void ligneFactureSupplementCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ligneFactureSupplementCriteria = new LigneFactureSupplementCriteria();
        setAllFilters(ligneFactureSupplementCriteria);

        var copy = ligneFactureSupplementCriteria.copy();

        assertThat(ligneFactureSupplementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(ligneFactureSupplementCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ligneFactureSupplementCriteria = new LigneFactureSupplementCriteria();

        assertThat(ligneFactureSupplementCriteria).hasToString("LigneFactureSupplementCriteria{}");
    }

    private static void setAllFilters(LigneFactureSupplementCriteria ligneFactureSupplementCriteria) {
        ligneFactureSupplementCriteria.id();
        ligneFactureSupplementCriteria.montantCalcule();
        ligneFactureSupplementCriteria.description();
        ligneFactureSupplementCriteria.supplementId();
        ligneFactureSupplementCriteria.factureId();
        ligneFactureSupplementCriteria.distinct();
    }

    private static Condition<LigneFactureSupplementCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMontantCalcule()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getSupplementId()) &&
                condition.apply(criteria.getFactureId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LigneFactureSupplementCriteria> copyFiltersAre(
        LigneFactureSupplementCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMontantCalcule(), copy.getMontantCalcule()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getSupplementId(), copy.getSupplementId()) &&
                condition.apply(criteria.getFactureId(), copy.getFactureId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
