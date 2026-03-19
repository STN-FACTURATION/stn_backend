package sn.stn.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.stn.facturation.domain.FactureTestSamples.*;
import static sn.stn.facturation.domain.LigneFactureSupplementTestSamples.*;
import static sn.stn.facturation.domain.SupplementTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.stn.facturation.web.rest.TestUtil;

class LigneFactureSupplementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneFactureSupplement.class);
        LigneFactureSupplement ligneFactureSupplement1 = getLigneFactureSupplementSample1();
        LigneFactureSupplement ligneFactureSupplement2 = new LigneFactureSupplement();
        assertThat(ligneFactureSupplement1).isNotEqualTo(ligneFactureSupplement2);

        ligneFactureSupplement2.setId(ligneFactureSupplement1.getId());
        assertThat(ligneFactureSupplement1).isEqualTo(ligneFactureSupplement2);

        ligneFactureSupplement2 = getLigneFactureSupplementSample2();
        assertThat(ligneFactureSupplement1).isNotEqualTo(ligneFactureSupplement2);
    }

    @Test
    void supplementTest() {
        LigneFactureSupplement ligneFactureSupplement = getLigneFactureSupplementRandomSampleGenerator();
        Supplement supplementBack = getSupplementRandomSampleGenerator();

        ligneFactureSupplement.setSupplement(supplementBack);
        assertThat(ligneFactureSupplement.getSupplement()).isEqualTo(supplementBack);

        ligneFactureSupplement.supplement(null);
        assertThat(ligneFactureSupplement.getSupplement()).isNull();
    }

    @Test
    void factureTest() {
        LigneFactureSupplement ligneFactureSupplement = getLigneFactureSupplementRandomSampleGenerator();
        Facture factureBack = getFactureRandomSampleGenerator();

        ligneFactureSupplement.setFacture(factureBack);
        assertThat(ligneFactureSupplement.getFacture()).isEqualTo(factureBack);

        ligneFactureSupplement.facture(null);
        assertThat(ligneFactureSupplement.getFacture()).isNull();
    }
}
