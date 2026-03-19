package sn.stn.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.stn.facturation.domain.HistoriqueTarifTestSamples.*;
import static sn.stn.facturation.domain.TarifTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.stn.facturation.web.rest.TestUtil;

class TarifTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tarif.class);
        Tarif tarif1 = getTarifSample1();
        Tarif tarif2 = new Tarif();
        assertThat(tarif1).isNotEqualTo(tarif2);

        tarif2.setId(tarif1.getId());
        assertThat(tarif1).isEqualTo(tarif2);

        tarif2 = getTarifSample2();
        assertThat(tarif1).isNotEqualTo(tarif2);
    }

    @Test
    void historiquesTest() {
        Tarif tarif = getTarifRandomSampleGenerator();
        HistoriqueTarif historiqueTarifBack = getHistoriqueTarifRandomSampleGenerator();

        tarif.addHistoriques(historiqueTarifBack);
        assertThat(tarif.getHistoriques()).containsOnly(historiqueTarifBack);
        assertThat(historiqueTarifBack.getTarif()).isEqualTo(tarif);

        tarif.removeHistoriques(historiqueTarifBack);
        assertThat(tarif.getHistoriques()).doesNotContain(historiqueTarifBack);
        assertThat(historiqueTarifBack.getTarif()).isNull();

        tarif.historiques(new HashSet<>(Set.of(historiqueTarifBack)));
        assertThat(tarif.getHistoriques()).containsOnly(historiqueTarifBack);
        assertThat(historiqueTarifBack.getTarif()).isEqualTo(tarif);

        tarif.setHistoriques(new HashSet<>());
        assertThat(tarif.getHistoriques()).doesNotContain(historiqueTarifBack);
        assertThat(historiqueTarifBack.getTarif()).isNull();
    }
}
