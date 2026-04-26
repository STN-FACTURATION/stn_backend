package sn.stn.facturation.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MouvementCriteriaTest {

    @Test
    void newMouvementCriteriaHasAllFiltersNullTest() {
        var mouvementCriteria = new MouvementCriteria();
        assertThat(mouvementCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void mouvementCriteriaFluentMethodsCreatesFiltersTest() {
        var mouvementCriteria = new MouvementCriteria();

        setAllFilters(mouvementCriteria);

        assertThat(mouvementCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void mouvementCriteriaCopyCreatesNullFilterTest() {
        var mouvementCriteria = new MouvementCriteria();
        var copy = mouvementCriteria.copy();

        assertThat(mouvementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(mouvementCriteria)
        );
    }

    @Test
    void mouvementCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var mouvementCriteria = new MouvementCriteria();
        setAllFilters(mouvementCriteria);

        var copy = mouvementCriteria.copy();

        assertThat(mouvementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(mouvementCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var mouvementCriteria = new MouvementCriteria();

        assertThat(mouvementCriteria).hasToString("MouvementCriteria{}");
    }

    private static void setAllFilters(MouvementCriteria mouvementCriteria) {
        mouvementCriteria.id();
        mouvementCriteria.type();
        mouvementCriteria.posteA();
        mouvementCriteria.posteB();
        mouvementCriteria.duree();
        mouvementCriteria.montantCalcule();
        mouvementCriteria.libelle();
        mouvementCriteria.remorqueurId();
        mouvementCriteria.factureId();
        mouvementCriteria.distinct();
    }

    private static Condition<MouvementCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getPosteA()) &&
                condition.apply(criteria.getPosteB()) &&
                condition.apply(criteria.getDuree()) &&
                condition.apply(criteria.getMontantCalcule()) &&
                condition.apply(criteria.getLibelle()) &&
                condition.apply(criteria.getRemorqueurId()) &&
                condition.apply(criteria.getFactureId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MouvementCriteria> copyFiltersAre(MouvementCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getPosteA(), copy.getPosteA()) &&
                condition.apply(criteria.getPosteB(), copy.getPosteB()) &&
                condition.apply(criteria.getDuree(), copy.getDuree()) &&
                condition.apply(criteria.getMontantCalcule(), copy.getMontantCalcule()) &&
                condition.apply(criteria.getLibelle(), copy.getLibelle()) &&
                condition.apply(criteria.getRemorqueurId(), copy.getRemorqueurId()) &&
                condition.apply(criteria.getFactureId(), copy.getFactureId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
