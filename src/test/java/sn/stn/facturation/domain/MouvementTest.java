package sn.stn.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.stn.facturation.domain.FactureTestSamples.*;
import static sn.stn.facturation.domain.MouvementTestSamples.*;
import static sn.stn.facturation.domain.RemorqueurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.stn.facturation.web.rest.TestUtil;

class MouvementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mouvement.class);
        Mouvement mouvement1 = getMouvementSample1();
        Mouvement mouvement2 = new Mouvement();
        assertThat(mouvement1).isNotEqualTo(mouvement2);

        mouvement2.setId(mouvement1.getId());
        assertThat(mouvement1).isEqualTo(mouvement2);

        mouvement2 = getMouvementSample2();
        assertThat(mouvement1).isNotEqualTo(mouvement2);
    }

    @Test
    void remorqueurTest() {
        Mouvement mouvement = getMouvementRandomSampleGenerator();
        Remorqueur remorqueurBack = getRemorqueurRandomSampleGenerator();

        mouvement.setRemorqueur(remorqueurBack);
        assertThat(mouvement.getRemorqueur()).isEqualTo(remorqueurBack);

        mouvement.remorqueur(null);
        assertThat(mouvement.getRemorqueur()).isNull();
    }

    @Test
    void factureTest() {
        Mouvement mouvement = getMouvementRandomSampleGenerator();
        Facture factureBack = getFactureRandomSampleGenerator();

        mouvement.setFacture(factureBack);
        assertThat(mouvement.getFacture()).isEqualTo(factureBack);

        mouvement.facture(null);
        assertThat(mouvement.getFacture()).isNull();
    }
}
