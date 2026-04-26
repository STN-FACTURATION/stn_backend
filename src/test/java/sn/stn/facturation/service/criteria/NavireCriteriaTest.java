package sn.stn.facturation.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NavireCriteriaTest {

    @Test
    void newNavireCriteriaHasAllFiltersNullTest() {
        var navireCriteria = new NavireCriteria();
        assertThat(navireCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void navireCriteriaFluentMethodsCreatesFiltersTest() {
        var navireCriteria = new NavireCriteria();

        setAllFilters(navireCriteria);

        assertThat(navireCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void navireCriteriaCopyCreatesNullFilterTest() {
        var navireCriteria = new NavireCriteria();
        var copy = navireCriteria.copy();

        assertThat(navireCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(navireCriteria)
        );
    }

    @Test
    void navireCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var navireCriteria = new NavireCriteria();
        setAllFilters(navireCriteria);

        var copy = navireCriteria.copy();

        assertThat(navireCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(navireCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var navireCriteria = new NavireCriteria();

        assertThat(navireCriteria).hasToString("NavireCriteria{}");
    }

    private static void setAllFilters(NavireCriteria navireCriteria) {
        navireCriteria.id();
        navireCriteria.nom();
        navireCriteria.numeroImo();
        navireCriteria.jaugeBrute();
        navireCriteria.longueur();
        navireCriteria.largeur();
        navireCriteria.tirant();
        navireCriteria.pavillon();
        navireCriteria.actif();
        navireCriteria.distinct();
    }

    private static Condition<NavireCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getNumeroImo()) &&
                condition.apply(criteria.getJaugeBrute()) &&
                condition.apply(criteria.getLongueur()) &&
                condition.apply(criteria.getLargeur()) &&
                condition.apply(criteria.getTirant()) &&
                condition.apply(criteria.getPavillon()) &&
                condition.apply(criteria.getActif()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NavireCriteria> copyFiltersAre(NavireCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getNumeroImo(), copy.getNumeroImo()) &&
                condition.apply(criteria.getJaugeBrute(), copy.getJaugeBrute()) &&
                condition.apply(criteria.getLongueur(), copy.getLongueur()) &&
                condition.apply(criteria.getLargeur(), copy.getLargeur()) &&
                condition.apply(criteria.getTirant(), copy.getTirant()) &&
                condition.apply(criteria.getPavillon(), copy.getPavillon()) &&
                condition.apply(criteria.getActif(), copy.getActif()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
