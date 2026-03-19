package sn.stn.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.stn.facturation.domain.HistoriqueTarifTestSamples.*;
import static sn.stn.facturation.domain.TarifTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.stn.facturation.web.rest.TestUtil;

class HistoriqueTarifTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueTarif.class);
        HistoriqueTarif historiqueTarif1 = getHistoriqueTarifSample1();
        HistoriqueTarif historiqueTarif2 = new HistoriqueTarif();
        assertThat(historiqueTarif1).isNotEqualTo(historiqueTarif2);

        historiqueTarif2.setId(historiqueTarif1.getId());
        assertThat(historiqueTarif1).isEqualTo(historiqueTarif2);

        historiqueTarif2 = getHistoriqueTarifSample2();
        assertThat(historiqueTarif1).isNotEqualTo(historiqueTarif2);
    }

    @Test
    void tarifTest() {
        HistoriqueTarif historiqueTarif = getHistoriqueTarifRandomSampleGenerator();
        Tarif tarifBack = getTarifRandomSampleGenerator();

        historiqueTarif.setTarif(tarifBack);
        assertThat(historiqueTarif.getTarif()).isEqualTo(tarifBack);

        historiqueTarif.tarif(null);
        assertThat(historiqueTarif.getTarif()).isNull();
    }
}
