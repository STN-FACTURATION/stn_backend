package sn.stn.facturation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.stn.facturation.domain.ClientTestSamples.*;
import static sn.stn.facturation.domain.FactureTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.stn.facturation.web.rest.TestUtil;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void facturesTest() {
        Client client = getClientRandomSampleGenerator();
        Facture factureBack = getFactureRandomSampleGenerator();

        client.addFactures(factureBack);
        assertThat(client.getFactures()).containsOnly(factureBack);
        assertThat(factureBack.getClient()).isEqualTo(client);

        client.removeFactures(factureBack);
        assertThat(client.getFactures()).doesNotContain(factureBack);
        assertThat(factureBack.getClient()).isNull();

        client.factures(new HashSet<>(Set.of(factureBack)));
        assertThat(client.getFactures()).containsOnly(factureBack);
        assertThat(factureBack.getClient()).isEqualTo(client);

        client.setFactures(new HashSet<>());
        assertThat(client.getFactures()).doesNotContain(factureBack);
        assertThat(factureBack.getClient()).isNull();
    }
}
