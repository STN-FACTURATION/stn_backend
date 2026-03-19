package sn.stn.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.stn.facturation.domain.ClientTestSamples.*;
import static sn.stn.facturation.domain.NavireTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.stn.facturation.web.rest.TestUtil;

class NavireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Navire.class);
        Navire navire1 = getNavireSample1();
        Navire navire2 = new Navire();
        assertThat(navire1).isNotEqualTo(navire2);

        navire2.setId(navire1.getId());
        assertThat(navire1).isEqualTo(navire2);

        navire2 = getNavireSample2();
        assertThat(navire1).isNotEqualTo(navire2);
    }

    @Test
    void clientTest() {
        Navire navire = getNavireRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        navire.setClient(clientBack);
        assertThat(navire.getClient()).isEqualTo(clientBack);

        navire.client(null);
        assertThat(navire.getClient()).isNull();
    }
}
