package sn.stn.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.stn.facturation.domain.ClientTestSamples.*;
import static sn.stn.facturation.domain.FactureTestSamples.*;
import static sn.stn.facturation.domain.LigneFactureSupplementTestSamples.*;
import static sn.stn.facturation.domain.MouvementTestSamples.*;
import static sn.stn.facturation.domain.NavireTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.stn.facturation.web.rest.TestUtil;

class FactureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facture.class);
        Facture facture1 = getFactureSample1();
        Facture facture2 = new Facture();
        assertThat(facture1).isNotEqualTo(facture2);

        facture2.setId(facture1.getId());
        assertThat(facture1).isEqualTo(facture2);

        facture2 = getFactureSample2();
        assertThat(facture1).isNotEqualTo(facture2);
    }

    @Test
    void mouvementsTest() {
        Facture facture = getFactureRandomSampleGenerator();
        Mouvement mouvementBack = getMouvementRandomSampleGenerator();

        facture.addMouvements(mouvementBack);
        assertThat(facture.getMouvements()).containsOnly(mouvementBack);
        assertThat(mouvementBack.getFacture()).isEqualTo(facture);

        facture.removeMouvements(mouvementBack);
        assertThat(facture.getMouvements()).doesNotContain(mouvementBack);
        assertThat(mouvementBack.getFacture()).isNull();

        facture.mouvements(new HashSet<>(Set.of(mouvementBack)));
        assertThat(facture.getMouvements()).containsOnly(mouvementBack);
        assertThat(mouvementBack.getFacture()).isEqualTo(facture);

        facture.setMouvements(new HashSet<>());
        assertThat(facture.getMouvements()).doesNotContain(mouvementBack);
        assertThat(mouvementBack.getFacture()).isNull();
    }

    @Test
    void supplementsTest() {
        Facture facture = getFactureRandomSampleGenerator();
        LigneFactureSupplement ligneFactureSupplementBack = getLigneFactureSupplementRandomSampleGenerator();

        facture.addSupplements(ligneFactureSupplementBack);
        assertThat(facture.getSupplements()).containsOnly(ligneFactureSupplementBack);
        assertThat(ligneFactureSupplementBack.getFacture()).isEqualTo(facture);

        facture.removeSupplements(ligneFactureSupplementBack);
        assertThat(facture.getSupplements()).doesNotContain(ligneFactureSupplementBack);
        assertThat(ligneFactureSupplementBack.getFacture()).isNull();

        facture.supplements(new HashSet<>(Set.of(ligneFactureSupplementBack)));
        assertThat(facture.getSupplements()).containsOnly(ligneFactureSupplementBack);
        assertThat(ligneFactureSupplementBack.getFacture()).isEqualTo(facture);

        facture.setSupplements(new HashSet<>());
        assertThat(facture.getSupplements()).doesNotContain(ligneFactureSupplementBack);
        assertThat(ligneFactureSupplementBack.getFacture()).isNull();
    }

    @Test
    void navireTest() {
        Facture facture = getFactureRandomSampleGenerator();
        Navire navireBack = getNavireRandomSampleGenerator();

        facture.setNavire(navireBack);
        assertThat(facture.getNavire()).isEqualTo(navireBack);

        facture.navire(null);
        assertThat(facture.getNavire()).isNull();
    }

    @Test
    void clientTest() {
        Facture facture = getFactureRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        facture.setClient(clientBack);
        assertThat(facture.getClient()).isEqualTo(clientBack);

        facture.client(null);
        assertThat(facture.getClient()).isNull();
    }
}
