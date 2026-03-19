package sn.stn.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.stn.facturation.domain.SupplementTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.stn.facturation.web.rest.TestUtil;

class SupplementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Supplement.class);
        Supplement supplement1 = getSupplementSample1();
        Supplement supplement2 = new Supplement();
        assertThat(supplement1).isNotEqualTo(supplement2);

        supplement2.setId(supplement1.getId());
        assertThat(supplement1).isEqualTo(supplement2);

        supplement2 = getSupplementSample2();
        assertThat(supplement1).isNotEqualTo(supplement2);
    }
}
