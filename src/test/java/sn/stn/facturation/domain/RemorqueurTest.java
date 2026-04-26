package sn.stn.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.stn.facturation.domain.RemorqueurTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.stn.facturation.web.rest.TestUtil;

class RemorqueurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Remorqueur.class);
        Remorqueur remorqueur1 = getRemorqueurSample1();
        Remorqueur remorqueur2 = new Remorqueur();
        assertThat(remorqueur1).isNotEqualTo(remorqueur2);

        remorqueur2.setId(remorqueur1.getId());
        assertThat(remorqueur1).isEqualTo(remorqueur2);

        remorqueur2 = getRemorqueurSample2();
        assertThat(remorqueur1).isNotEqualTo(remorqueur2);
    }
}
